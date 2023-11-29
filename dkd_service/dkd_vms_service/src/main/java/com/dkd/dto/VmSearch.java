package com.dkd.dto;

import lombok.Data;

@Data
public class VmSearch {

    /**
     * 纬度
     */
    private Double lat;
    /**
     * 经度
     */
    private Double lon;
    /**
     * 搜索半径
     */
    private Integer distance;



}
