package com.dkd.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkd.dao.TaskCollectDao;
import com.dkd.entity.TaskCollectEntity;
import com.dkd.service.TaskCollectService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskCollectServiceImpl extends ServiceImpl<TaskCollectDao, TaskCollectEntity> implements TaskCollectService {

}
