package com.example.edums.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.edums.Mapper.*;
import com.example.edums.common.Result;
import com.example.edums.entity.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "课程安排接口")
@RestController
@RequestMapping("/courseSchedule")
@RequiredArgsConstructor // Lombok注解，自动注入依赖
public class CourseScheduleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseScheduleController.class);
    @Resource
    private SettingMapper settingMapper;
@Resource
    private CourseScheduleMapper courseScheduleMapper;
@Resource
private ClassesMapper classesMapper;
@Resource
private TeacherMapper teacherMapper;
@Resource
private CoursesMapper coursesMapper;

    @Operation(summary = "获取教师课程表")
    @GetMapping("/teacher/{teacherId}")
    public Result<?> getTeacherSchedule(@PathVariable String teacherId) {
        LOGGER.info("获取教师课程表: teacherId={}", teacherId);
        
        if (StringUtils.isBlank(teacherId)) {
            return Result.error("-1", "教师ID不能为空");
        }
        
        // 查询教师信息
        Teacher teacher = teacherMapper.selectOne(new QueryWrapper<Teacher>().eq("teacher_id", teacherId));
        if (teacher == null) {
            return Result.error("-1", "找不到该教师信息");
        }
        
        // 查询该教师的所有课程安排
        LambdaQueryWrapper<CourseSchedule> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CourseSchedule::getTeacherId, teacherId);
        List<CourseSchedule> schedules = courseScheduleMapper.selectList(queryWrapper);
        
        // 补充课程和班级信息
        for (CourseSchedule schedule : schedules) {
            // 补充课程信息
            Courses course = coursesMapper.selectOne(new QueryWrapper<Courses>().eq("course_id", schedule.getCourseId()));
            if (course != null) {
                schedule.setCourseName(course.getCourseName());
            }
            
            // 补充班级信息
            Classes clazz = classesMapper.selectOne(new QueryWrapper<Classes>().eq("class_id", schedule.getClassId()));
            if (clazz != null) {
                schedule.setClassName(clazz.getClassName());
            }
        }
        
        return Result.success(schedules);
    }

    @Operation(summary = "获取班级课程表")
    @GetMapping("/timeTable")
