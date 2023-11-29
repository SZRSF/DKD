package com.dkd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "tb_admin")
public class AdminEntity implements Serializable{
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;//id

    @TableField(value = "user_name")
    private String userName;//用户名称
    @TableField(value = "login_name")
    private String loginName;//登录名
    @TableField(value = "password")
    private String password;//密码

    /**
     * 头像地址
     */
    private String image;
    /**
     * 是否启用
     */
    private Boolean status;

}
