package com.example.edums.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "管理员信息表实体")

@TableName("admin")
public class Admin {

    @TableId(type = IdType.AUTO)
    private int id;

    @Column(name = "admin_id", nullable = false, unique = true)
    @Schema(example = "a00001", description = "管理员职工ID")
    private String adminId;

    @Column(name = "password", nullable = false)
    private String password;
}