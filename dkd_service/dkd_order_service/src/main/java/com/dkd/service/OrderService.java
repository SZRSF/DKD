package com.dkd.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dkd.entity.OrderEntity;

public interface OrderService extends IService<OrderEntity> {


    /**
     * 通过订单编号获取订单实体
     * @param orderNo
     * @return
     */
    OrderEntity getByOrderNo(String orderNo);


}
