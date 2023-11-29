package com.dkd.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkd.dao.NodeDao;
import com.dkd.dao.RegionDao;
import com.dkd.entity.NodeEntity;
import com.dkd.entity.RegionEntity;
import com.dkd.service.NodeService;
import com.dkd.service.RegionService;
import com.dkd.vo.Pager;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RegionServiceImpl extends ServiceImpl<RegionDao, RegionEntity> implements RegionService {


    @Autowired
    private NodeService nodeService;

    @Autowired
    private NodeDao nodeDao;

    @Override
    public RegionEntity findById(Long regionId) {
        RegionEntity regionEntity = this.getById(regionId);
        LambdaQueryWrapper<NodeEntity> qw = new LambdaQueryWrapper<NodeEntity>();
        qw.eq(NodeEntity::getRegionId,regionId);
        regionEntity.setNodeList(nodeService.list(qw));
        return regionEntity;
    }


    @Override
    public Pager<RegionEntity> search(Long pageIndex, Long pageSize, String name) {
        Page<RegionEntity> p=new Page<>( pageIndex,pageSize );

        LambdaQueryWrapper<NodeEntity> qw = new LambdaQueryWrapper<NodeEntity>();
        if(!Strings.isNullOrEmpty(name)){
            qw.like(NodeEntity::getName,name);
        }
        Page<RegionEntity> page = baseMapper.search(p, qw);
        return Pager.build(page);
    }


}
