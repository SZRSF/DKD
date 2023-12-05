package com.dkd.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dkd.entity.SkuEntity;
import com.dkd.vo.SkuVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SkuDao extends BaseMapper<SkuEntity> {


    @Results(id = "skuMap" ,value = {
            @Result(column = "class_id", property = "classId"),
            @Result(column = "class_id", property = "skuClass" ,one = @One(select = "com.dkd.dao.SkuClassDao.selectById"))
    })
    @Select("select * from tb_sku where sku_id=#{skuId} limit 1")
    SkuEntity getById(long skuId);

    @Select("SELECT sc.*,IFNULL( p.discount,100) AS discount, IFNULL(ROUND(sc.price*discount/100), sc.price) AS real_price FROM \n" +
            "(SELECT s.sku_id,sku_name,sku_image image,price,class_id,unit, c.capacity FROM tb_sku s \n" +
            "INNER JOIN \n" +
            "(\n" +
            "SELECT sku_id, SUM(`current_capacity`) capacity  FROM `tb_channel` WHERE `inner_code`=#{innerCode} AND sku_id>0 GROUP BY `sku_id` \n" +
            ") c \n" +
            "ON s.sku_id=c.sku_id \n" +
            "ORDER BY c.capacity DESC\n" +
            ") sc \n" +
            "LEFT JOIN \n" +
            "( SELECT discount FROM  `tb_vm_policy` WHERE `inner_code`=#{innerCode} ) p \n" +
            "ON 1=1 \n")
    List<SkuVO> findSkuByInnerCode( String innerCode );
}
