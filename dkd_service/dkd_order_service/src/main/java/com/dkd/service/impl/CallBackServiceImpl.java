package com.dkd.service.impl;


import cn.elegent.ac.ElegentAC;
import cn.elegent.pay.CallBackService;
import cn.elegent.pay.ElegentPay;
import com.dkd.config.TopicConfig;
import com.dkd.constant.OrderStatus;
import com.dkd.constant.PayStatus;
import com.dkd.constant.VMRuningStatus;
import com.dkd.dto.VendoutDTO;
import com.dkd.dto.VendoutRunningDTO;
import com.dkd.entity.OrderEntity;
import com.dkd.feign.VMService;
import com.dkd.service.OrderService;
import com.dkd.vo.ChannelVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author zengzhicheng
 */
@Component
@Slf4j
public class CallBackServiceImpl implements CallBackService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private VMService vmService;

    @Autowired
    private ElegentAC elegentAC;

    @Override
    public void successPay(String orderSn) {
        log.info("支付成功回调{}", orderSn);

        OrderEntity orderEntity = orderService.getByOrderNo(orderSn);
        if (orderEntity != null) {
            if (orderEntity.getStatus().equals(OrderStatus.ORDER_STATUS_CREATE)) {
                //订单状态  已支付
                orderEntity.setStatus(OrderStatus.ORDER_STATUS_PAYED);
                //支付状态  成功
                orderEntity.setPayStatus(PayStatus.PAY_STATUS_PAYED);

                // 得到出货货道
                ChannelVO channel = vmService.getChannel(orderEntity.getInnerCode(), orderEntity.getSkuId());
                // 货道编号
                orderEntity.setChannelCode(channel.getChannelCode());
                orderService.updateById(orderEntity);

                //添加服务端运行日志
                VendoutRunningDTO vendoutRunning = new VendoutRunningDTO();
                BeanUtils.copyProperties(orderEntity, vendoutRunning);
                //状态:准备发货
                vendoutRunning.setStatus(VMRuningStatus.VENDOUT_PREP);
                vmService.addVendoutRunning(vendoutRunning);

                //构建报文并发送
                //报文封装对象  （数据传输对象）
                VendoutDTO vendoutDTO = new VendoutDTO();
                BeanUtils.copyProperties(orderEntity, vendoutDTO);
                elegentAC.publish(TopicConfig.getVendoutTopic(orderEntity.getInnerCode()), vendoutDTO);
            }
        }
    }

    @Override
    public void failPay(String orderSn) {
        log.info("支付失败回调{}", orderSn);
    }

    /**
     * 修改订单状态为退款完成
     *
     * @param orderSn
     */
    @Override
    public void successRefund(String orderSn) {
        log.info("退款成功回调{}", orderSn);
        OrderEntity orderEntity = orderService.getByOrderNo(orderSn);
        if (orderEntity != null) {
            //将状态设置退款成功
            orderEntity.setPayStatus(PayStatus.PAY_STATUS_REFUNDIED);
            orderService.updateById(orderEntity);
        }
    }

    /**
     * 退款失败打日志记录--修改状态为已支付
     *
     * @param orderSn
     */
    @Override
    public void failRefund(String orderSn) {
        log.info("退款失败回调{}", orderSn);
        OrderEntity orderEntity = orderService.getByOrderNo(orderSn);
        if (orderEntity != null) {
            //将状态再改回支付成功
            orderEntity.setPayStatus(PayStatus.PAY_STATUS_PAYED);
            orderService.updateById(orderEntity);
        }
    }
}