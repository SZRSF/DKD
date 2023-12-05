package com.dkd.config;



/**
 * 消息队列中的主题配置
 *
 * @author zengzhicheng
 */
public class TopicConfig {

    /**
     * 发送到售货机终端主题
     *
     * @param innerCode
     * @return
     */
    public static String getVendoutTopic(String innerCode){
        return "vm/"+innerCode+"/vendout";
    }

    /**
     * 出货结果主题（终端->服务端）
     */
    public final static String VMS_RESULT_TOPIC = "server/vms/result";

    /**
     * 延迟订单主题
     */
    public final static String ORDER_CHECK_TOPIC = "server/order/check";

}