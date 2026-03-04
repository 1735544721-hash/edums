package com.example.edums.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@Schema(description = "开课实体")
@AllArgsConstructor
@TableName( "course_open")
public class CourseOpen {

    @TableId(type = IdType.AUTO)
    private int id;

    @Schema(example = "t00001", description = "教师ID")

    private String teacherId;

    @Schema(example = "cl01", description = "班级ID")

    private String classId;

    @Schema(example = "c00001", description = "课程ID")

    private String courseId;

    @Schema(example = "0", description = "审核状态")
    private int status;

    @TableField(exist = false)
    @Schema(description = "教师姓名")
    private String teacherName;

    @TableField(exist = false)
    @Schema(description = "课程名称")
    private String courseName;


}