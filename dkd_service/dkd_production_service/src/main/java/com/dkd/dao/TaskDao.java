package com.dkd.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dkd.entity.TaskCollectEntity;
import com.dkd.entity.TaskEntity;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TaskDao extends BaseMapper<TaskEntity> {


}
