package com.dkd.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkd.constant.OrderStatus;
import com.dkd.constant.PayStatus;
import com.dkd.dao.OrderDao;
import com.dkd.feign.UserService;
import com.dkd.vo.*;
import com.dkd.entity.OrderEntity;
import com.dkd.exception.LogicException;
import com.dkd.feign.VMService;
import com.dkd.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Autowired
    private VMService vmService;

    @Autowired
    private UserService userService;

    @Override
    public OrderEntity getByOrderNo(String orderNo) {
        QueryWrapper<OrderEntity> qw = new QueryWrapper<>();
        qw.lambda()
                .eq(OrderEntity::getOrderNo,orderNo);
        return this.getOne(qw);
    }




}
