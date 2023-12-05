package com.dkd.service.impl;

import cn.elegent.ac.ACHandler;
import cn.elegent.ac.annotation.Topic;
import com.dkd.config.TopicConfig;
import com.dkd.dto.VendoutResultDTO;
import com.dkd.entity.ChannelEntity;
import com.dkd.service.ChannelService;
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

    @Override
    public void process(String s, VendoutResultDTO vendoutResultDTO) throws Exception {
        log.info("接收到出货结果,{}", vendoutResultDTO);
        //扣减库存
        if(vendoutResultDTO.isSuccess()){
            //扣减库存
            ChannelEntity channelInfo = channelService.getChannelInfo(vendoutResultDTO.getInnerCode(), vendoutResultDTO.getChannelCode());
            channelInfo.setCurrentCapacity( channelInfo.getCurrentCapacity()-1 );
            channelService.updateById(channelInfo );
        }
    }
}