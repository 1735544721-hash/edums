package com.example.edums.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.example.edums.Mapper.*;
import com.example.edums.common.Result;
import com.example.edums.entity.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Tag(name="教师信息接口")
@RequestMapping("/teacher")
@RestController
public class TeacherController {
    @Resource
    CourseOpenMapper courseOpenMapper;
    @Resource
    private TeachingIncidentsMapper teachingIncidentsMapper;
    @Resource
    TeacherMapper teacherMapper;
    @Resource
    CoursesMapper coursesMapper;
    public static final Logger LOGGER = LoggerFactory.getLogger(TeacherController.class);

    // 根据id获取教师信息
    @Operation(summary = "根据id获取教师信息")
    @GetMapping("/{id}")
    public Result<?> getTeacherById(@PathVariable int id) {

        LOGGER.info("GET teacher by ID: " + id);
        Teacher teacher = teacherMapper.selectById(id);
        ArrayList<Courses> coursesArrayList = new ArrayList<>();

        if (teacher != null) {
            LambdaQueryWrapper<CourseOpen> queryWrapper = Wrappers.lambdaQuery();
            List<CourseOpen> courseOpenList = courseOpenMapper.selectList(queryWrapper.eq(CourseOpen::getTeacherId, teacher.getTeacherId()).eq(CourseOpen::getStatus,1));
           LOGGER.info("clssss:"+courseOpenList.toString());
            Set<String> courseIdSet = new HashSet<>(); // Create a set to store unique course_ids
            Set<String> classIdSet=new HashSet<>();
            for (CourseOpen courseOpen : courseOpenList) {
                Courses courses = new Courses();

                courses.setCourseId(courseOpen.getCourseId());
                if(courseOpen.getClassId()!=null){
                    classIdSet.add(courseOpen.getClassId());
                }
               Courses t_courses = coursesMapper.selectOne(new QueryWrapper<Courses>().eq("course_id", courses.getCourseId()));
               if(t_courses!=null){

                   coursesArrayList.add(courses);
                   courseIdSet.add(courses.getCourseId()); // Add course_id to the set
               }

            }
            teacher.setOpenCourses(coursesArrayList);
            teacher.setCourseCount(courseIdSet.size());
            teacher.setClassCount(classIdSet.size());
            teacherMapper.updateById(teacher);
            LOGGER.info(teacher.toString());
            return Result.success(teacher);
        } else {
            return Result.error("-1", "未找到教师信息");
        }
    }

    @Operation(summary = "密码修改")
    @PutMapping("/password/{id}")
    public Result<?> updatePassword(@PathVariable int id, @RequestBody UserPasswordUpdate userPasswordUpdate){
        Teacher oldUser = teacherMapper.selectById(id);
        if(oldUser==null){
            return Result.error("-1","用户不存在");
        }else{
            if(userPasswordUpdate.getOldPassword().equals(oldUser.getPassword())){
                oldUser.setPassword(userPasswordUpdate.getNewPassword());
                int res = teacherMapper.updateById(oldUser);
                if(res>0){
                    return Result.success();
                }else{
                    return Result.error("-1","修改失败,请联系管理员");
                }
            }else{
                return Result.error("-1","旧密码错误,请重试！");
            }
        }
    }
    @GetMapping("/getWithClassAndCourse")
   Result<?> getWithClassAndCourse(@RequestParam(defaultValue = "") String teacherId, @RequestParam (defaultValue = "") String classId){
        LOGGER.info("GET teacher getWithClassAndCourse: " + teacherId);
        Teacher teacher_t = new Teacher();
        teacher_t.setTeacherId(teacherId);
        Teacher teacher = teacherMapper.selectOne(new QueryWrapper<Teacher>().eq("teacher_id", teacher_t.getTeacherId()));
        ArrayList<Courses> coursesArrayList = new ArrayList<>();
        if (teacher != null) {
            LambdaQueryWrapper<CourseOpen> queryWrapper = Wrappers.lambdaQuery();
            List<CourseOpen> courseOpenList = courseOpenMapper.selectList(queryWrapper.eq(CourseOpen::getTeacherId, teacher.getTeacherId()).eq(CourseOpen::getClassId,classId).eq(CourseOpen::getStatus,1));

            for (CourseOpen courseOpen : courseOpenList) {
                Courses courses = new Courses();
                courses.setCourseId(courseOpen.getCourseId());
                Courses t_courses = coursesMapper.selectOne(new QueryWrapper<Courses>().eq("course_id", courses.getCourseId()));

                if(t_courses!=null){
                    coursesArrayList.add(t_courses);
                }

            }
            LOGGER.info("coursesArrayList:"+coursesArrayList);
            teacher.setOpenCourses(coursesArrayList);
            return Result.success(teacher);
        } else {
            return Result.error("-1", "未找到教师信息");
        }

    }

