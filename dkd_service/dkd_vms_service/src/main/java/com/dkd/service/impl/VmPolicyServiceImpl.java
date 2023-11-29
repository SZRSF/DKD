package com.dkd.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkd.dao.VmPolicyDao;
import com.dkd.entity.PolicyEntity;
import com.dkd.entity.VmPolicyEntity;
import com.dkd.exception.LogicException;
import com.dkd.http.vo.PolicyReq;
import com.dkd.service.PolicyService;
import com.dkd.service.VendingMachineService;
import com.dkd.service.VmPolicyService;
import com.dkd.vo.VmVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class VmPolicyServiceImpl extends ServiceImpl<VmPolicyDao,VmPolicyEntity> implements VmPolicyService{

    @Autowired
    private VendingMachineService vendingMachineService;

    @Autowired
    private PolicyService policyService;

    @Override
    public VmPolicyEntity getPolicyByInnerCode(String innerCode) {
        QueryWrapper<VmPolicyEntity> qw = new QueryWrapper<>();
        qw.lambda()
                .eq(VmPolicyEntity::getInnerCode,innerCode);

        return getOne(qw);
    }


    /**
     * 将策略应用到售货机
     *
     * @param policyReq
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean applyPolicy(PolicyReq policyReq) {
        // 删除原策略
        QueryWrapper<VmPolicyEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(VmPolicyEntity::getInnerCode, policyReq.getInnerCodeList());
        remove(wrapper);

        List<VmPolicyEntity> vmPolicyEntityList = policyReq.getInnerCodeList().stream().map(code -> {
            // 查询售货机和策略
            VmVO vm = vendingMachineService.findByInnerCode(code);
            if (vm == null) {
                throw new LogicException("没有此售货机");
            }
            PolicyEntity policyEntity = policyService.getById(policyReq.getPolicyId());
            if (policyEntity == null) {
                throw new LogicException("没有此策略");
            }

            VmPolicyEntity vmPolicyEntity = new VmPolicyEntity();
            // 更新售货机策略配置
            vmPolicyEntity.setDiscount(policyEntity.getDiscount());
            vmPolicyEntity.setInnerCode(code);
            vmPolicyEntity.setPolicyName(policyEntity.getPolicyName());
            vmPolicyEntity.setVmId(vm.getVmId());
            vmPolicyEntity.setPolicyId(policyEntity.getPolicyId());
            return vmPolicyEntity;
        }).collect(Collectors.toList());

        return saveBatch(vmPolicyEntityList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelPolicy(String innerCode, int policyId) {
        UpdateWrapper<VmPolicyEntity> uw = new UpdateWrapper<>();
        uw.lambda()
                .eq(VmPolicyEntity::getInnerCode,innerCode)
                .eq(VmPolicyEntity::getPolicyId,policyId);
        return remove(uw);
    }


}
