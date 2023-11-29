package com.dkd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkd.dao.AdminDao;
import com.dkd.dao.UserDao;
import com.dkd.entity.AdminEntity;
import com.dkd.entity.UserEntity;
import com.dkd.service.AdminService;
import com.dkd.vo.Pager;
import com.dkd.vo.UserVO;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminServiceImpl extends ServiceImpl<AdminDao, AdminEntity> implements AdminService {



}
