package com.dkd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkd.dao.BusinessTypeDao;
import com.dkd.entity.BusinessTypeEntity;
import com.dkd.service.BusinessTypeService;
import org.springframework.stereotype.Service;

@Service
public class BusinessTypeServiceImpl extends ServiceImpl<BusinessTypeDao, BusinessTypeEntity> implements BusinessTypeService {
}
