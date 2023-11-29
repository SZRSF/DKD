package com.dkd.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dkd.entity.TaskCollectEntity;
import com.dkd.entity.TaskEntity;
import com.dkd.entity.TaskStatusEntity;
import com.dkd.exception.LogicException;
import com.dkd.http.vo.TaskViewModel;
import com.dkd.vo.Pager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 工单业务逻辑
 */
public interface TaskService extends IService<TaskEntity> {


    /**
     * 通过条件搜索工单列表
     * @param pageIndex
     * @param pageSize
     * @param innerCode
     * @param userId
     * @param taskCode
     * @param isRepair 是否是运维工单
     * @return
     */
    Pager<TaskEntity> search(Long pageIndex, Long pageSize, String innerCode, Integer userId, String taskCode, Integer status, Boolean isRepair, String start, String end);





    /**
     * 获取所有状态类型
     * @return
     */
    List<TaskStatusEntity> getAllStatus();





    /**
     * 检查该售货机是否有未完成的工单
     * @param innerCode
     * @param productionType
     * @return
     */
    boolean hasTask(String innerCode,int productionType);


    /**
     * 加载
     * @param taskList
     */
    void readAll( List<TaskEntity> taskList );

}
