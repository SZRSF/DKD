package com.dkd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dkd.entity.VmPolicyEntity;
import com.dkd.http.vo.PolicyReq;

public interface VmPolicyService extends IService<VmPolicyEntity> {



    /**
     * 获取售货机的策略
     * @param innerCode
     * @return
     */
    VmPolicyEntity getPolicyByInnerCode(String innerCode);

    /**
     * 给售货机应用策略
     *
     * @param policyReq
     * @return
     */
    boolean applyPolicy(PolicyReq policyReq);

    /**
     * 取消策略
     *
     * @param innerCode
     * @param policyId
     * @return
     */
    boolean cancelPolicy(String innerCode, int policyId);

}