public Result<?> getTimeTable(@RequestParam(defaultValue = "")String classId){
        LambdaQueryWrapper<CourseSchedule> objectLambdaQueryWrapper = Wrappers.lambdaQuery();
        if(StringUtils.isNotBlank(classId)){
            objectLambdaQueryWrapper.eq(CourseSchedule::getClassId,classId);
        }
        List<CourseSchedule> courseSchedules = courseScheduleMapper.selectList(objectLambdaQueryWrapper);
        ArrayList<TimeTable> timeTables = new ArrayList<>();
        Map<String, Integer> weekDayMap = new HashMap<>();
        weekDayMap.put("周一", 1);
        weekDayMap.put("周二", 2);
        weekDayMap.put("周三", 3);
        weekDayMap.put("周四", 4);
        weekDayMap.put("周五", 5);
        weekDayMap.put("周六", 6);
        weekDayMap.put("周日", 7);
        if(courseSchedules!=null&&!courseSchedules.isEmpty()) {
            for (CourseSchedule courseSchedule : courseSchedules) {
                TimeTable timeTable = new TimeTable();
//            Courses courses = new Courses();
//            courses.setCourseId(courses.getCourseId());
                QueryWrapper<Courses> coursesQueryWrapper = new QueryWrapper<>();
                coursesQueryWrapper.eq("course_id", courseSchedule.getCourseId());

                QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
                teacherQueryWrapper.eq("teacher_id", courseSchedule.getTeacherId());
                Teacher teacher = teacherMapper.selectOne(teacherQueryWrapper);
                Courses courses = coursesMapper.selectOne(coursesQueryWrapper);
                if (courses != null) {
                    timeTable.setCourseId(courseSchedule.getCourseId());
                    timeTable.setCourseName(courses.getCourseName());
                }
                if (teacher != null) {
                    timeTable.setTeacherName(teacher.getName());
                }

                timeTable.setDayOfWeek(weekDayMap.get(courseSchedule.getDayOfWeek()));
                timeTable.setStart(courseSchedule.getClassPeriod());
                timeTable.setEnd(courseSchedule.getClassPeriod());
                timeTable.setContent("");
                timeTables.add(timeTable);
            }
        }else{
            TimeTable timeTable = new TimeTable();
            timeTable.setCourseId("本日没有课程哦");
            timeTable.setCourseName("本日没有课程哦");
            timeTable.setStart(1);
            timeTable.setEnd(1);
            timeTable.setDayOfWeek(1);
            timeTable.setContent("本日没有课程哦");
            timeTables.add(timeTable);
        }

        return Result.success(timeTables);
}
    @Operation(summary = "获取所有课程安排")
    @GetMapping
    public Result<?> getAllCourseSchedules(@RequestParam(defaultValue = "") String classId) {
        LOGGER.info("getAllCourseSchedules classID:"+classId);
        LambdaQueryWrapper<CourseSchedule> objectLambdaQueryWrapper = Wrappers.lambdaQuery();
        if(StringUtils.isNotBlank(classId)){
            objectLambdaQueryWrapper.eq(CourseSchedule::getClassId,classId);

        }
        List<CourseSchedule> courseSchedules = courseScheduleMapper.selectList(objectLambdaQueryWrapper);
        LOGGER.info("Retrieved course schedules: {}", courseSchedules);
        return Result.success(courseSchedules);
    }
    @PostMapping("/postList")
    public Result<?> planCoursesList(@RequestBody CourseScheduleList courseScheduleList){
        Setting setting = settingMapper.selectById(1);
        if(setting==null){
            return Result.error("-1","系统异常，请联系管理员");
        }
        LOGGER.info(courseScheduleList.toString());
        
        // 检查课程表是否有重复的时间安排
        Map<String, List<CourseSchedule>> timeSlotMap = new HashMap<>();
        for (CourseSchedule schedule : courseScheduleList.getScheduleList()) {
            if (schedule.getDayOfWeek() != null && schedule.getClassPeriod() != null) {
                String timeKey = schedule.getDayOfWeek() + "-" + schedule.getClassPeriod();
                
                if (!timeSlotMap.containsKey(timeKey)) {
                    timeSlotMap.put(timeKey, new ArrayList<>());
                }
                
                timeSlotMap.get(timeKey).add(schedule);
                
                // 检查该时间段是否有多个安排
                if (timeSlotMap.get(timeKey).size() > 1) {
                    return Result.error("-1", "在" + schedule.getDayOfWeek() + "的第" + schedule.getClassPeriod() + "节课有多个课程安排，请检查并修正");
                }
            }
        }
        
        // 获取当前班级的课程表
        LambdaQueryWrapper<CourseSchedule> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CourseSchedule::getClassId, courseScheduleList.getClassId());
        List<CourseSchedule> courseSchedulesByClass = courseScheduleMapper.selectList(queryWrapper);
        
        // 处理删除的课程安排，减少教师的周课时
        for (CourseSchedule schedulesByClass : courseSchedulesByClass) {
            String teacherId = schedulesByClass.getTeacherId();
            
            // 确保TeacherId不为空
            if (StringUtils.isBlank(teacherId)) {
                continue;
            }
            
            Teacher teacher = teacherMapper.selectOne(new QueryWrapper<Teacher>().eq("teacher_id", teacherId));
            if (teacher != null) {
                // 减少教师的周课时
                int newHours = Math.max(0, teacher.getWeeklyClassHours() - 1); // 防止负值
                teacher.setWeeklyClassHours(newHours);
                teacherMapper.updateById(teacher);
                LOGGER.info("减少教师[{}]的周课时，当前为: {}", teacher.getName(), newHours);
            }
        }
        
        // 删除当前班级的所有课程安排
        courseScheduleMapper.delete(queryWrapper);
        
        // 处理新的课程安排
        List<String> errors = new ArrayList<>();
        for (CourseSchedule courseSchedule : courseScheduleList.getScheduleList()) {
            // 确保必要信息完整
            if (courseSchedule == null || 
                StringUtils.isBlank(courseSchedule.getTeacherId()) || 
                StringUtils.isBlank(courseSchedule.getCourseId()) || 
                StringUtils.isBlank(courseSchedule.getDayOfWeek()) || 
                courseSchedule.getClassPeriod() == null) {
                continue; // 跳过不完整的数据
            }
            
            String teacherId = courseSchedule.getTeacherId();
            
            // 查询教师信息
            Teacher teacher = teacherMapper.selectOne(new QueryWrapper<Teacher>().eq("teacher_id", teacherId));
            if (teacher == null) {
                errors.add("找不到ID为" + teacherId + "的教师信息");
                continue;
            }
            
            // 检查教师周课时限制
            if (teacher.getTeacherType() == 0 && teacher.getWeeklyClassHours() >= setting.getAdminOfficerTeachingHours()) {
                errors.add("教师:" + teacher.getName() + "已经超过周课时限制(" + teacher.getWeeklyClassHours() + "学时/周)。含有行政身份的教师主讲课时不得超过"+setting.getAdminOfficerTeachingHours()+"学时/周");
                continue;
            } else if (teacher.getTeacherType() == 1 && teacher.getWeeklyClassHours() >= setting.getRegularTeacherTeachingHours()) {
                errors.add("教师:" + teacher.getName() + "已经超过周课时限制(" + teacher.getWeeklyClassHours() + "学时/周)。普通教师主讲课时不得超过"+setting.getRegularTeacherTeachingHours()+"学时/周");
                continue;
            }
            
            // 更新教师周课时数
            teacher.setWeeklyClassHours(teacher.getWeeklyClassHours() + 1);
            teacherMapper.updateById(teacher);
            LOGGER.info("增加教师[{}]的周课时，当前为: {}", teacher.getName(), teacher.getWeeklyClassHours());

            // 添加班级信息到课程表
            courseSchedule.setClassId(courseScheduleList.getClassId());
            Classes classes = classesMapper.selectOne(new QueryWrapper<Classes>().eq("class_id", courseScheduleList.getClassId()));
            if (classes != null) {
                courseSchedule.setClassName(classes.getClassName());
            }
            
            // 添加课程和教师的名称信息，便于前端显示
            Courses course = coursesMapper.selectOne(new QueryWrapper<Courses>().eq("course_id", courseSchedule.getCourseId()));
            if (course != null) {
                courseSchedule.setCourseName(course.getCourseName());
            }
            courseSchedule.setTeacherName(teacher.getName());
            
            // 插入课程安排
            int insert = courseScheduleMapper.insert(courseSchedule);
            if (insert <= 0) {
                errors.add("添加课程安排失败: " + courseSchedule.getCourseName());
            }
        }
        
        // 如果有错误，返回第一个错误信息
        if (!errors.isEmpty()) {
            return Result.error("-1", errors.get(0));
        }
        
        return Result.success();
    }

    @Operation(summary = "分页查询课程安排")
    @GetMapping("/page")
    public Result<?> getCourseSchedulesByPage(
            @RequestParam(defaultValue = "")String teacherId,
            @RequestParam(defaultValue = "")String classId,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer size) {
        LOGGER.info(teacherId+" "+classId+" "+currentPage+" "+size);
        LambdaQueryWrapper<CourseSchedule> objectLambdaQueryWrapper = Wrappers.lambdaQuery();
        if(StringUtils.isNotBlank(teacherId)){
            objectLambdaQueryWrapper.eq(CourseSchedule::getTeacherId,teacherId);
        }
        if(StringUtils.isNotBlank(classId)){
            objectLambdaQueryWrapper.eq(CourseSchedule::getClassId,classId);
        }
        Page<CourseSchedule> courseSchedulePage = courseScheduleMapper.selectPage(new Page<>(currentPage, size), objectLambdaQueryWrapper);
        LOGGER.info("Retrieved course schedules page: {}", courseSchedulePage.getRecords());
        List<CourseSchedule> records = courseSchedulePage.getRecords();
        for (CourseSchedule record : records) {
            QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
           teacherQueryWrapper.eq("teacher_id", record.getTeacherId());
            Teacher teacher = teacherMapper.selectOne(teacherQueryWrapper);
            if(teacher!=null){
                record.setTeacherName(teacher.getName());
            }
            QueryWrapper<Courses> coursesQueryWrapper = new QueryWrapper<>();
            coursesQueryWrapper.eq("course_id",record.getCourseId());
            Courses courses = coursesMapper.selectOne(coursesQueryWrapper);
            if(courses!=null){
                record.setCourseName(courses.getCourseName());
            }
        }
        courseSchedulePage.setRecords(records);
        LOGGER.info("Retrieved course schedules page: {}", courseSchedulePage.getRecords());
        return Result.success(courseSchedulePage);
    }

    @Operation(summary = "根据ID获取课程安排")
    @GetMapping("/{id}")
    public Result<?> getCourseScheduleById(@PathVariable Integer id) {
        CourseSchedule courseSchedule = courseScheduleMapper.selectById(id);
        LOGGER.info("Retrieved course schedule with id {}: {}", id, courseSchedule);
        return Result.success(courseSchedule);
    }

    @Operation(summary = "新增课程安排")
    @PostMapping
    public Result<?> addCourseSchedule(@RequestBody CourseSchedule courseSchedule) {
        int result = courseScheduleMapper.insert(courseSchedule);
        LOGGER.info("Added new course schedule: {}", courseSchedule);
        return result > 0 ? Result.success() : Result.error("-1", "新增失败");
    }

    @Operation(summary = "更新课程安排")
    @PutMapping("/{id}")
    public Result<?> updateCourseSchedule(@PathVariable Integer id, @RequestBody CourseSchedule courseSchedule) {
        courseSchedule.setId(id);
        int result = courseScheduleMapper.updateById(courseSchedule);
        LOGGER.info("Updated course schedule: {}", courseSchedule);
        return result > 0 ? Result.success() : Result.error("-1", "更新失败");
    }

    @Operation(summary = "批量删除课程安排")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatchCourseSchedules(@RequestParam List<Integer> ids) {
        int result = courseScheduleMapper.deleteBatchIds(ids);
        LOGGER.info("Deleted course schedules with ids: {}", ids);
        return result > 0 ? Result.success() : Result.error("-1", "删除失败");
    }

    @Operation(summary = "根据ID删除课程安排")
    @DeleteMapping("/{id}")
    public Result<?> deleteCourseScheduleById(@PathVariable Integer id) {
        int result = courseScheduleMapper.deleteById(id);
        LOGGER.info("Deleted course schedule with id: {}", id);
        return result > 0 ? Result.success() : Result.error("-1", "删除失败");
    }
}