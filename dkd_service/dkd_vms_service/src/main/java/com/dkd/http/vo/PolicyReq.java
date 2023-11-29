package com.dkd.http.vo;

import lombok.Data;

import java.util.List;

/**
 * @author zengzhicheng
 */
@Data
public class PolicyReq{
    private List<String> innerCodeList;
    private int policyId;
}
