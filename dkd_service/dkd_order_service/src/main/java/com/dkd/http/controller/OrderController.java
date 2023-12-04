package com.dkd.http.controller;

import cn.elegent.pay.ElegentPay;
import cn.elegent.pay.dto.PayRequest;
import cn.elegent.pay.dto.PayResponse;
import com.dkd.entity.OrderEntity;
import com.dkd.service.OrderService;
import com.dkd.vo.PayVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author zengzhicheng
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ElegentPay elegentPay;


    /**
     * 支付
     *
     * @param payVO
     * @return
     */
    @PostMapping("/requestPay/{tradeType}/{platform}")
    public PayResponse requestPay(@RequestBody PayVO payVO, @PathVariable("tradeType") String tradeType, @PathVariable("platform") String platform) {
        //创建订单
        OrderEntity orderEntity = orderService.createOrder(payVO, platform);
        //封装支付请求对象调用支付
        PayRequest param = new PayRequest();
        //商品名称
        param.setBody(orderEntity.getSkuName());
        //订单编号
        param.setOrderSn(orderEntity.getOrderNo());
        //订单金额
        param.setTotalFee(orderEntity.getAmount());
        //openid
        param.setOpenid(payVO.getOpenId());
        return elegentPay.requestPay(param, tradeType, platform);
    }
}