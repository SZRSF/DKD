package com.dkd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dkd.entity.VendingMachineEntity;

import com.dkd.http.vo.CreateVMReq;
import com.dkd.vo.Pager;
import com.dkd.vo.SkuVO;
import com.dkd.vo.VmVO;

import java.util.List;

public interface VendingMachineService extends IService<VendingMachineEntity> {


    /**
     * 新增
     *
     * @param createVMReq
     * @return
     */
    boolean add(CreateVMReq createVMReq);

    /**
     * 修改售货机点位
     * @param id
     * @param nodeId
     * @return
     */
    boolean update(Long id,Long nodeId);

    /**
     * 根据机器状态获取机器编号列表
     * @param isRunning
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Pager<String> getAllInnerCodes(boolean isRunning, long pageIndex, long pageSize);

    /**
     * 根据状态获取售货机列表
     * @param status
     * @return
     */
    Pager<VendingMachineEntity> query(Long pageIndex, Long pageSize, Integer status, String innerCode);




    /**
     * 根据售货机编号查找
     * @param innerCode
     * @return
     */
    VmVO findByInnerCode(String innerCode);



    /**
     * 根据售货机编号查询商品列表
     * @param innerCode
     * @return
     */
    List<SkuVO> getSkuListByInnerCode(String innerCode);


    /**
     * 商品是否还有余量
     * @param skuId
     * @return
     */
    Boolean hasCapacity(String innerCode,Long skuId);


    /**
     * 查询售货机商品当前库存数量
     * @param skuId
     * @return
     */
    int countCapacity(String innerCode,Long skuId);

    /**
     * 查询售货机商品最大库存数量
     * @param skuId
     * @return
     */
    int countMaxCapacity(String innerCode,Long skuId);


    /**
     * 查询售货机商品库存率
     * @param skuId
     * @return
     */
    int countCapacityRatio(String innerCode,Long skuId);

    /**
     * 根据clientId 查询售货机
     * @param clientId
     * @return
     */
    VendingMachineEntity findByClientId(String clientId);


    /**
     * 加载全部关联数据
     * @param vendingMachineEntityList
     */
    void readAll(List<VendingMachineEntity> vendingMachineEntityList );


}
