package com.example.edums.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "教师信息表实体")
@TableName("teachers")
public class Teacher {


    @TableId(type = IdType.AUTO)
    private int id;


    @Schema(example = "t00001", description = "教师职工ID")
    private String teacherId;
    @Schema(example = "t00001", description = "用户名")
    private String username;
    private String password;


    @Schema(example = "张三", description = "教师姓名")
    private String name;


    @Schema(example = "male", description = "教师性别")
    private String gender;


    @Schema(example = "1", description = "教师职称")
    private int title;

    @Schema(example = "1", description = "教师已开课程数")
    private int courseCount;
    @Schema(example = "1", description = "教师周课时数")
    private int weeklyClassHours;
    @Schema(example = "1", description = "教师班级数")
    private int classCount;

    @TableField(exist = false)
    @Schema(description = "开课的课程")
    private List<Courses> openCourses;

    @TableField(exist = false)
    @Schema(description = "教师的所有课程")
    private List<Courses> courses;

    @Schema(example = "1", description = "1:普通教师 0：行政干部")
    private int teacherType;

    @Schema(example = "12345678901", description = "教师联系方式")
    private String phone;

    @Schema(example = "12345678901", description = "注册审核状态")
    private Integer status;

}