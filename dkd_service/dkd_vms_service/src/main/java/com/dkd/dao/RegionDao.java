package com.dkd.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dkd.entity.NodeEntity;
import com.dkd.entity.RegionEntity;
import org.apache.ibatis.annotations.*;
@Mapper
public interface RegionDao extends BaseMapper<RegionEntity> {

    @Select("select * from tb_region where id=#{id}")
    RegionEntity getById(Long id);


    @Select("SELECT r.*, IFNULL( n.nodeCount,0)  AS nodeCount \n" +
            "FROM  `tb_region` AS r    \n" +
            "LEFT JOIN \n" +
            "( SELECT COUNT(*) AS nodeCount, region_id AS id  FROM tb_node GROUP BY region_id ) AS n \n" +
            "ON r.id = n.id  ${ew.customSqlSegment} ")
    Page<RegionEntity> search(Page<RegionEntity> regionVo,  @Param("ew") LambdaQueryWrapper<NodeEntity> wrapper);

}
