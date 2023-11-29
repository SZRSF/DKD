package com.dkd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dkd.entity.VendoutRunningEntity;
import com.dkd.vo.Pager;

import java.util.List;
import java.util.Map;

public interface VendoutRunningService extends IService<VendoutRunningEntity> {
    /**
     * 条件查询
     * @param searchMap
     * @return
     */
    List<VendoutRunningEntity> findList(Map searchMap);

    /**
     * 分页查询
     * @param pageIndex
     * @param pageSize
     * @param searchMap
     * @return
     */
    Pager<VendoutRunningEntity> findPage(long pageIndex, long pageSize, Map searchMap);


    /**
     * 修改订单状态
     * @param orderSn
     * @param status
     */
    void updateStatus(String orderSn,String status);
}