    // 分页查询教师信息
    @Operation(summary = "分页查询教师信息")
    @GetMapping("/page")
    public Result<?> getTeachersByPage(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "")String teacherId,
            @RequestParam(defaultValue = "")String username,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer size) {
        LOGGER.info("name:" + name + " currentPage:" + currentPage + " size:" + size);
        LambdaQueryWrapper<Teacher> queryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(Teacher::getName, name);
        }

        Page<Teacher> resultPage = teacherMapper.selectPage(new Page<>(currentPage, size), queryWrapper);
        LOGGER.info("Total: " + resultPage.getTotal());
        LOGGER.info("Teachers: " + resultPage.getRecords());
        LOGGER.info("Pages: " + resultPage.getPages());

        return Result.success(resultPage);
    }

    // 批量删除教师信息
    @Operation(summary = "批量删除教师信息")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam List<Integer> ids) {
        LOGGER.info("DELETEBATCH teacher IDs:" + ids);
        for (Integer id : ids) {
            LambdaQueryWrapper<CourseOpen> queryWrapper = Wrappers.lambdaQuery();
            LambdaQueryWrapper<TeachingIncidents> queryWrapper1 = Wrappers.lambdaQuery();

            Teacher teacher = teacherMapper.selectById(id);
            queryWrapper.eq(CourseOpen::getTeacherId, teacher.getTeacherId());
            queryWrapper1.eq(TeachingIncidents::getTeacherId,teacher.getTeacherId());
            List<CourseOpen> courseOpens = courseOpenMapper.selectList(queryWrapper);
            List<TeachingIncidents> teachingIncidents = teachingIncidentsMapper.selectList(queryWrapper1);
            if(ObjectUtils.isEmpty(teachingIncidents)){
                LOGGER.info("111111111111");
            }
            if(teachingIncidents!=null&&!teachingIncidents.isEmpty()){
                return Result.error("-1","删除失败，该教师存在教学事故记录，请处理后再操作");
            }
            LOGGER.info("-----CLASS COURSE:"+courseOpens);
            if(courseOpens!=null&&!courseOpens.isEmpty()){
                return Result.error("-1","删除失败，id为"+teacher.getTeacherId()+"的课程存在开课信息");
            }
        }
        int res = teacherMapper.deleteBatchIds(ids);
        if (res > 0) {
            return Result.success();
        } else {
            return Result.error("-1", "删除失败");
        }
    }

    // 获取所有教师信息
    @Operation(summary = "获取所有教师信息")
    @GetMapping
    public Result<?> getAllTeachers() {
        List<Teacher> teachers = teacherMapper.selectList(null);
        LOGGER.info("GET ALL teachers:" + teachers);
        if (teachers != null) {
            return Result.success(teachers);
        } else {
            return Result.error("-1", "未找到教师信息");
        }
    }

    // 创建新的教师信息
    @Operation(summary = "创建新的教师信息")
    @PostMapping
    public Result<?> createTeacher(@RequestBody Teacher teacher) {
        LOGGER.info("POST teacher: " + teacher);
        String maxId = teacherMapper.selectMaxTeacherId();
        if(maxId == null) {
            teacher.setTeacherId("t00001");
        } else {
            // 2. 解析最大ID并加一
            int maxIdInt = Integer.parseInt(maxId); // 移除't'字符并转换为整数
            maxIdInt++;

            // 3. 格式化新的ID
            String newId = String.format("t%05d", maxIdInt);
            teacher.setTeacherId(newId);
        }
        // 检查用户名是否已存在
        LambdaQueryWrapper<Teacher> queryWrapper = Wrappers.lambdaQuery();
        Teacher searchTeacher = teacherMapper.selectOne(queryWrapper.eq(Teacher::getUsername, teacher.getUsername()));
        if (searchTeacher != null) {
            return Result.error("-1", "注册失败，用户名已存在！");
        }




        // 插入新教师记录
        int res = teacherMapper.insert(teacher);
        if (res > 0) {
            return Result.success(teacher);
        } else {
            return Result.error("-1", "创建失败");
        }
    }
    // 更新教师信息
    @Operation(summary = "更新教师信息")
    @PutMapping("/{id}")
    public Result<?> updateTeacher(@PathVariable int id, @RequestBody Teacher teacher) {
        teacher.setId(id);
        Teacher teacher1 = teacherMapper.selectById(teacher.getId());
        LambdaQueryWrapper<Teacher> queryWrapper = Wrappers.lambdaQuery();
        Teacher searchTeacher = teacherMapper.selectOne(queryWrapper.eq(Teacher::getUsername, teacher.getUsername()));
        LOGGER.info("--------------------------:"+searchTeacher);
        if (!(teacher1.getUsername().equals(teacher.getUsername()))&&searchTeacher != null) {
            return Result.error("-1", "修改失败，用户名已存在！");
        }
        LOGGER.info("PUT teacher ID: " + id + ", teacher: " + teacher);
        int res = teacherMapper.updateById(teacher);
        if (res > 0) {
            return Result.success(teacher);
        } else {
            return Result.error("-1", "更新失败");
        }
    }

    // 根据id删除教师信息
    @Operation(summary = "根据id删除教师信息")
    @DeleteMapping("/{id}")
    public Result<?> deleteTeacherById(@PathVariable int id) {
        LOGGER.info("DELETE teacher ID: " + id);
        LambdaQueryWrapper<CourseOpen> queryWrapper = Wrappers.lambdaQuery();

        Teacher teacher = teacherMapper.selectById(id);
        queryWrapper.eq(CourseOpen::getTeacherId, teacher.getTeacherId());

        LambdaQueryWrapper<TeachingIncidents> queryWrapper1 = Wrappers.lambdaQuery();
        queryWrapper1.eq(TeachingIncidents::getTeacherId,teacher.getTeacherId());
        List<TeachingIncidents> teachingIncidents = teachingIncidentsMapper.selectList(queryWrapper1);

        if(teachingIncidents!=null&&!teachingIncidents.isEmpty()){
            return Result.error("-1","删除失败，该教师存在教学事故记录，请处理后再操作");
        }
        List<CourseOpen> courseOpens = courseOpenMapper.selectList(queryWrapper);
        LOGGER.info("-----CLASS COURSE:"+courseOpens);
        if(courseOpens!=null&&!courseOpens.isEmpty()){
            return Result.error("-1","删除失败，id为"+teacher.getTeacherId()+"的课程存在开课信息");
        }
        int res = teacherMapper.deleteById(id);
        if (res > 0) {
            return Result.success();
        } else {
            return Result.error("-1", "删除失败");
        }
    }
    @Operation(summary = "教师登录")
    @PostMapping("/login")
    public Result<?> login(@RequestBody Teacher teacher) {
        // 检查admin_id是否为空
        if (StringUtils.isBlank(teacher.getTeacherId())) {
            return Result.error("-1", "教师ID不能为空");
        }
        // 根据admin_id查询管理员信息
        LambdaQueryWrapper<Teacher> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Teacher::getTeacherId, teacher.getTeacherId());
        Teacher teacherInfo = teacherMapper.selectOne(queryWrapper);

        if(teacherInfo != null && teacherInfo.getStatus()==0){
            return Result.error("-1","该账号还未审核！");
        }
        // 如果管理员存在且密码匹配
        if (teacherInfo != null && teacherInfo.getPassword().equals(teacher.getPassword())) {
            // 登录成功，可以生成token等逻辑
            LOGGER.info("Login successful for admin: " + teacher.getTeacherId());
            // 这里返回登录成功结果，实际开发中可能需要返回token等信息
            return Result.success(teacherInfo);
        } else {
            // 登录失败，用户不存在或密码错误
            LOGGER.info("Login failed for admin: " + teacher.getTeacherId());
            return Result.error("-1", "用户名或密码错误");
        }

    }
    // 实现selectMaxTeacherId方法

}

