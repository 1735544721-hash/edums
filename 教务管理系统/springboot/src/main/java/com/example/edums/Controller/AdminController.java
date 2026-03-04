package com.example.edums.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.edums.Mapper.AdminMapper;
import com.example.edums.common.Result;
import com.example.edums.entity.Admin;
import com.example.edums.entity.Teacher;
import com.example.edums.entity.UserPasswordUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@Tag(name="管理员信息接口")
@RequestMapping("/admin")
@RestController
public class AdminController {
    @Resource
    AdminMapper adminMapper;
    public static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Operation(summary = "管理员登录")
    @PostMapping("/login")
    public Result<?> login(@RequestBody Admin admin) {
        // 检查admin_id是否为空
        if (StringUtils.isBlank(admin.getAdminId())) {
            return Result.error("-1", "管理员职工ID不能为空");
        }
        // 根据admin_id查询管理员信息
        LambdaQueryWrapper<Admin> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Admin::getAdminId, admin.getAdminId());
        Admin adminInfo = adminMapper.selectOne(queryWrapper);

        // 如果管理员存在且密码匹配
        if (adminInfo != null && adminInfo.getPassword().equals(admin.getPassword())) {
            // 登录成功，可以生成token等逻辑
            LOGGER.info("Login successful for admin: " + admin.getAdminId());
            // 这里返回登录成功结果，实际开发中可能需要返回token等信息
            return Result.success(adminInfo);
        } else {
            // 登录失败，用户不存在或密码错误
            LOGGER.info("Login failed for admin: " + admin.getAdminId());
            return Result.error("-1", "用户名或密码错误");
        }

    }

    @Operation(summary = "密码修改")
    @PutMapping("/password/{id}")
    public Result<?> updatePassword(@PathVariable int id, @RequestBody UserPasswordUpdate userPasswordUpdate){
        Admin oldUser = adminMapper.selectById(id);
        if(oldUser==null){
            return Result.error("-1","用户不存在");
        }else{
            if(userPasswordUpdate.getOldPassword().equals(oldUser.getPassword())){
                oldUser.setPassword(userPasswordUpdate.getNewPassword());
                int res = adminMapper.updateById(oldUser);
                if(res>0){
                    return Result.success();
                }else{
                    return Result.error("-1","修改失败");
                }
            }else{
                return Result.error("-1","旧密码错误,请重试！");
            }
        }
    }
}