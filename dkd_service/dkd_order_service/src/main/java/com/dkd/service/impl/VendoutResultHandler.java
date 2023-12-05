package com.dkd.service.impl;

import cn.elegent.ac.ACHandler;
import cn.elegent.ac.annotation.Topic;
import cn.elegent.pay.ElegentPay;
import cn.elegent.pay.dto.RefundRequest;
import com.dkd.config.TopicConfig;
import com.dkd.constant.OrderStatus;
import com.dkd.constant.PayStatus;
import com.dkd.dto.VendoutResultDTO;
import com.dkd.entity.OrderEntity;
import com.dkd.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;



/**
 * 出货结果处理类
 * @author zengzhicheng
 */
@Topic(TopicConfig.VMS_RESULT_TOPIC)
@Slf4j
public class VendoutResultHandler implements ACHandler<VendoutResultDTO> {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ElegentPay elegentPay;

    @Override
    public void process(String s, VendoutResultDTO vendoutResultDTO) throws Exception {
        log.info("接收到出货结果,{}",vendoutResultDTO);

        OrderEntity orderEntity = orderService.getByOrderNo(vendoutResultDTO.getOrderNo());
        if(orderEntity==null) {
            return;
        }

        //如果成功
        if(vendoutResultDTO.isSuccess()){
            //出货成功
            orderEntity.setStatus(OrderStatus.ORDER_STATUS_VENDOUT_SUCCESS);
            orderService.updateById( orderEntity );
        }else{
            //修改订单状态为失败
            orderEntity.setStatus( OrderStatus.ORDER_STATUS_VENDOUT_FAIL );
            //退款中
            orderEntity.setPayStatus(PayStatus.PAY_STATUS_REFUNDING);
            orderService.updateById( orderEntity );

            RefundRequest refundRequest=new RefundRequest();
            //订单号
            refundRequest.setOrderSn( vendoutResultDTO.getOrderNo() );
            //退款号
            refundRequest.setRequestNo(vendoutResultDTO.getOrderNo());
            //金额
            refundRequest.setTotalFee( orderEntity.getAmount() );
            //退款金额
            refundRequest.setRefundAmount( orderEntity.getAmount());
            //发起退款
            elegentPay.refund(refundRequest, orderEntity.getPayType());
        }

    }
}