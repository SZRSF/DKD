package com.dkd.service.impl;


import cn.elegent.pay.CallBackService;
import com.dkd.constant.OrderStatus;
import com.dkd.constant.PayStatus;
import com.dkd.entity.OrderEntity;
import com.dkd.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class CallBackServiceImpl implements CallBackService {

    @Autowired
    private OrderService orderService;

    @Override
    public void successPay(String orderSn) {
        log.info("支付成功回调{}",orderSn);

        OrderEntity orderEntity = orderService.getByOrderNo(orderSn);
        if(orderEntity!=null){
            if(orderEntity.getStatus().equals( OrderStatus.ORDER_STATUS_CREATE )){
                //订单状态  已支付
                orderEntity.setStatus(OrderStatus.ORDER_STATUS_PAYED);
                //支付状态  成功
                orderEntity.setPayStatus(PayStatus.PAY_STATUS_PAYED) ;
                orderService.updateById( orderEntity );
                //todo: 发货
            }
        }
    }

    @Override
    public void failPay(String orderSn) {
        log.info("支付失败回调{}",orderSn);
    }

    @Override
    public void successRefund(String orderSn) {

    }

    @Override
    public void failRefund(String orderSn) {

    }
}