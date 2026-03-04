package com.example.edums.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.edums.Mapper.ClassesMapper;
import com.example.edums.Mapper.CourseOpenMapper;
import com.example.edums.Mapper.CoursesMapper;
import com.example.edums.Mapper.TeacherMapper;
import com.example.edums.common.Result;
import com.example.edums.entity.Classes;
import com.example.edums.entity.CourseOpen;
import com.example.edums.entity.Courses;
import com.example.edums.entity.Teacher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name="班级信息接口")
@RequestMapping("/class")
@RestController
public class ClassController {
    @Resource
    private ClassesMapper classMapper;

    @Resource
    CourseOpenMapper courseOpenMapper;
    @Resource
    TeacherMapper teacherMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassController.class);
    @Autowired
    private CoursesMapper coursesMapper;

    // 根据id获取班级信息
    @Operation(summary = "根据id获取班级信息")
    @GetMapping("/{id}")
    public Result<?> getClassById(@PathVariable int id) {
        Classes selectedClass = classMapper.selectById(id);
        ArrayList<Teacher> teachers = new ArrayList<>();
        if (selectedClass != null) {
            LambdaQueryWrapper<CourseOpen> queryWrapper = Wrappers.lambdaQuery();
            List<CourseOpen> courseOpenList = courseOpenMapper.selectList(queryWrapper.eq(CourseOpen::getClassId, selectedClass.getClassId()).eq(CourseOpen::getStatus,1));
            LOGGER.info("courseOpenList"+courseOpenList);
            
            // 存储每个教师及其课程信息
            Map<String, Teacher> teacherMap = new HashMap<>();
            
            for (CourseOpen courseOpen : courseOpenList) {
                String teacherId = courseOpen.getTeacherId();
                
                // 查询教师信息
                Teacher teacher = teacherMapper.selectOne(new QueryWrapper<Teacher>().eq("teacher_id", teacherId));
                if (teacher == null) continue;
                
                // 查询课程信息
                Courses course = coursesMapper.selectOne(new QueryWrapper<Courses>().eq("course_id", courseOpen.getCourseId()));
                
                // 如果这个教师已经在map中，只需添加课程信息
                if (teacherMap.containsKey(teacherId)) {
                    Teacher existingTeacher = teacherMap.get(teacherId);
                    
                    // 如果教师的课程列表为空，创建一个新的
                    if (existingTeacher.getCourses() == null) {
                        existingTeacher.setCourses(new ArrayList<>());
                    }
                    
                    // 添加课程到教师的课程列表
                    if (course != null) {
                        existingTeacher.getCourses().add(course);
                    }
                } else {
                    // 第一次遇到这个教师，初始化他的课程列表
                    if (teacher.getCourses() == null) {
                        teacher.setCourses(new ArrayList<>());
                    }
                    
                    // 添加课程到教师的课程列表
                    if (course != null) {
                        teacher.getCourses().add(course);
                    }
                    
                    // 添加到map中
                    teacherMap.put(teacherId, teacher);
                }
            }
            
            // 将map中的所有教师添加到结果列表
            teachers.addAll(teacherMap.values());
            
            selectedClass.setCourseOpenTeacher(teachers);

            return Result.success(selectedClass);
        } else {
            return Result.error("-1", "未找到班级信息");
        }
    }

    @Operation(summary = "批量删除课程")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam List<Integer> ids) {
        LOGGER.info("DELETEBATCH class IDS:"+ids);
        for (Integer id : ids) {
            LambdaQueryWrapper<CourseOpen> queryWrapper = Wrappers.lambdaQuery();

            Classes classes = classMapper.selectById(id);
            queryWrapper.eq(CourseOpen::getClassId, classes.getClassId());
            List<CourseOpen> courseOpens = courseOpenMapper.selectList(queryWrapper);
            LOGGER.info("-----CLASS COURSE:"+courseOpens);
            if(courseOpens!=null&&!courseOpens.isEmpty()){
               return Result.error("-1","删除失败，id为"+classes.getClassId()+"的课程存在开课信息");
            }
        }
        int res = classMapper.deleteBatchIds(ids);
        if(res>0){
            return Result.success();
        }else{
            return Result.error("-1","删除失败");
        }

    }
    // 分页查询班级信息
    @Operation(summary = "分页查询班级信息")
    @GetMapping("/page")
    public Result<?> getClassesByPage(
            @RequestParam(defaultValue = "") String className,
            @RequestParam(defaultValue = "") String classId,
            @RequestParam(defaultValue = "") String college,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer size) {

        LambdaQueryWrapper<Classes> queryWrapper = Wrappers.lambdaQuery();
        if(StringUtils.isNotBlank(classId)){
            queryWrapper.like(Classes::getClassId,classId);
        }
        if(StringUtils.isNotBlank(college)){
            queryWrapper.like(Classes::getCollege,college);
        }
        if (StringUtils.isNotBlank(className)) {
            queryWrapper.like(Classes::getClassName, className);
        }

        Page<Classes> resultPage = classMapper.selectPage(new Page<>(currentPage, size), queryWrapper);
        return Result.success(resultPage);
    }

    // 获取所有班级信息
    @Operation(summary = "获取所有班级信息")
    @GetMapping
    public Result<?> getAllClasses(@RequestParam(defaultValue = "")String classId) {


        List<Classes> classes = classMapper.selectList(null);
        if (classes != null) {
            return Result.success(classes);
        } else {
            return Result.error("-1", "未找到班级信息");
        }
    }

    // 创建新的班级信息
    @Operation(summary = "创建新的班级信息")
    @PostMapping
    public Result<?> createClass(@RequestBody Classes classes) {
        String maxId =classMapper.selectMaxClassId();
        LOGGER.info("add class:"+classes);
        LOGGER.info("max id:"+maxId);
        if(maxId == null) {
            classes.setClassId("cl00001");
        } else {
            // 2. 解析最大ID并加一
            int maxIdInt = Integer.parseInt(maxId); // 移除'tl'字符并转换为整数
            maxIdInt++;
            // 3. 格式化新的ID
            String newId = String.format("cl%05d", maxIdInt);
            classes.setClassId(newId);
        }
        int res = classMapper.insert(classes);
        if (res > 0) {
            return Result.success(classes);
        } else {
            return Result.error("-1", "创建失败");
        }
    }

    // 更新班级信息
    @Operation(summary = "更新班级信息")
    @PutMapping("/{id}")
    public Result<?> updateClass(@PathVariable int id, @RequestBody Classes Classes) {
        Classes.setId(id);

        int res = classMapper.updateById(Classes);
        if (res > 0) {
            return Result.success(Classes);
        } else {
            return Result.error("-1", "更新失败");
        }
    }

    // 根据id删除班级信息
    @Operation(summary = "根据id删除班级信息")
    @DeleteMapping("/{id}")
    public Result<?> deleteClassById(@PathVariable int id) {
        LambdaQueryWrapper<CourseOpen> queryWrapper = Wrappers.lambdaQuery();

        Classes classes = classMapper.selectById(id);
        queryWrapper.eq(CourseOpen::getClassId, classes.getClassId());
        List<CourseOpen> courseOpens = courseOpenMapper.selectList(queryWrapper);
        LOGGER.info("-----CLASS COURSE:"+courseOpens);
        if(courseOpens!=null&&!courseOpens.isEmpty()){
            return Result.error("-1","删除失败，id为"+classes.getClassId()+"的课程存在开课信息");
        }
        int res = classMapper.deleteById(id);
        if (res > 0) {
            return Result.success();
        } else {
            return Result.error("-1", "删除失败");
        }
    }


}