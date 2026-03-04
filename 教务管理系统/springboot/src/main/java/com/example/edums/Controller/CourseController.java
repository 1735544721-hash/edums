package com.example.edums.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.example.edums.Mapper.CourseOpenMapper;
import com.example.edums.Mapper.CoursesMapper;
import com.example.edums.common.Result;
import com.example.edums.entity.CourseOpen;
import com.example.edums.entity.Courses;
import com.example.edums.entity.Teacher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Tag(name="课程信息接口")
@RequestMapping("/course")
@RestController
public class CourseController {
    @Resource
    CoursesMapper courseMapper;
    @Resource
    CourseOpenMapper courseOpenMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseController.class);

    // 根据id获取课程信息
    @Operation(summary = "根据id获取课程信息")
    @GetMapping("/{id}")
    public Result<?> getCourseById(@PathVariable int id) {
        Courses course = courseMapper.selectById(id);
        if (course != null) {
            return Result.success(course);
        } else {
            return Result.error("-1", "未找到课程信息");
        }
    }
    @Operation(summary = "批量删除课程")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam List<Integer> ids) {
        LOGGER.info("DELETEBATCH courses IDS:"+ids);
        for (Integer id : ids) {
            LambdaQueryWrapper<CourseOpen> queryWrapper = Wrappers.lambdaQuery();

            Courses courses = courseMapper.selectById(id);
            queryWrapper.eq(CourseOpen::getCourseId, courses.getCourseId());
            List<CourseOpen> courseOpens = courseOpenMapper.selectList(queryWrapper);
            LOGGER.info("-----CLASS COURSE:"+courseOpens);
            if(courseOpens!=null&&!courseOpens.isEmpty()){
                return Result.error("-1","删除失败，id为"+courses.getCourseId()+"的课程存在开课信息");
            }
        }
        int res = courseMapper.deleteBatchIds(ids);
        if(res>0){
            return Result.success();
        }else{
            return Result.error("-1","删除失败");
        }

    }
    // 分页查询课程信息
    @Operation(summary = "分页查询课程信息")
    @GetMapping("/page")
    public Result<?> getCoursesByPage(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String courseCode,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer size) {
        LambdaQueryWrapper<Courses> queryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(Courses::getCourseName, name);
        }
    if(StringUtils.isNotBlank(courseCode)){
        queryWrapper.like(Courses::getCourseCode,courseCode);
    }
        Page<Courses> resultPage = courseMapper.selectPage(new Page<>(currentPage, size), queryWrapper);
        return Result.success(resultPage);
    }

    // 获取所有课程信息
    @Operation(summary = "获取所有课程信息")
    @GetMapping
    public Result<?> getAllCourses() {
        List<Courses> courses = courseMapper.selectList(null);
        if (courses != null) {
            return Result.success(courses);
        } else {
            return Result.error("-1", "未找到课程信息");
        }
    }

    // 创建新的课程信息
    @Operation(summary = "创建新的课程信息")
    @PostMapping
    public Result<?> createCourse(@RequestBody Courses course) {
        String maxId = courseMapper.selectMaxCourseId();

        if(maxId == null) {
            course.setCourseId("c00001");
        } else {
            // 2. 解析最大ID并加一
            int maxIdInt = Integer.parseInt(maxId); // 移除't'字符并转换为整数
            maxIdInt++;
            // 3. 格式化新的ID
            String newId = String.format("c%05d", maxIdInt);
            course.setCourseId(newId);
        }
        int res = courseMapper.insert(course);
        if (res > 0) {
            return Result.success(course);
        } else {
            return Result.error("-1", "创建失败");
        }
    }

    // 更新课程信息
    @Operation(summary = "更新课程信息")
    @PutMapping("/{id}")
    public Result<?> updateCourse(@PathVariable int id, @RequestBody Courses course) {
        course.setId(id);
        String maxId = courseMapper.selectMaxCourseId();

        int res = courseMapper.updateById(course);
        if (res > 0) {
            return Result.success(course);
        } else {
            return Result.error("-1", "更新失败");
        }
    }

    // 根据id删除课程信息
    @Operation(summary = "根据id删除课程信息")
    @DeleteMapping("/{id}")
    public Result<?> deleteCourseById(@PathVariable int id) {
        LambdaQueryWrapper<CourseOpen> queryWrapper = Wrappers.lambdaQuery();

        Courses courses = courseMapper.selectById(id);
        queryWrapper.eq(CourseOpen::getCourseId, courses.getCourseId());
        List<CourseOpen> courseOpens = courseOpenMapper.selectList(queryWrapper);
        LOGGER.info("-----CLASS COURSE:"+courseOpens);
        if(courseOpens!=null&&!courseOpens.isEmpty()){
            return Result.error("-1","删除失败，id为"+courses.getCourseId()+"的课程存在开课信息");
        }

        int res = courseMapper.deleteById(id);
        if (res > 0) {
            return Result.success();
        } else {
            return Result.error("-1", "删除失败");
        }
    }
}