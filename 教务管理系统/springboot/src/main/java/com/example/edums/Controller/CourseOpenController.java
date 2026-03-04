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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="课程开放接口")
@RequestMapping("/courseOpen")
@RestController
public class CourseOpenController {
    @Resource
    private SettingMapper settingMapper;
    @Resource
    private CourseOpenMapper courseOpenMapper;
    @Resource
    private TeacherMapper teacherMapper;
    @Resource
    private CoursesMapper coursesMapper;

    @Resource
    private TeachingIncidentsMapper teachingIncidentsMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseOpenController.class);

    /**
     * 获取所有课程开放信息
     * @return 课程开放列表
     */
    @Operation(summary = "获取所有课程开放信息")
    @GetMapping
    public Result<?> getAllCourseOpens(@RequestParam(defaultValue = "")String classId ,@RequestParam(defaultValue = "")String teacherId) {
        LOGGER.info("getAllCourseOpens  "+classId);
        LambdaQueryWrapper<CourseOpen> queryWrapper = Wrappers.lambdaQuery();
        if(StringUtils.isNotBlank(classId)){
            queryWrapper.eq(CourseOpen::getClassId,classId);
        }
        if(StringUtils.isNotBlank(teacherId)){
            queryWrapper.eq(CourseOpen::getTeacherId,teacherId);
        }
        List<CourseOpen> courseOpens = courseOpenMapper.selectList(queryWrapper);
        for (CourseOpen courseOpen : courseOpens) {
            Courses courses = new Courses();
            courses.setCourseId(courseOpen.getCourseId());
            QueryWrapper<Courses> coursesQueryWrapper = new QueryWrapper<>();
            coursesQueryWrapper.eq("course_id",courses.getCourseId());
            Courses courses1 = coursesMapper.selectOne(coursesQueryWrapper);
            if(courses1!=null){
                courseOpen.setCourseName(courses1.getCourseName());
            }
//            Teacher teacher = new Teacher();
//            teacher.setTeacherId(courseOpen.getTeacherId());
//            QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
//    teacherQueryWrapper.eq("teacher_id", teacher.getTeacherId());
//            Teacher teacher1 = teacherMapper.selectOne(teacherQueryWrapper);
//            if(teacher1!=null){
//                courseOpen.setTeacherName(teacher1.getName());
//            }

        }
        LOGGER.info("courseOpens:" + courseOpens);
        return Result.success(courseOpens);
    }

    /**
     * 根据ID获取课程开放信息
     * @param id 课程开放ID
     * @return 课程开放对象
     */
    @Operation(summary = "根据id获取课程开放信息")
    @GetMapping("/{id}")
    public Result<?> getCourseOpenById(@PathVariable int id) {
        CourseOpen courseOpen = courseOpenMapper.selectById(id);
        LOGGER.info("courseOpen:" + courseOpen);
        return Result.success(courseOpen);
    }


    /**
     * 添加课程开放信息
     * @param courseOpen 课程开放对象
     * @return 添加结果
     */
    @Operation(summary = "新增课程开设信息")
    @PostMapping
    public Result<?> addCourseOpen(@RequestBody CourseOpen courseOpen) {
        Setting setting = settingMapper.selectById(1);
        if(setting==null){
            return Result.error("-1","系统异常，请联系管理员");
        }
        LOGGER.info("OPEN COUSRE:"+courseOpen);
        LambdaQueryWrapper<CourseOpen> queryWrapper = Wrappers.lambdaQuery();
        List<CourseOpen> courseOpens = courseOpenMapper.selectList(queryWrapper.eq(CourseOpen::getClassId, courseOpen.getClassId()).eq(CourseOpen::getCourseId, courseOpen.getCourseId()));
        Courses courses = new Courses();
        courses.setCourseId(courseOpen.getCourseId());
        courses=coursesMapper.selectOne(new QueryWrapper<Courses>().eq("course_id",courses.getCourseId()));

        Teacher teacher = new Teacher();
        teacher.setTeacherId(courseOpen.getTeacherId());
        teacher =teacherMapper.selectOne(new QueryWrapper<Teacher>().eq("teacher_id", teacher.getTeacherId()));
        TeachingIncidents teachingIncidents = new TeachingIncidents();
        teachingIncidents.setTeacherId(courseOpen.getTeacherId());
        teachingIncidents=   teachingIncidentsMapper.selectOne(new QueryWrapper<TeachingIncidents>().eq("teacher_id",teachingIncidents.getTeacherId()));
        //每位教师主讲课程数不得大于2
        if(teachingIncidents!=null){
            return Result.error("-1","您存在教学事故记录，不得开设课程,请处理后再开设");
        }
        if(teacher.getCourseCount()>=setting.getMainCoursesCount()){
            return Result.error("-1","每位教师主讲课程数不得大于"+setting.getMainCoursesCount()+"，您的主讲课程数为："+teacher.getCourseCount());
        }
        if(teacher.getClassCount()>=setting.getClassesCounted()){
            LambdaQueryWrapper<CourseOpen> queryWrapper1 = Wrappers.lambdaQuery();
            List<CourseOpen> courseOpensByClass = courseOpenMapper.selectList(queryWrapper1.eq(CourseOpen::getClassId, courseOpen.getClassId()).eq(CourseOpen::getTeacherId, courseOpen.getTeacherId()));
            if(courseOpensByClass.isEmpty()){
                return Result.error("-1","每位教师开班数不得大于"+setting.getClassesCounted()+"，您的开班数为："+teacher.getClassCount());
            }
        }
        if(courses.getCourseLevel().equals("College")){
            //教师职称 1：教授 2：副教授 3：讲师  4：助教
            if(teacher.getTitle()>=3){//讲师以下职称的教师不能承担学院定主课的主讲任务；
                    return Result.error("-1","讲师以下职称的教师不能承担学院定主课的主讲任务");
            }
        }

        if(courseOpens!=null&&!courseOpens.isEmpty()){
            return Result.error("-1","该班已开设该课程,请勿重复开课");
        }
        int res = courseOpenMapper.insert(courseOpen);
        LOGGER.info("NEW courseOpen:" + courseOpen);
        if (res > 0) {
            return Result.success();
        } else {
            return Result.error("-1", "开设失败，请联系管理员");
        }
    }

    /**
     * 更新课程开放信息
     * @param id 课程开放ID
     * @param courseOpen 更新后的课程开放对象
     * @return 更新结果
     */
    @Operation(summary = "更新课程开放信息")
    @PutMapping("/{id}")
    public Result<?> updateCourseOpen(@PathVariable int id, @RequestBody CourseOpen courseOpen) {
        courseOpen.setId(id);
        LOGGER.info("UPDATE courseOpen:" + courseOpen);
        int res = courseOpenMapper.updateById(courseOpen);
        if (res > 0) {
            return Result.success();
        } else {
            return Result.error("-1", "修改失败");
        }
    }

    /**
     * 批量删除课程开放信息
     * @param ids 课程开放ID列表
     * @return 删除结果
     */
    @Operation(summary = "批量删除课程开放信息")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam List<Integer> ids) {
        LOGGER.info("DELETEBATCH courseOpen IDS:" + ids);
        int res = courseOpenMapper.deleteBatchIds(ids);
        if (res > 0) {
            return Result.success();
        } else {
            return Result.error("-1", "删除失败");
        }
    }
    @Operation(summary = "分页查询记录信息")
    @GetMapping("/page")
    public Result<?> getByPage(
            @RequestParam(defaultValue = "") String teacherId,
            @RequestParam(defaultValue = "")String classId,
            @RequestParam(defaultValue = "") String courseId,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer size) {
        LambdaQueryWrapper<CourseOpen> queryWrapper = Wrappers.lambdaQuery();
        if(StringUtils.isNotBlank(teacherId)){
            queryWrapper.like(CourseOpen::getTeacherId,teacherId);
        }
        if(StringUtils.isNotBlank(classId)){
            queryWrapper.like(CourseOpen::getClassId,classId);
        }
        if(StringUtils.isNotBlank(courseId)){
            queryWrapper.like(CourseOpen::getCourseId,courseId);
        }


        Page<CourseOpen> resultPage = courseOpenMapper.selectPage(new Page<>(currentPage, size), queryWrapper);
        List<CourseOpen> records = resultPage.getRecords();
        for (CourseOpen record : records) {
            Teacher teacher = new Teacher();
            teacher.setTeacherId(record.getTeacherId());
            teacher   =teacherMapper.selectOne(new QueryWrapper<Teacher>().eq("teacher_id",teacher.getTeacherId()));
            record.setTeacherName(teacher.getName());
            Courses courses = new Courses();
            courses.setCourseId(record.getCourseId());
           courses= coursesMapper.selectOne(new QueryWrapper<Courses>().eq("course_id",courses.getCourseId()));
           record.setCourseName(courses.getCourseName());
        }
        resultPage.setRecords(records);
        return Result.success(resultPage);
    }
    /**
     * 删除课程开放信息
     * @param id 课程开放ID
     * @return 删除结果
     */
    @Operation(summary = "根据id删除课程开放信息")
    @DeleteMapping("/{id}")
    public Result<?> deleteCourseOpenById(@PathVariable int id) {
        LOGGER.info("DELETE courseOpen ID:" + id);
        int res = courseOpenMapper.deleteById(id);
        if (res > 0) {
            return Result.success();
        } else {
            return Result.error("-1", "删除失败");
        }
    }
}