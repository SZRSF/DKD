package com.dkd.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dkd.entity.NodeEntity;
import com.dkd.vo.CountVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NodeDao extends BaseMapper<NodeEntity> {

    @Select("select IFNULL(COUNT(1),0) from tb_node where region_id=#{regionId} ")
    Integer getCountByRegion(Long regionId);

    @Select("select * from tb_node where id=#{nodeId} limit 1")
    NodeEntity getById(long nodeId);

    @Select("select * from tb_node where `name` like CONCAT('%',#{name},'%')")
    Page<NodeEntity> searchByName(Page<NodeEntity> page,String name);


    /**
     * 查询点位列表
     * @param page
     * @param wrapper
     * @return
     */
    @Select("SELECT n.*, IFNULL( v.vmCount,0) AS vmCount FROM `tb_node` n \n" +
            "LEFT JOIN \n" +
            " (SELECT COUNT(*) AS vmCount, node_id AS id  FROM `tb_vending_machine` GROUP BY node_id) v \n" +
            " ON n.id=v.id ${ew.customSqlSegment} ")
    Page<NodeEntity> search(Page<NodeEntity> page, @Param("ew")LambdaQueryWrapper<NodeEntity> wrapper);

    /**
     * 按区域统计记录个数
     * @return
     */
    @Select("select count(*) as count, region_id as id  from tb_node group by region_id")
    List<CountVo> countGroupByRegion();


}
