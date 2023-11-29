package com.dkd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dkd.entity.ChannelEntity;

import com.dkd.http.vo.ChannelConfig;
import com.dkd.vo.ChannelVO;
import com.dkd.vo.Pager;

import java.util.List;
import java.util.Map;

public interface ChannelService extends IService<ChannelEntity> {
    /**
     * 条件查询
     * @param searchMap
     * @return
     */
    List<ChannelEntity> findList(Map searchMap);

    /**
     * 分页查询
     * @param pageIndex
     * @param pageSize
     * @param searchMap
     * @return
     */
    Pager<ChannelEntity> findPage(long pageIndex, long pageSize, Map searchMap);

    /**
     * 根据设备编号获取货道
     * @param innerCode
     * @return
     */
    List<ChannelEntity> getChannelesByInnerCode(String innerCode);


    /**
     * 根据设备编号获取货道
     *
     * @param innerCode
     * @return
     */
    ChannelVO getChanneleByInnerCodeAndSku(String innerCode, Long skuId);


    /**
     * 获取货道信息
     * @param innerCode
     * @param channelCode
     * @return
     */
    ChannelEntity getChannelInfo(String innerCode,String channelCode);

    /**
     * 货道设置
     *
     * @param channelConfig
     * @return
     */
    Boolean channelSet(ChannelConfig channelConfig);

}
