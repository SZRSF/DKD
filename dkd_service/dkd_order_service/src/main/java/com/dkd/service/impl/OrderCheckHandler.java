package com.dkd.service.impl;

import cn.elegent.ac.ACHandler;
import cn.elegent.ac.annotation.Topic;
import cn.elegent.pay.ElegentPay;
import com.dkd.config.TopicConfig;
import com.dkd.constant.OrderStatus;
import com.dkd.dto.OrderCheckDTO;
import com.dkd.entity.OrderEntity;
import com.dkd.service.OrderService;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 订单超时处理
 * @author zengzhicheng
 */
@Topic(TopicConfig.ORDER_CHECK_TOPIC)
@Slf4j
public class OrderCheckHandler implements ACHandler<OrderCheckDTO> {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ElegentPay elegentPay;

    @Override
    public void process(String s, OrderCheckDTO orderCheck) throws Exception {

        if(orderCheck == null || Strings.isNullOrEmpty(orderCheck.getOrderNo())) {
            return;
        }
        //查询订单
        OrderEntity orderEntity = orderService.getByOrderNo(orderCheck.getOrderNo());
        if(orderEntity == null) {
            return;
        }
        //如果是未支付
        if(orderEntity.getStatus().equals(OrderStatus.ORDER_STATUS_CREATE)){
            log.info("订单无效处理 订单号：{}",orderCheck.getOrderNo());
            //无效状态
            orderEntity.setStatus(OrderStatus.ORDER_STATUS_INVALID);
            orderService.updateById(orderEntity);
            //关闭支付
            elegentPay.closePay( orderEntity.getOrderNo(),orderEntity.getPayType() );
        }
    }
}