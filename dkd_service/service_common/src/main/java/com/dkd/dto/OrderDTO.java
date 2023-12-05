package com.dkd.dto;

import lombok.Data;

@Data
public class OrderDTO {

    private Long id;//id

    private String orderNo;//订单编号

    private String innerCode;//机器编号

    private String addr;//点位地址

    private Long skuId;//商品id

    private String skuName;//商品名称

    private Integer status;//订单状态:0-创建;1-支付完成;2-出货成功;3-出货失败;

    private Integer amount;//支付金额

    private Integer bill;//分账金额

    private Integer price;//商品金额

}