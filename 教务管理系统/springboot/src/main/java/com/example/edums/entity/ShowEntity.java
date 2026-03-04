package com.example.edums.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "首页信息展示实体")

@NoArgsConstructor
@AllArgsConstructor


//cardInfoData: [
//        { title: '货品数量', icon: 'vue-dsn-icon-dianji', count: 682, color: '#2d8cf0' },
//        { title: '用户总数', icon: 'vue-dsn-icon-xinzeng', count: 259, color: '#19be6b' },
//        { title: '累计订单', icon: 'vue-dsn-icon-xinfeng', count: 1262, color: '#ff9900' },
//        { title: '累计金额', icon: 'vue-dsn-icon-dianzan', count: 508, color: '#e46cbb' },
//        { title: '本月成交', icon: 'vue-dsn-icon-heart', count: 379, color: '#9a66e4' }
//        ],
public class ShowEntity {
    @Schema(description = "货品数量")
    private Long goodsCount;
    @Schema(description = "用户总数")
    private Long userCount;
    @Schema(description = "累计订单")
    private Long orderCount;
    @Schema(description = "累计金额")
    private Double moneyCount;
    @Schema(description = "本月成交")
    private Double monthMoney;

}
