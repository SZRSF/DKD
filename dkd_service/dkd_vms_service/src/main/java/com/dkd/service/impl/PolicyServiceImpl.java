package com.dkd.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkd.http.vo.PolicyReq;
import com.google.common.base.Strings;
import com.dkd.dao.PolicyDao;
import com.dkd.entity.PolicyEntity;
import com.dkd.entity.VendingMachineEntity;
import com.dkd.entity.VmPolicyEntity;
import com.dkd.exception.LogicException;
import com.dkd.service.*;
import com.dkd.vo.Pager;
import com.dkd.vo.VmVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PolicyServiceImpl extends ServiceImpl<PolicyDao,PolicyEntity> implements PolicyService{
    @Autowired
    private VendingMachineService vmService;

    @Autowired
    private VmPolicyService vmPolicyService;

    @Autowired
    private ChannelService channelService;





    @Override
    public Pager<PolicyEntity> search(String policyName, long pageIndex, long pageSize) {
        LambdaQueryWrapper<PolicyEntity> qw = new LambdaQueryWrapper<>();
        if(!Strings.isNullOrEmpty(policyName))
            qw.like(PolicyEntity::getPolicyName,policyName);

        com.baomidou.mybatisplus.extension.plugins.pagination.Page<PolicyEntity> page =
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageIndex,pageSize);

        this.page(page,qw);

        return Pager.build(page);
    }

    @Override
    public Boolean delete(Integer policyId) {
        var qw = new LambdaQueryWrapper<VmPolicyEntity>();
        qw.eq(VmPolicyEntity::getPolicyId,policyId);
        int count = vmPolicyService.count(qw);
        if(count > 0){
            throw new LogicException("该策略正在使用");
        }
        return this.removeById(policyId);
    }

    @Autowired
    private VendingMachineService vendingMachineService;

    @Override
    public Pager<VmVO> vmList(Integer policyId, long pageIndex, long pageSize) {
        LambdaQueryWrapper<VmPolicyEntity> qw1 = new LambdaQueryWrapper<>();
        qw1.eq( VmPolicyEntity::getPolicyId,policyId );
        List<VmPolicyEntity> vmPolicyEntityList = vmPolicyService.list(qw1);
        if(vmPolicyEntityList.size()==0){
            return Pager.buildEmpty();
        }
        //获得售货机编号列表
        List<String> innerCodeList = vmPolicyEntityList.stream().map(vmPolicyEntity -> vmPolicyEntity.getInnerCode()).collect(Collectors.toList());

        com.baomidou.mybatisplus.extension.plugins.pagination.Page<VendingMachineEntity> page =
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageIndex,pageSize);
        LambdaQueryWrapper<VendingMachineEntity> qw = new LambdaQueryWrapper<>();

        qw.in(VendingMachineEntity::getInnerCode , innerCodeList  );  //售货机列表

        vendingMachineService.page(page,qw);
        Pager<VendingMachineEntity> vendingMachineEntityPager = Pager.build(page);

        //转换
        List<VmVO> vmVOList = vendingMachineEntityPager.getCurrentPageRecords().stream().map(vendingMachineEntity -> {
            VmVO vmVO = new VmVO();
            //vmVO.setNodeName(vendingMachineEntity.getNode().getName());
            vmVO.setInnerCode(vendingMachineEntity.getInnerCode());
            return vmVO;
        }).collect(Collectors.toList());

        Pager<VmVO> vmVOPager=new Pager<>();
        vmVOPager.setCurrentPageRecords( vmVOList );
        vmVOPager.setTotalCount( vendingMachineEntityPager.getTotalCount() );
        vmVOPager.setPageIndex( vendingMachineEntityPager.getPageIndex() );
        vmVOPager.setPageSize( vendingMachineEntityPager.getPageSize());
        vmVOPager.setTotalPage( vendingMachineEntityPager.getTotalPage());
        return vmVOPager;
    }


}
