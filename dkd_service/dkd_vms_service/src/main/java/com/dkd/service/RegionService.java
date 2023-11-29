package com.dkd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dkd.entity.RegionEntity;
import com.dkd.vo.Pager;

public interface RegionService extends IService<RegionEntity> {




    /**
     * 获取区域详情
     * @param regionId
     * @return
     */
    RegionEntity findById(Long regionId);



    /**
     * 搜索
     * @param pageIndex
     * @param pageSize
     * @param name
     * @return
     */
    Pager<RegionEntity> search(Long pageIndex, Long pageSize, String name);




}
