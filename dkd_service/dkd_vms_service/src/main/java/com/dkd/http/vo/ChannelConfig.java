package com.dkd.http.vo;

import lombok.Data;

import java.util.List;

/**
 * 售货机货道配置
 *
 * @author zengzhicheng
 */
@Data
public class ChannelConfig {
    /**
     * 售货机编号
     */
    private String innerCode;
    /**
     * 货道列表
     */
    private List<ChannelVo> channelList;
}
