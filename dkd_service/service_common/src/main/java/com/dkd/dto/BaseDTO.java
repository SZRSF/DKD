package com.dkd.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class BaseDTO implements Serializable{
    /**
     * InnerCode售货机编号
     */
    private String innerCode;

}
