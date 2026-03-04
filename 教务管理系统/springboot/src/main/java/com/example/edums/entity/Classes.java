package com.example.edums.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "班级信息表实体")
@TableName( "classes")
public class Classes {
    @TableId(type = IdType.AUTO)
    private int id;


    @Schema(example = "cl01", description = "班级ID")
    private String classId;


    @Schema(example = "计算机科学与技术1班", description = "班级名称")
    private String className;


    @Schema(example = "计算机学院", description = "所属学院")
    private String college;


    @TableField(exist = false)
    @Schema(description = "开课教师记录")
    private List<Teacher> courseOpenTeacher;
}