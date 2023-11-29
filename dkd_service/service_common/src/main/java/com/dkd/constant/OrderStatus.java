package com.dkd.constant;

/**
 * 订单状态
 */
public class OrderStatus {

    /**
     * 订单状态：创建
     */
    public static final Integer ORDER_STATUS_CREATE = 0;

    /**
     * 订单状态：支付完成
     */
    public static final  Integer ORDER_STATUS_PAYED = 1;

    /**
     * 订单状态：出货成功
     */
    public static final  Integer ORDER_STATUS_VENDOUT_SUCCESS = 2;
    /**
     * 订单状态：出货失败
     */
    public static final  Integer ORDER_STATUS_VENDOUT_FAIL = 3;

    /**
     * 订单状态：失效
     */
    public static final  Integer ORDER_STATUS_INVALID = 4;


}
