package com.dkd.http.controller;
import com.dkd.entity.*;
import com.dkd.exception.LogicException;
import com.dkd.http.vo.*;
import com.dkd.service.*;
import com.dkd.vo.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskDetailsService taskDetailsService;

    @Autowired
    private TaskTypeService taskTypeService;



    /**
     * 搜索工单
     * @param pageIndex
     * @param pageSize
     * @param innerCode 设备编号
     * @param userId  工单所属人Id
     * @param taskCode 工单编号
     * @param status 工单状态
     * @param isRepair 是否是维修工单
     * @return
     */
    @GetMapping("/search")
    public Pager<TaskEntity> search(
            @RequestParam(value = "pageIndex",required = false,defaultValue = "1") Long pageIndex,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10") Long pageSize,
            @RequestParam(value = "innerCode",required = false,defaultValue = "") String innerCode,
            @RequestParam(value = "userId",required = false,defaultValue = "") Integer userId,
            @RequestParam(value = "taskCode",required = false,defaultValue = "") String taskCode,
            @RequestParam(value = "status",required = false,defaultValue = "") Integer status,
            @RequestParam(value = "isRepair",required = false,defaultValue = "") Boolean isRepair,
            @RequestParam(value = "start",required = false,defaultValue = "") String start,
            @RequestParam(value = "end",required = false,defaultValue = "") String end){
        return taskService.search(pageIndex,pageSize,innerCode,userId,taskCode,status,isRepair,start,end);
    }



    /**
     * 根据taskId查询
     * @param taskId
     * @return 实体
     */
    @GetMapping("/taskInfo/{taskId}")
    public TaskEntity findById(@PathVariable Long taskId){
        return taskService.getById(taskId);
    }

    @GetMapping("/allTaskStatus")
    public List<TaskStatusEntity> getAllStatus(){
        return taskService.getAllStatus();
    }

    /**
     * 获取工单类型
     * @return
     */
    @GetMapping("/typeList")
    public List<TaskTypeEntity> getProductionTypeList(){
        return taskTypeService.list();
    }

    /**
     * 获取工单详情
     * @param taskId
     * @return
     */
    @GetMapping("/details/{taskId}")
    public List<TaskDetailsEntity> getDetail(@PathVariable long taskId){
        return taskDetailsService.getByTaskId(taskId);
    }


    @Autowired
    private JobService jobService;

    /**
     * 设置自动补货工单配置
     * @param config
     * @return
     */
    @PostMapping("/autoSupplyConfig")
    public Boolean setAutoSupplyConfig(@RequestBody AutoSupplyConfigViewModel config){
        return jobService.setJob(config.getAlertValue());
    }

    /**
     * 获取补货预警值
     * @return
     */
    @GetMapping("/supplyAlertValue")
    public Integer getSupplyAlertValue(){
        JobEntity jobEntity = jobService.getAlertValue();
        if(jobEntity == null) return 0;
        return jobEntity.getAlertValue();
    }

    /**
     * 获取该售货机是否有未完成的补货工单
     * @return
     */
    @GetMapping("/hasTask/{innerCode}/{productionType}")
    public Boolean hasTask(@PathVariable("innerCode") String innerCode,
                           @PathVariable int productionType){
        return taskService.hasTask(innerCode,productionType);
    }


    @Autowired
    private TaskCollectService taskCollectService;




}