package com.dkd.http.vo;

import lombok.Data;


/**
 * 登录响应对象
 *
 * @author zengzhicheng
 */
@Data
public class LoginResp{

    private Integer userId;

    private String userName;

    private boolean success;

    private String msg;

    /**
     * jwt令牌
     */
    private String token;

}