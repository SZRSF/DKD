package com.dkd.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dkd.entity.OrderEntity;
import com.dkd.vo.PayVO;

public interface OrderService extends IService<OrderEntity> {


    /**
     * 通过订单编号获取订单实体
     * @param orderNo
     * @return
     */
    OrderEntity getByOrderNo(String orderNo);

    /**
     * 微信小程序支付创建订单
     *
     * @param payVO
     * @return
     */
    OrderEntity createOrder(PayVO payVO, String platform);


}
