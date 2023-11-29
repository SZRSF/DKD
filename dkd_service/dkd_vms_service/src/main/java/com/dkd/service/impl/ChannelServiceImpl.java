package com.dkd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkd.dao.ChannelDao;
import com.dkd.entity.ChannelEntity;
import com.dkd.entity.PolicyEntity;
import com.dkd.entity.SkuEntity;
import com.dkd.http.vo.ChannelConfig;
import com.dkd.service.ChannelService;
import com.dkd.service.PolicyService;
import com.dkd.service.SkuService;
import com.dkd.vo.ChannelVO;
import com.dkd.vo.Pager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChannelServiceImpl extends ServiceImpl<ChannelDao, ChannelEntity> implements ChannelService {


    @Override
    public Pager<ChannelEntity> findPage(long pageIndex, long pageSize, Map searchMap) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<ChannelEntity> page =
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageIndex, pageSize);

        QueryWrapper queryWrapper = createQueryWrapper(searchMap);
        this.page(page, queryWrapper);

        Pager<ChannelEntity> pageResult = new Pager<>();
        pageResult.setCurrentPageRecords(page.getRecords());
        pageResult.setPageIndex(page.getCurrent());
        pageResult.setPageSize(page.getSize());
        pageResult.setTotalCount(page.getTotal());
        return pageResult;
    }

    @Override
    public List<ChannelEntity> getChannelesByInnerCode(String innerCode) {
        LambdaQueryWrapper<ChannelEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChannelEntity::getInnerCode, innerCode);

        return this.list(queryWrapper);
    }

    @Override
    public ChannelVO getChanneleByInnerCodeAndSku(String innerCode, Long skuId) {

        LambdaQueryWrapper<ChannelEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChannelEntity::getInnerCode, innerCode)
                .eq(ChannelEntity::getSkuId, skuId)
                .gt(ChannelEntity::getCurrentCapacity, 0);

        List<ChannelEntity> channelEntityList = this.list(queryWrapper);
        if (channelEntityList.size() == 0) {
            return null;
        }
        ChannelEntity channelEntity = channelEntityList.get(0);

        ChannelVO channelVO = new ChannelVO();
        BeanUtils.copyProperties(channelEntity, channelVO);
        channelVO.setSkuName(channelEntity.getSku().getSkuName());

        return channelVO;
    }

    @Override
    public ChannelEntity getChannelInfo(String innerCode, String channelCode) {
        LambdaQueryWrapper<ChannelEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(ChannelEntity::getInnerCode, innerCode)
                .eq(ChannelEntity::getChannelCode, channelCode);
        return this.getOne(queryWrapper);
    }

    @Override
    public Boolean channelSet(ChannelConfig channelConfig) {
        // 当前售卖机所有货道列表
        List<ChannelEntity> channelEntityList = getChannelesByInnerCode(channelConfig.getInnerCode());

        List<ChannelEntity> channelList = channelConfig.getChannelList().stream().map(channelVo -> {
            Optional<ChannelEntity> optional = channelEntityList.stream().filter(channelEntity ->
                    channelEntity.getChannelCode().equals(channelVo.getChannelCode())).findFirst();
            if (optional.isPresent()) {
                ChannelEntity channelEntity = optional.get();
                channelEntity.setSkuId(Long.valueOf(channelVo.getSkuId()));
                return channelEntity;
            }
            return null;
        }).collect(Collectors.toList());
        // 批量保存
        return this.saveOrUpdateBatch(channelList);
    }

    @Override
    public List<ChannelEntity> findList(Map searchMap) {
        QueryWrapper queryWrapper = createQueryWrapper(searchMap);

        return this.list(queryWrapper);
    }

    /**
     * 条件构建
     *
     * @param searchMap
     * @return
     */
    private QueryWrapper createQueryWrapper(Map searchMap) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (searchMap != null) {
            queryWrapper.allEq(searchMap);
        }
        return queryWrapper;
    }

    @Autowired
    private SkuService skuService;


    @Autowired
    private PolicyService policyService;

    /**
     * 计算真实售价
     *
     * @param skuId
     * @return
     */
    private Integer calRealPrice(Long skuId, Integer policyId) {
        PolicyEntity policy = policyService.getById(policyId);
        SkuEntity skuEntity = skuService.getById(skuId);
        if (skuEntity == null) {
            return 0;
        }
        if (policy != null) {
            BigDecimal price = new BigDecimal(skuEntity.getPrice() * policy.getDiscount());
            return price.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP).intValue();
        }
        return skuEntity.getPrice();
    }


}
