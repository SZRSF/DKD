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

}