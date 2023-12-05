package com.dkd.dto;

import lombok.Data;

/**
 * @author zengzhicheng
 */
@Data
public class VendoutRunningDTO {

    /**
     * 订单编号
     */
    private String orderNo;


    /**
     * 售货机编号
     */
    private String innerCode;


    /**
     * 货道编号
     */
    private String channelCode;

    /**
     * 状态
     */
    private String status;

}
