package com.example.edums.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@Data // Lombok注解，自动生成所有getter和setter方法、equals、hashCode和toString方法
@NoArgsConstructor // Lombok注解，生成无参构造函数
@AllArgsConstructor // Lombok注解，生成全参构造函数
@Builder // Lombok注解，生成构建者模式代码
@Schema(description = "教学事故表实体")
@TableName("teaching_incidents")
public class TeachingIncidents {
        /**
         * 事件ID
         */
        @Schema(description = "事件id")
        @TableId(type = IdType.AUTO)
        private int id;
        /**
         * 教师ID
         */

        @Schema(description = "教师id")
        private String teacherId;

        /**
         * 事件描述
         */
        @Schema(description = "事故描述")
        private String description;

        /**
         * 事件发生日期
         */        @Schema(description = "发生日期")
        @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
        private Date date;

        @TableField(exist = false)
        @Schema(description = "教师姓名")
         private String teacherName;






}
