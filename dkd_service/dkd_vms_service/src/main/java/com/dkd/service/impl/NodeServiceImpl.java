package com.dkd.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkd.entity.BusinessTypeEntity;
import com.dkd.entity.RegionEntity;
import com.dkd.service.BusinessTypeService;
import com.dkd.service.RegionService;
import com.google.common.base.Strings;
import com.dkd.dao.NodeDao;
import com.dkd.entity.NodeEntity;
import com.dkd.entity.VendingMachineEntity;
import com.dkd.service.NodeService;
import com.dkd.service.VendingMachineService;
import com.dkd.vo.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NodeServiceImpl  extends ServiceImpl<NodeDao, NodeEntity> implements NodeService{

    @Override
    public Pager<NodeEntity> search(String name, String regionId, long pageIndex, long pageSize) {
        Page<NodeEntity> page =  new Page<>(pageIndex,pageSize);
        LambdaQueryWrapper<NodeEntity> queryWrapper = new LambdaQueryWrapper<>();
        if(!Strings.isNullOrEmpty(name)){
            queryWrapper.like(NodeEntity::getName,name);
        }
        if(!Strings.isNullOrEmpty(regionId)){
            Long regionIdLong = Long.valueOf(regionId);
            queryWrapper.eq(NodeEntity::getRegionId,regionIdLong);
        }
        queryWrapper.orderByDesc(NodeEntity::getCreateTime);
        Pager<NodeEntity> pager = Pager.build(baseMapper.search( page,queryWrapper ));
        return pager;
    }

    @Autowired
    private VendingMachineService vmService;

    @Override
    public List<VendingMachineEntity> getVmList(long id) {
        QueryWrapper<VendingMachineEntity> qw = new QueryWrapper<>();
        qw.lambda()
                .eq(VendingMachineEntity::getNodeId,id);

        return vmService.list(qw);
    }

    @Autowired
    private RegionService regionService;

    @Autowired
    private BusinessTypeService businessTypeService;


    @Override
    public void readAll(List<NodeEntity> nodeEntities) {

        //区域列表
        List<RegionEntity>  regionList = regionService.list();
        //商圈
        List<BusinessTypeEntity> busList = businessTypeService.list();

        nodeEntities.stream().forEach( nodeEntity -> {

            //区域
            Optional<RegionEntity> regionEntity1 = regionList.stream()
                    .filter(regionEntity -> regionEntity.getId().equals(nodeEntity.getRegionId())).findFirst();
            regionEntity1.ifPresent(nodeEntity::setRegion);

            //商圈
            Optional<BusinessTypeEntity> businessTypeEntity1 = busList.stream()
                    .filter(businessTypeEntity -> businessTypeEntity.getId().equals(nodeEntity.getBusinessId())).findFirst();

            businessTypeEntity1.ifPresent(nodeEntity::setBusinessType);

        });

    }


}
