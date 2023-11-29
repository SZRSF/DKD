package com.dkd.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkd.dao.VendoutRunningDao;
import com.dkd.entity.VendoutRunningEntity;
import com.dkd.service.VendoutRunningService;
import com.dkd.vo.Pager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class VendoutRunningServiceImpl extends ServiceImpl<VendoutRunningDao,VendoutRunningEntity> implements VendoutRunningService{
    @Override
    public Pager<VendoutRunningEntity> findPage(long pageIndex, long pageSize, Map searchMap) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<VendoutRunningEntity> page =
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageIndex,pageSize);

        QueryWrapper queryWrapper = createQueryWrapper( searchMap );
        this.page(page,queryWrapper);

        Pager<VendoutRunningEntity> pageResult = new Pager<>();
        pageResult.setCurrentPageRecords(page.getRecords());
        pageResult.setPageIndex(page.getCurrent());
        pageResult.setPageSize(page.getSize());
        pageResult.setTotalCount(page.getTotal());
        return pageResult;
    }

    @Override
    public void updateStatus(String orderSn, String status) {

        QueryWrapper<VendoutRunningEntity> queryWapper=new QueryWrapper<>();
        queryWapper.lambda().eq( VendoutRunningEntity::getOrderNo,  orderSn);

        VendoutRunningEntity vendoutRunningEntity = this.getOne(queryWapper);
        vendoutRunningEntity.setStatus(status);
        this.updateById(vendoutRunningEntity);
    }


    @Override
    public List<VendoutRunningEntity> findList(Map searchMap) {
        QueryWrapper queryWrapper = createQueryWrapper( searchMap );
        return this.list(queryWrapper);
    }

    /**
     * 条件构建
     * @param searchMap
     * @return
     */
    private QueryWrapper createQueryWrapper(Map searchMap){
        QueryWrapper queryWrapper=new QueryWrapper(  );
        if(searchMap!=null){
            queryWrapper.allEq(searchMap);
        }
        return queryWrapper;
    }

}
