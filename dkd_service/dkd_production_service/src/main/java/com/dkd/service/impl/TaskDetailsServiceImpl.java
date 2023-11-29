package com.dkd.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.dkd.dao.TaskDetailsDao;
import com.dkd.entity.TaskDetailsEntity;
import com.dkd.service.TaskDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskDetailsServiceImpl extends ServiceImpl<TaskDetailsDao,TaskDetailsEntity> implements TaskDetailsService{
    @Override
    public List<TaskDetailsEntity> getByTaskId(long taskId) {

        QueryWrapper<TaskDetailsEntity> qw = new QueryWrapper<>();
        qw.lambda()
                .eq(TaskDetailsEntity::getTaskId,taskId);

        return this.list(qw);
    }


}
