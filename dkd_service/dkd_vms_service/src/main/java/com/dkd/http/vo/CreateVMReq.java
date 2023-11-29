package com.dkd.http.vo;

import lombok.Data;

/**
 * @author zengzhicheng
 */
@Data
public class CreateVMReq {
    /**
     * 售货机类型
     */
    private int vmType;

    /**
     * 所属点位Id
     */
    private String  nodeId;

    /**
     * 创建人Id
     */
    private Integer createUserId;
}
