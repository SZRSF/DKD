package com.dkd.constant;

/**
 * 支付状态
 */
public class PayStatus {


    /**
     * 支付状态：未支付
     */
    public static final Integer PAY_STATUS_NOPAY = 0;
    /**
     * 支付状态：支付完成
     */
    public static final Integer PAY_STATUS_PAYED = 1;

    /**
     * 支付状态：退款中
     */
    public static final Integer PAY_STATUS_REFUNDING = 2;

    /**
     * 支付状态：退款完成
     */
    public static final Integer PAY_STATUS_REFUNDIED = 3;



}
