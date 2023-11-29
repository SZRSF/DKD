package com.dkd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dkd.entity.NodeEntity;
import com.dkd.entity.VendingMachineEntity;
import com.dkd.vo.Pager;

import java.util.List;

public interface NodeService extends IService<NodeEntity> {


    /**
     * 搜索点位
     * @param name
     * @param regionId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Pager<NodeEntity> search(String name, String regionId, long pageIndex, long pageSize);


    /**
     * 获取点位下所有售货机
     * @param id
     * @return
     */
    List<VendingMachineEntity> getVmList(long id);



    /**
     * 读取全部数据
     * @param nodeEntities
     */
    void readAll(List<NodeEntity> nodeEntities );



}
