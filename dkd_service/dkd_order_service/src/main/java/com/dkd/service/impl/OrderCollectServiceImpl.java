package com.dkd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkd.dao.OrderCollectDao;
import com.dkd.entity.OrderCollectEntity;
import com.dkd.service.OrderCollectService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrderCollectServiceImpl extends ServiceImpl<OrderCollectDao,OrderCollectEntity> implements OrderCollectService{
    @Override
    public List<OrderCollectEntity> getOwnerCollectByDate(Integer ownerId,LocalDate start,LocalDate end){
        QueryWrapper<OrderCollectEntity> qw = new QueryWrapper<>();
        qw.lambda()
                .eq(OrderCollectEntity::getOwnerId,ownerId)
                .ge(OrderCollectEntity::getOrderDate,start)
                .le(OrderCollectEntity::getOrderDate,end)
                .groupBy(OrderCollectEntity::getNodeName,OrderCollectEntity::getOrderDate)
                .orderByDesc(OrderCollectEntity::getOrderDate);

        return this.list(qw);
    }



}
