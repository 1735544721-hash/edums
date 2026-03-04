package com.example.edums.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.edums.entity.Classes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ClassesMapper extends BaseMapper<Classes> {
    @Select("SELECT MAX(CAST(SUBSTRING(class_id, 3) AS UNSIGNED)) FROM classes")
    String selectMaxClassId();
}
