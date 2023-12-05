package com.dkd.service.impl;

import cn.elegent.ac.ACHandler;
import cn.elegent.ac.annotation.Topic;
import com.dkd.config.TopicConfig;
import com.dkd.constant.VMRuningStatus;
import com.dkd.dto.VendoutResultDTO;
import com.dkd.entity.ChannelEntity;
import com.dkd.service.ChannelService;
import com.dkd.service.VendoutRunningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 出货结果处理类（售货机微服务）
 *
 * @author zengzhicheng
 */
@Topic(TopicConfig.VMS_RESULT_TOPIC)
@Slf4j
public class VendoutResultHandler implements ACHandler<VendoutResultDTO> {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private VendoutRunningService vendoutRunningService;

    @Override
    public void process(String s, VendoutResultDTO vendoutResultDTO) throws Exception {
        log.info("接收到出货结果,{}", vendoutResultDTO);
        //出货成功扣减库存
        if(vendoutResultDTO.isSuccess()){
            ChannelEntity channelEntity = channelService.getChannelInfo(vendoutResultDTO.getInnerCode(), vendoutResultDTO.getChannelCode());
            channelEntity.setCurrentCapacity( channelEntity.getCurrentCapacity()-1 );
            channelService.updateById( channelEntity);
            //发货成功
            vendoutRunningService.updateStatus(vendoutResultDTO.getOrderNo(), VMRuningStatus.VENDOUT_COMP);

        }else{
            //发货失败
            vendoutRunningService.updateStatus(vendoutResultDTO.getOrderNo(), VMRuningStatus.VENDOUT_FAIL);
        }
    }
}