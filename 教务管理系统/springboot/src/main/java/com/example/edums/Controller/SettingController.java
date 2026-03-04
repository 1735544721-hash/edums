package com.example.edums.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.edums.Mapper.SettingMapper;
import com.example.edums.common.Result;
import com.example.edums.entity.Setting;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name="设置接口")
@RequestMapping("/setting")
@RestController
public class SettingController {


    @Resource
    private SettingMapper settingMapper;

    @GetMapping
    public Result<?> getSetting(){
        Setting setting = settingMapper.selectById(1);
        if(setting==null){
            Setting setting1 = new Setting(1, 4, 5, 10, 10);
            settingMapper.insert(setting1);
            return Result.success(setting1);
        }
       return Result.success(setting);

    }

    @PostMapping
    public Result<?> update(@RequestBody Setting setting){
        int updateCount = settingMapper.update(setting, new QueryWrapper<Setting>().eq("id", 1));

        // 如果更新的记录数为0，则表示没有找到ID为1的记录，执行插入操作
        if (updateCount == 0) {
            // 插入新记录之前，可以设置ID为1
            setting.setId(1);
            return settingMapper.insert(setting) > 0 ? Result.success() : Result.error("-1","操作失败");
        }
        return Result.success();

    }
}
