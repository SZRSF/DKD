package com.dkd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

@Data
@TableName(value = "tb_task_status")
public class TaskStatusEntity implements Serializable{

    @TableId(value = "status_id",type = IdType.AUTO)
    private Integer statusId;//status_id


	
    @TableField(value = "status_name")
    private String statusName;//status_name

}
