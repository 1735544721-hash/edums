package com.example.edums.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description="设置类实体")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("setting")
public class Setting {
    @TableId(type = IdType.AUTO)
    @Schema(description = "自增主键")
    private Integer id;
    @Schema(description = "主讲课门数")
    // 主讲课门数
    private Integer mainCoursesCount;
    @Schema(description = "学院行政干部的主讲课时")
    // 学院行政干部的主讲课时
    private Integer adminOfficerTeachingHours;
    @Schema(description = "普通教师的主讲课时")
    // 普通教师的主讲课时
    private Integer regularTeacherTeachingHours;
    @Schema(description = "上课的班数")
    // 上课的班数
    private Integer classesCounted;
}
