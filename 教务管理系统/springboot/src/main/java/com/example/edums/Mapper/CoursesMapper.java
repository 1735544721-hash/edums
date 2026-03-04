package com.example.edums.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.edums.entity.Courses;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CoursesMapper extends BaseMapper<Courses> {
    @Select("SELECT MAX(CAST(SUBSTRING(course_id, 2) AS UNSIGNED)) FROM courses")
    String selectMaxCourseId();
}
