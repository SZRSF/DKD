package com.dkd.pipe;

import cn.elegent.pipe.client.annotation.ElegentPipe;
import cn.elegent.pipe.client.core.PipeService;
import cn.elegent.pipe.common.TransmitDTO;
import com.dkd.service.impl.OrderEsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Map;


/**
 * 接受订单数据同步
 */
@ElegentPipe(db="dkd_order",table = "tb_order")
@Slf4j
public class OrderPipe implements PipeService {

    @Autowired
    private OrderEsService orderEsService;

    @Override
    public void insertHandler(TransmitDTO transmitDTO) {
        Map<String, Serializable> orderMap = transmitDTO.getAfter();
        orderEsService.save("order", (Long)orderMap.get("id")+"",orderMap );
        log.info("ES插入订单数据{}",orderMap);
    }

    @Override
    public void updateHandler(TransmitDTO transmitDTO) {
        Map<String, Serializable> orderMap = transmitDTO.getAfter();
        orderEsService.update("order",(Long)orderMap.get("id")+"",orderMap );
        log.info("ES修改订单数据{}",orderMap);
    }

    @Override
    public void deleteHandler(TransmitDTO transmitDTO) {
        Map<String, Serializable> orderMap = transmitDTO.getBefore();
        orderEsService.delete("order",(Long)orderMap.get("id")+"");
        log.info("ES删除订单数据{}",orderMap);
    }
}