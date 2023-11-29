package com.dkd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.dkd.dao.VmTypeDao;
import com.dkd.entity.VendingMachineEntity;
import com.dkd.entity.VmTypeEntity;
import com.dkd.exception.LogicException;
import com.dkd.service.VendingMachineService;
import com.dkd.service.VmTypeService;
import com.dkd.vo.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VmTypeServiceImpl extends ServiceImpl<VmTypeDao,VmTypeEntity> implements VmTypeService{
    @Autowired
    private VendingMachineService vmService;

    @Override
    public Boolean delete(Integer id) {
        LambdaQueryWrapper<VendingMachineEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(VendingMachineEntity::getVmType,id);
        if(vmService.count(qw) > 0){
            throw new LogicException("该售货机类型在使用");
        }

        return this.removeById(id);
    }

    @Override
    public Pager<VmTypeEntity> search(long pageIndex,long pageSize,String name) {
        var page = new Page<VmTypeEntity>(pageIndex,pageSize);
        var qw = new LambdaQueryWrapper<VmTypeEntity>();
        if(!Strings.isNullOrEmpty(name)){
            qw.like(VmTypeEntity::getName,name);
        }
        this.page(page,qw);

        return Pager.build(page);
    }
}
