package com.dkd.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkd.dao.TaskStatusTypeDao;
import com.dkd.entity.TaskStatusEntity;
import com.dkd.service.TaskStatusTypeService;
import org.springframework.stereotype.Service;

@Service
public class TaskStatusTypeServiceImpl extends ServiceImpl<TaskStatusTypeDao, TaskStatusEntity> implements TaskStatusTypeService{
}