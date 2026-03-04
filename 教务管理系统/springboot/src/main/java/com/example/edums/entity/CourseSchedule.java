package com.example.edums.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "授课计划表实体")
@AllArgsConstructor
@TableName( "course_schedule")
public class CourseSchedule {
    /**
     * 唯一标识
     */    @Schema(description = "ID")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 班级编号
     */
  @Schema(description = "班级ID")
    private String classId;

    /**
     * 班级名称
     */
    @Schema(description = "班级名称")
    private String className;

    /**
     * 教师ID
     */
    @Schema(description = "教师id")
    private String teacherId;

    /**
     * 课程ID
     */
    @Schema(description = "课程id")
    private String courseId;

    /**
     * 周几，枚举类型
     */
    @Schema(description = "周几")
    private String dayOfWeek;
  @TableField(exist = false)
    private String teacherName;
  @TableField(exist = false)
  private String courseName;

    /**
     * 节次
     */
    @Schema(description = "节次")
    private Integer classPeriod;
}
