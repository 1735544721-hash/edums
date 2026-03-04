package com.example.edums.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.edums.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TeacherMapper extends BaseMapper<Teacher> {

    // 这个方法用于查询最大的teacher_id
    @Select("SELECT MAX(CAST(SUBSTRING(teacher_id, 2) AS UNSIGNED)) FROM teachers")
    String selectMaxTeacherId();

}
