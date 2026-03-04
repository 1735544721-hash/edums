package com.example.edums.Controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.edums.Mapper.TeacherMapper;
import com.example.edums.Mapper.TeachingIncidentsMapper;
import com.example.edums.common.Result;
import com.example.edums.entity.Classes;
import com.example.edums.entity.CourseOpen;
import com.example.edums.entity.Teacher;
import com.example.edums.entity.TeachingIncidents;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/teachingIncidents")
@Tag(name = "教学事件管理")
public class TeachingIncidentsController {

    @Resource
    private TeachingIncidentsMapper teachingIncidentsMapper;
    @Resource
    private TeacherMapper teacherMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(TeachingIncidentsController.class);

    /**
     * 获取所有教学事件
     * @return 教学事件列表
     */
    @Operation(summary = "获取所有教学事件")
    @GetMapping
    public Result<?> getAllTeachingIncidents() {
        List<TeachingIncidents> incidents = teachingIncidentsMapper.selectList(null);
        LOGGER.info("incidents:" + incidents);
        return Result.success(incidents);
    }

    /**
     * 根据ID获取教学事件
     * @param id 教学事件ID
     * @return 教学事件对象
     */
    @Operation(summary = "根据ID获取教学事件")
    @GetMapping("/{id}")
    public Result<?> getTeachingIncidentById(@PathVariable int id) {
        TeachingIncidents incident = teachingIncidentsMapper.selectById(id);
        LOGGER.info("incident:" + incident);
        return Result.success(incident);
    }

    /**
     * 添加教学事件
     * @param incident 教学事件对象
     * @return 添加结果
     */
    @Operation(summary = "新增教学事件")
    @PostMapping
    public Result<?> addTeachingIncident(@RequestBody TeachingIncidents incident) {
        int res = teachingIncidentsMapper.insert(incident);
        LOGGER.info("NEW incident:" + incident);
        return res > 0 ? Result.success() : Result.error("-1", "新增失败");
    }

    /**
     * 更新教学事件
     * @param id 教学事件ID
     * @param incident 更新后的教学事件对象
     * @return 更新结果
     */
    @Operation(summary = "更新教学事件")
    @PutMapping("/{id}")
    public Result<?> updateTeachingIncident(@PathVariable int id, @RequestBody TeachingIncidents incident) {
        incident.setId(id);
        int res = teachingIncidentsMapper.updateById(incident);
        LOGGER.info("UPDATE incident:" + incident);
        return res > 0 ? Result.success() : Result.error("-1", "修改失败");
    }

    /**
     * 删除教学事件
     * @param id 教学事件ID
     * @return 删除结果
     */
    @Operation(summary = "根据ID删除教学事件")
    @DeleteMapping("/{id}")
    public Result<?> deleteTeachingIncidentById(@PathVariable int id) {
        int res = teachingIncidentsMapper.deleteById(id);
        LOGGER.info("DELETE incident ID:" + id);
        return res > 0 ? Result.success() : Result.error("-1", "删除失败");
    }

    @Operation(summary = "分页查询记录信息")
    @GetMapping("/page")
    public Result<?> getByPage(
            @RequestParam(defaultValue = "") String teacherId,
            @RequestParam(defaultValue = "") String id,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer size) {
        LambdaQueryWrapper<TeachingIncidents> queryWrapper = Wrappers.lambdaQuery();
        if(StringUtils.isNotBlank(teacherId)){
            queryWrapper.like(TeachingIncidents::getTeacherId,teacherId);
        }
        if(StringUtils.isNotBlank(id)){
            queryWrapper.like(TeachingIncidents::getId,id);
        }


        Page<TeachingIncidents> resultPage = teachingIncidentsMapper.selectPage(new Page<>(currentPage, size), queryWrapper);
        List<TeachingIncidents> records = resultPage.getRecords();
        for (TeachingIncidents record : records) {
            Teacher teacher = new Teacher();
            teacher.setTeacherId(record.getTeacherId());
            teacher   =teacherMapper.selectOne(new QueryWrapper<Teacher>().eq("teacher_id",teacher.getTeacherId()));
            record.setTeacherName(teacher.getName());
        }
        resultPage.setRecords(records);
        return Result.success(resultPage);
    }
    @Operation(summary = "批量删除教学事故信息")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam List<Integer> ids) {
        LOGGER.info("DELETEBATCH teachingIncidentsIDs:" + ids);
        int res = teachingIncidentsMapper.deleteBatchIds(ids);
        if (res > 0) {
            return Result.success();
        } else {
            return Result.error("-1", "删除失败");
        }
    }
    // 可以根据需要添加其他CRUD操作和相关的方法
}