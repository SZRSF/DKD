package com.dkd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dkd.entity.SkuClassEntity;
import com.dkd.entity.SkuEntity;
import com.dkd.exception.LogicException;
import com.dkd.vo.Pager;
import com.dkd.vo.SkuVO;

import java.util.List;

public interface SkuService extends IService<SkuEntity> {
    /**
     * 修改
     * @param skuEntity
     * @return
     */
    boolean update(SkuEntity skuEntity) throws LogicException;

    /**
     * 删除
     * @param id
     * @return
     */
    boolean delete(Long id);

    /**
     * 分页查询
     * @param pageIndex
     * @param pageSize
     * @param skuName
     * @return
     */
    Pager<SkuEntity> findPage(long pageIndex, long pageSize, Integer classId, String skuName);

    /**
     * 获取所有商品类别
     * @return
     */
    List<SkuClassEntity> getAllClass();


    /**
     * 根据商品id返回商品列表
     * @param skuIds
     * @return
     */
    List<SkuEntity> getSkuList( List<Long> skuIds );


    /**
     * 根据售货机查询商品
     *
     * @param innerCode
     * @return
     */
    List<SkuVO> findSkuByInnerCode(String innerCode );


}
