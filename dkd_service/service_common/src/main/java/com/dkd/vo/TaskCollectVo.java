package com.dkd.vo;

import lombok.Data;

@Data
public class TaskCollectVo {


    private Integer userId;
    /**
     * 完成数
     */
    private Integer finishCount;
    /**
     * 进行中工单数
     */
    private Integer progressCount;
    /**
     * 取消的工单数
     */
    private Integer cancelCount;

}
