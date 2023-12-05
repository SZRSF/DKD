package com.dkd.service.impl;

import cn.elegent.ac.ElegentAC;
import cn.elegent.lock.ElegentLock;
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

    @Autowired
    private ElegentLock elegentLock;

    @Override
    public OrderEntity getByOrderNo(String orderNo) {
        QueryWrapper<OrderEntity> qw = new QueryWrapper<>();
        qw.lambda().eq(OrderEntity::getOrderNo, orderNo);
        return this.getOne(qw);
    }


    @Override
    public OrderEntity createOrder(PayVO payVO, String platform) {
        //判断库存
        if (!vmService.hasCapacity(payVO.getInnerCode(), Long.valueOf(payVO.getSkuId()))) {
            throw new LogicException("商品库存不足");
        }

        // 加锁，判断上次交易是否完成
        boolean lock = elegentLock.lock(payVO.getInnerCode() + "-" + payVO.getSkuId(), 60, false);
        if (!lock) {
            throw new LogicException("上一笔交易未完成，请稍后！");
        }

        //创建订单对象
        OrderEntity orderEntity = new OrderEntity();
        //订单号：售货机编号+纳秒
        orderEntity.setOrderNo(payVO.getInnerCode() + System.nanoTime());
        //未支付
        orderEntity.setStatus(OrderStatus.ORDER_STATUS_CREATE);
        //未支付
        orderEntity.setPayStatus(PayStatus.PAY_STATUS_NOPAY);
        //平台 wxpay  alipay
        orderEntity.setPayType(platform);
        orderEntity.setOpenId(payVO.getOpenId());
        orderEntity.setInnerCode(payVO.getInnerCode());

        //查询售货机
        VmVO vmVO = vmService.getVMInfo(payVO.getInnerCode());
        BeanUtils.copyProperties(vmVO, orderEntity);
        //地址
        orderEntity.setAddr(vmVO.getNodeAddr());

        //查询商品
        SkuVO skuVO = vmService.getSku(payVO.getSkuId());
        BeanUtils.copyProperties(skuVO, orderEntity);

        //计算金额
        PolicyVO policy = vmService.getPolicy(payVO.getInnerCode());
        if (policy != null) {
            //折扣比例
            BigDecimal discount = new BigDecimal(policy.getDiscount());
            //原价
            BigDecimal price = new BigDecimal(skuVO.getPrice());
            //计算真实价格
            int realPrice = price.multiply(discount).divide(new BigDecimal(100), 0, RoundingMode.HALF_UP).intValue();
            orderEntity.setAmount(realPrice);
        } else {
            orderEntity.setAmount(skuVO.getPrice());
        }

        //计算合作商分成

        PartnerVO partnerVO = userService.getPartner(orderEntity.getOwnerId());
        if (partnerVO != null) {
            if (orderEntity.getAmount() < 10) {
                //分成
                orderEntity.setBill(0);
            } else {
                //金额
                BigDecimal amount = new BigDecimal(orderEntity.getAmount());
                //分成比例
                BigDecimal ratio = new BigDecimal(partnerVO.getRatio());
                //分成金额
                int bill = amount.multiply(ratio).divide(new BigDecimal(100), 0, RoundingMode.HALF_UP).intValue();
                orderEntity.setBill(bill);
            }
        }

        //保存
        save(orderEntity);
        return orderEntity;
    }


}
