package com.dkd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkd.constant.VmStatus;
import com.dkd.http.vo.CreateVMReq;
import com.dkd.utils.UUIDUtils;
import com.google.common.base.Strings;


import com.dkd.dao.VendingMachineDao;

import com.dkd.entity.*;
import com.dkd.exception.LogicException;
import com.dkd.service.*;

import com.dkd.vo.Pager;
import com.dkd.vo.SkuVO;
import com.dkd.vo.VmVO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.TraceableRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VendingMachineServiceImpl extends ServiceImpl<VendingMachineDao,VendingMachineEntity> implements VendingMachineService{

    @Autowired
    private NodeService nodeService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private VmTypeService vmTypeService;


    @Autowired
    private RegionService regionService;

    @Autowired
    private VendingMachineDao vendingMachineDao;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(CreateVMReq vendingMachine) {
        VendingMachineEntity vendingMachineEntity = new VendingMachineEntity();
        vendingMachineEntity.setNodeId(Long.valueOf(vendingMachine.getNodeId()));
        vendingMachineEntity.setVmType(vendingMachine.getVmType());
        NodeEntity nodeEntity = nodeService.getById(vendingMachineEntity.getNodeId());
        if (nodeEntity == null) {
            throw  new LogicException("所选点不存在");
        }
        // 复制属性
        BeanUtils.copyProperties(nodeEntity, vendingMachineEntity);
        vendingMachineEntity.setCreateUserId(Long.valueOf(vendingMachine.getCreateUserId()));
        vendingMachineEntity.setInnerCode(UUIDUtils.getUUID());
        vendingMachineEntity.setClientId(UUIDUtils.generateClientId( vendingMachineEntity.getInnerCode() ));
        this.save(vendingMachineEntity);
        // 创建货道数据
        return createChannel(vendingMachineEntity);
    }

    /**
     * 创建货道
     *
     * @param vm
     * @return
     */
    private boolean createChannel(VendingMachineEntity vm) {
        VmTypeEntity vmType = vmTypeService.getById(vm.getVmType());
        List<ChannelEntity> channelList= Lists.newArrayList();
        for(int i = 1; i <= vmType.getVmRow(); i++) {
            for(int j = 1; j <= vmType.getVmCol(); j++) {
                ChannelEntity channel = new ChannelEntity();
                channel.setChannelCode(i+"-"+j);
                channel.setCurrentCapacity(0);
                channel.setInnerCode(vm.getInnerCode());
                channel.setLastSupplyTime(vm.getLastSupplyTime());
                channel.setMaxCapacity(vmType.getChannelMaxCapacity());
                channel.setVmId(vm.getId());
                channelList.add(channel);
            }
        }
        channelService.saveBatch(channelList);
        return true;
    }

    @Override
    public boolean update(Long id, Long nodeId) {
        VendingMachineEntity vm = this.getById(id);
        if(vm.getVmStatus() == VmStatus.VM_STATUS_RUNNING)
            throw new LogicException("改设备正在运营");
        NodeEntity nodeEntity = nodeService.getById(nodeId);
        BeanUtils.copyProperties( nodeEntity,vm );
        return this.updateById(vm);
    }


    @Override
    public Pager<String> getAllInnerCodes(boolean isRunning, long pageIndex, long pageSize) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<VendingMachineEntity> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageIndex,pageSize);

        QueryWrapper<VendingMachineEntity> qw = new QueryWrapper<>();
        if(isRunning){
            qw.lambda()
                    .select(VendingMachineEntity::getInnerCode)
                    .eq(VendingMachineEntity::getVmStatus,1);
        }else {
            qw.lambda()
                    .select(VendingMachineEntity::getInnerCode)
                    .ne(VendingMachineEntity::getVmStatus,1);
        }
        this.page(page,qw);
        Pager<String> result = new Pager<>();
        result.setCurrentPageRecords(page.getRecords().stream().map(VendingMachineEntity::getInnerCode).collect(Collectors.toList()));
        result.setPageIndex(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setTotalCount(page.getTotal());

        return result;
    }

    @Override
    public Pager<VendingMachineEntity> query(Long pageIndex, Long pageSize, Integer status,String innerCode) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<VendingMachineEntity> page
                = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageIndex,pageSize);
        LambdaQueryWrapper<VendingMachineEntity> queryWrapper = new LambdaQueryWrapper<>();
        if(status != null){
            queryWrapper.eq(VendingMachineEntity::getVmStatus,status);
        }
        if(!Strings.isNullOrEmpty(innerCode)){
            queryWrapper.likeLeft(VendingMachineEntity::getInnerCode,innerCode);
        }
        this.page(page, queryWrapper);
        Pager<VendingMachineEntity> machineEntityPager = Pager.build(page);
        return machineEntityPager;

    }



    @Override
    public VmVO findByInnerCode(String innerCode) {
        LambdaQueryWrapper<VendingMachineEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VendingMachineEntity::getInnerCode,innerCode);
        VendingMachineEntity vm = this.getOne(queryWrapper);
        VmVO vmVO=new VmVO();
        BeanUtils.copyProperties(vm,vmVO);
        NodeEntity nodeEntity = nodeService.getById(vm.getNodeId());
        vmVO.setNodeAddr(nodeEntity.getAddr());//地址
        vmVO.setNodeName(nodeEntity.getName());//名称
        RegionEntity regionEntity = regionService.findById(nodeEntity.getRegionId());
        vmVO.setRegionName( regionEntity.getName());//区域名称
        VmTypeEntity vmType = vmTypeService.getById(vm.getVmType());
        vmVO.setVmTypeName( vmType.getName() );

        return vmVO;
    }



    @Override
    public List<SkuVO> getSkuListByInnerCode(String innerCode) {
        //获取货道列表
        List<ChannelEntity> channelList = channelService.getChannelesByInnerCode(innerCode).stream()
                .filter(c -> c.getSkuId() > 0 && c.getSku() != null).collect(Collectors.toList());
        //获取有商品的库存余量
        Map<SkuEntity, Integer> skuMap = channelList.stream()
                .collect(Collectors.groupingBy(
                        ChannelEntity::getSku,
                        Collectors.summingInt(ChannelEntity::getCurrentCapacity)));//对库存数求和
        return skuMap.entrySet().stream().map( entry->{
                    SkuEntity sku = entry.getKey(); //查询商品
                    SkuVO skuVO = new SkuVO();
                    BeanUtils.copyProperties( sku,skuVO );
                    skuVO.setImage(sku.getSkuImage());//图片
                    skuVO.setCapacity( entry.getValue() );
                    skuVO.setRealPrice(sku.getPrice());//真实价格
                    return  skuVO;
                } ).sorted(Comparator.comparing(SkuVO::getCapacity).reversed())  //按库存量降序排序
                .collect(Collectors.toList());
    }

    @Override
    public Boolean hasCapacity(String innerCode, Long skuId) {
        var qw = new LambdaQueryWrapper<ChannelEntity>();
        qw
                .eq(ChannelEntity::getInnerCode,innerCode)
                .eq(ChannelEntity::getSkuId,skuId)
                .gt(ChannelEntity::getCurrentCapacity,0);
        return channelService.count(qw) > 0;
    }

    @Override
    public int countCapacity(String innerCode, Long skuId) {
        var qw = new QueryWrapper<ChannelEntity>();
        qw
                .select("sum(current_capacity  ) as current_capacity")
                .lambda()
                .eq(ChannelEntity::getInnerCode,innerCode)
                .eq(ChannelEntity::getSkuId,skuId);
        List<ChannelEntity> list = channelService.list(qw);
        if(list.get(0)!=null){
            return list.get(0).getCurrentCapacity();
        }else{
            return 0;
        }
    }

    @Override
    public int countMaxCapacity(String innerCode, Long skuId) {
        var qw = new QueryWrapper<ChannelEntity>();
        qw
                .select("sum(max_capacity  ) as max_capacity")
                .lambda()
                .eq(ChannelEntity::getInnerCode,innerCode)
                .eq(ChannelEntity::getSkuId,skuId);
        List<ChannelEntity> list = channelService.list(qw);
        if(list.get(0)!=null){
            return list.get(0).getMaxCapacity();
        }else{
            return 0;
        }
    }

    @Override
    public int countCapacityRatio(String innerCode, Long skuId) {
        int maxCapacity = countMaxCapacity(innerCode, skuId); //获取最大库存
        if(maxCapacity<=0){
            return 0;
        }
        int capacity = countCapacity(innerCode, skuId);  //获得当前库存
        if(capacity<=0){
            return 0;
        }
        return (new BigDecimal( capacity ).multiply(new BigDecimal(100)).divide( new BigDecimal(maxCapacity ) ,2, RoundingMode.HALF_UP )).intValue();
    }

    @Override
    public VendingMachineEntity findByClientId(String clientId) {
        QueryWrapper<VendingMachineEntity> qw = new QueryWrapper<>();
        qw.lambda().eq( VendingMachineEntity::getClientId ,clientId );
        return this.getOne(qw);
    }

    @Override
    public void readAll(List<VendingMachineEntity> vendingMachineEntityList) {
        // 点位列表
        List<NodeEntity> nodeList = nodeService.list();
        // 设备类型列表
        List<VmTypeEntity> vmTypeList = vmTypeService.list();

        vendingMachineEntityList.stream().forEach( vendingMachine -> {
            //查询点位数据
            Optional<NodeEntity> node = nodeList.stream()
                    .filter(nodeEntity -> nodeEntity.getId().equals(vendingMachine.getNodeId()))
                    .findFirst();
            if(node.isPresent()){
                vendingMachine.setNode( node.get() );
            }

            //查询类型数据
            Optional<VmTypeEntity> vmType = vmTypeList.stream()
                    .filter(vmTypeEntity -> vmTypeEntity.getTypeId().equals(vendingMachine.getVmType()))
                    .findFirst();
            if(vmType.isPresent()){
                vendingMachine.setType(vmType.get() );
            }

        } );
    }


}
