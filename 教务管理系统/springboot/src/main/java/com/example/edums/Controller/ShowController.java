package com.example.edums.Controller;


import com.example.edums.common.Result;
import com.example.edums.entity.ShowEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.time.LocalDate;

@RestController
@Tag(name = "首页展示信息接口")
@RequestMapping("/show")
public class ShowController {

    @Operation(summary = "获取首页卡片信息")
    @GetMapping("/getCardInfo")
    public Result<?> getCardInfo(){
//        LocalDate lastMonthFirstDay = DateUtils.getLastMonthFirstDay();
//        Long userCount = userMapper.selectCount(null)-1;
//        Long orderCount = ordersMapper.selectCount(null);
//        Long goodsCount = goodsMapper.selectCount(null);
//        Double moneyCount = ordersMapper.selectTotalOrderAmount().getTotalAmount();
//        Double monthMoney = ordersMapper.selectOrderAmountForLastMonth(Date.valueOf(lastMonthFirstDay)).getTotalAmount();

        ShowEntity showEntity = new ShowEntity(1111L,312232L,133434L,234334.55,31232.55);
        return Result.success(showEntity);




    }
}
