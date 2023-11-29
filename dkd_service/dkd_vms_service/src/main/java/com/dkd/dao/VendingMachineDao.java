package com.dkd.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dkd.entity.VendingMachineEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface VendingMachineDao extends BaseMapper<VendingMachineEntity> {

    @Select("select * from tb_vending_machine where inner_code=#{innerCode} limit 1")
    VendingMachineEntity findByInnerCode(String innerCode);


    @Select("select IFNULL(COUNT(1),0) from tb_vending_machine where node_id=#{nodeId}")
    long getCountByNodeId(long nodeId);



}
