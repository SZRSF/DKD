package com.dkd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dkd.entity.PolicyEntity;
import com.dkd.vo.Pager;
import com.dkd.vo.VmVO;

import java.util.List;

public interface PolicyService extends IService<PolicyEntity> {




    /**
     * 搜索
     * @param policyName
     * @return
     */
    Pager<PolicyEntity> search(String policyName, long pageIndex, long pageSize);

    /**
     * 删除策略
     * @param policyId
     * @return
     */
    Boolean delete(Integer policyId);


    /**
     * 此策略的售货机列表
     * @param policyId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Pager<VmVO> vmList(Integer policyId, long pageIndex, long pageSize);


}
