package com.dkd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dkd.entity.OrderCollectEntity;

import java.time.LocalDate;
import java.util.List;

public interface OrderCollectService extends IService<OrderCollectEntity> {


    /**
     *获取一定时间范围内的合作商的点位分成数据
     * @param start
     * @param end
     * @return
     */
    List<OrderCollectEntity> getOwnerCollectByDate(Integer ownerId, LocalDate start, LocalDate end);



}
