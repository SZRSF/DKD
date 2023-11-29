package com.dkd.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkd.entity.*;
import com.dkd.service.TaskTypeService;
import com.google.common.base.Strings;
import com.dkd.constant.TaskStatus;
import com.dkd.constant.TaskType;
import com.dkd.constant.VmStatus;
import com.dkd.dao.TaskDao;
import com.dkd.exception.LogicException;
import com.dkd.feign.UserService;
import com.dkd.feign.VMService;
import com.dkd.http.vo.*;
import com.dkd.service.TaskDetailsService;
import com.dkd.service.TaskService;
import com.dkd.service.TaskStatusTypeService;
import com.dkd.vo.Pager;
import com.dkd.vo.UserVO;
import com.dkd.vo.VmVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskServiceImpl extends ServiceImpl<TaskDao,TaskEntity> implements TaskService{

    @Autowired
    private TaskStatusTypeService statusTypeService;
    
    @Autowired
    private TaskTypeService taskTypeService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private VMService vmService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskDetailsService taskDetailsService;


    @Override
    public Pager<TaskEntity> search(Long pageIndex, Long pageSize, String innerCode, Integer userId, String taskCode, Integer status, Boolean isRepair, String start, String end) {
        Page<TaskEntity> page = new Page<>(pageIndex,pageSize);
        LambdaQueryWrapper<TaskEntity> qw = new LambdaQueryWrapper<>();
        if(!Strings.isNullOrEmpty(innerCode)){
            qw.eq(TaskEntity::getInnerCode,innerCode);
        }
        if(userId != null && userId > 0){
            qw.eq(TaskEntity::getUserId,userId);
        }
        if(!Strings.isNullOrEmpty(taskCode)){
            qw.like(TaskEntity::getTaskCode,taskCode);
        }
        if(status != null && status > 0){
            qw.eq(TaskEntity::getTaskStatus,status);
        }
        if(isRepair != null){
            if(isRepair){
                qw.ne(TaskEntity::getProductTypeId, TaskType.TASK_TYPE_SUPPLY);
            }else {
                qw.eq(TaskEntity::getProductTypeId,TaskType.TASK_TYPE_SUPPLY);
            }
        }
        if(!Strings.isNullOrEmpty(start) && !Strings.isNullOrEmpty(end)){
            qw
                    .ge(TaskEntity::getCreateTime, LocalDate.parse(start, DateTimeFormatter.ISO_LOCAL_DATE))
                    .le(TaskEntity::getCreateTime,LocalDate.parse(end,DateTimeFormatter.ISO_LOCAL_DATE));
        }
        //根据最后更新时间倒序排序
        qw.orderByDesc(TaskEntity::getUpdateTime);
        Page<TaskEntity> taskPage = this.page(page, qw);
        readAll( taskPage.getRecords() );
        return Pager.build(taskPage);
    }



    @Override
    public List<TaskStatusEntity> getAllStatus() {
        QueryWrapper<TaskStatusEntity> qw = new QueryWrapper<>();
        qw.lambda()
                .ge(TaskStatusEntity::getStatusId, TaskStatus.TASK_STATUS_CREATE);

        return statusTypeService.list(qw);
    }


    /**
     * 生成工单编号
     * @return
     */
    private String generateTaskCode(){
        //日期+序号
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));  //日期字符串
        String key= "dkd.task.code."+date; //redis key
        Object obj = redisTemplate.opsForValue().get(key);
        if(obj==null){
            redisTemplate.opsForValue().set(key,1L, Duration.ofDays(1) );
            return date+"0001";
        }
        return date+  Strings.padStart( redisTemplate.opsForValue().increment(key,1).toString(),4,'0');
    }


    /**
     * 统计工单数量
     * @param start
     * @param end
     * @param repair 是否是运维工单
     * @param taskStatus
     * @return
     */
    private int taskCount(LocalDateTime start, LocalDateTime end , Boolean repair , Integer taskStatus ){
        LambdaQueryWrapper<TaskEntity> qw = new LambdaQueryWrapper<>();
        qw.ge(TaskEntity::getUpdateTime,start)
                .le(TaskEntity::getUpdateTime,end);
        //按工单状态查询
        if(taskStatus!=null){
            qw.eq(TaskEntity::getTaskStatus,taskStatus);
        }
        if(repair){//如果是运维工单
            qw.ne(TaskEntity::getProductTypeId,TaskType.TASK_TYPE_SUPPLY);
        }else{
            qw.eq(TaskEntity::getProductTypeId,TaskType.TASK_TYPE_SUPPLY);
        }
        return this.count(qw);
    }


    /**
     * 创建工单校验
     * @param vmStatus
     * @param productType
     * @throws LogicException
     */
    private void checkCreateTask(Integer vmStatus,int productType) throws LogicException {
        //如果是投放工单，状态为运营
        if(productType == TaskType.TASK_TYPE_DEPLOY  && vmStatus == VmStatus.VM_STATUS_RUNNING){
            throw new LogicException("该设备已在运营");
        }

        //如果是补货工单，状态不是运营状态
        if(productType == TaskType.TASK_TYPE_SUPPLY  && vmStatus != VmStatus.VM_STATUS_RUNNING){
            throw new LogicException("该设备不在运营状态");
        }

        //如果是撤机工单，状态不是运营状态
        if(productType == TaskType.TASK_TYPE_REVOKE  && vmStatus != VmStatus.VM_STATUS_RUNNING){
            throw new LogicException("该设备不在运营状态");
        }
    }


    /**
     * 同一台设备下是否存在未完成的工单
     * @param innerCode
     * @param productionType
     * @return
     */
    @Override
    public boolean hasTask(String innerCode,int productionType){
        QueryWrapper<TaskEntity> qw = new QueryWrapper<>();
        qw.lambda()
                .select(TaskEntity::getTaskId)
                .eq(TaskEntity::getInnerCode,innerCode)
                .eq(TaskEntity::getProductTypeId,productionType)
                .le(TaskEntity::getTaskStatus,TaskStatus.TASK_STATUS_PROGRESS);
        return this.count(qw) > 0;
    }



    @Override
    public void readAll(List<TaskEntity> taskList) {

        List<TaskTypeEntity> typeEntityList = taskTypeService.list();
        List<TaskStatusEntity> statusList = statusTypeService.list();

        taskList.forEach(taskEntity -> {
            //工单类型
            Optional<TaskTypeEntity> type = typeEntityList.stream().filter(taskTypeEntity -> taskTypeEntity.getTypeId().equals(taskEntity.getProductTypeId())).findFirst();
            if(type.isPresent()){
                taskEntity.setTaskType( type.get() );
            }
            //工单状态
            Optional<TaskStatusEntity> status = statusList.stream().filter(taskStatusTypeEntity -> taskStatusTypeEntity.getStatusId().equals(taskEntity.getTaskStatus())).findFirst();
            if(status.isPresent()){
                taskEntity.setTaskStatusTypeEntity( status.get() );
            }

        } );

    }


}
