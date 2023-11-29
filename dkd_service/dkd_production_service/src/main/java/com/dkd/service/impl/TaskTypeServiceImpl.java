package com.dkd.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkd.dao.TaskTypeDao;
import com.dkd.entity.TaskTypeEntity;
import com.dkd.service.TaskTypeService;
import org.springframework.stereotype.Service;

@Service
public class TaskTypeServiceImpl extends ServiceImpl<TaskTypeDao,TaskTypeEntity> implements TaskTypeService{
}
