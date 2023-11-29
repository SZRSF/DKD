package com.dkd.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 货道vo
 */
@Data
public class ChannelVO {


    private String channelCode;//货道编号

    private Long skuId;//商品Id
    /**
     * 货道商品最终售价
     */
    private Integer price;

    private Long vmId;//售货机Id

    private String innerCode;//售货机软编号

    private Integer maxCapacity;//货道最大容量

    private Integer currentCapacity;//货道当前容量

    private LocalDateTime lastSupplyTime;//上次补货时间

    private String  skuName; //商品名称


}
