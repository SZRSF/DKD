package com.dkd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "tb_vendout_running")
public class VendoutRunningEntity extends AbstractEntity implements Serializable{

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;//id

    @TableField(value = "order_no")
    private String orderNo;//订单编号

    @TableField(value = "inner_code")
    private String innerCode;//售货机编号

    @TableField(value = "channel_code")
    private String channelCode;//货道编号

    @TableField(value = "status")
    private String status;//状态


}
