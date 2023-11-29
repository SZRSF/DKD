package com.dkd.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dkd.entity.TaskStatusEntity;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface TaskStatusTypeDao extends BaseMapper<TaskStatusEntity> {
}
