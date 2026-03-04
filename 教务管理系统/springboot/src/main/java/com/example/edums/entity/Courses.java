package com.example.edums.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "课程信息表实体")

@TableName("courses")
public class Courses {

    @TableId(type = IdType.AUTO)
    private int id;


    @Schema(example = "c00001", description = "课程ID")
    private String courseId;


    @Schema(example = "计算机科学导论", description = "课程名称")
    private String courseName;


    @Schema(example = "CS101", description = "课程代码")
    private String courseCode;



    @Schema(example = "Undergraduate", description = "班级类别")
    private String classType;


    @Schema(example = "College", description = "课程级别")
    private String courseLevel;
}