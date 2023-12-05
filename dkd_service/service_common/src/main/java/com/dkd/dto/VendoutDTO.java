package com.dkd.dto;

import lombok.Data;


/**
 * 数据传输类
 *
 * @author zengzhicheng
 */
@Data
public class VendoutDTO extends BaseDTO{

    /**
     * 商品Id
     */
    private long skuId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 货道编号
     */
    private String channelCode;

}