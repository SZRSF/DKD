package com.dkd.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dkd.entity.ChannelEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChannelDao extends BaseMapper<ChannelEntity> {


    @Results(id="channelMap",value = {
            @Result(property = "skuId",column = "sku_id"),
            @Result(property = "sku",column = "sku_id",one = @One(select = "com.dkd.dao.SkuDao.selectById"))
    })
    @Select("select * from tb_channel where inner_code=#{innerCode}")
    List<ChannelEntity> getChannelsByInnerCode(String innerCode);


}
