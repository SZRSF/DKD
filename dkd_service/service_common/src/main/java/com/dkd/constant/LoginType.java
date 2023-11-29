package com.dkd.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录类型
 */

@Getter
@AllArgsConstructor
public enum LoginType {

    /**
     * 管理员后台登录
     */
    LOGIN_ADMIN(0,"ADMIN"),
    /**
     * 运维/运营人员app登录
     */
    LOGIN_EMP(1,"EMP"),

    /**
     * 合作商登录
     */
    LOGIN_PARTNER(2,"PARTNER"),
    ;
    private Integer code;

    private String name;

    public static LoginType getByCode(Integer code){
        for (LoginType index : LoginType.values()) {
            if(index.getCode().equals(code)){
                return index;
            }
        }
        return null;
    }
}
