package com.dkd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.dkd.dao.PartnerDao;
import com.dkd.entity.PartnerEntity;
import com.dkd.exception.LogicException;
import com.dkd.http.vo.PartnerReq;
import com.dkd.http.vo.PartnerUpdatePwdReq;
import com.dkd.service.PartnerService;
import com.dkd.utils.BCrypt;
import com.dkd.vo.Pager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartnerServiceImpl extends ServiceImpl<PartnerDao, PartnerEntity> implements PartnerService {
    private final RedisTemplate<String,String> redisTemplate;


    @Override
    public Boolean modify(Integer id, PartnerReq req) {
        var uw = new LambdaUpdateWrapper<PartnerEntity>();
        uw
                .set(PartnerEntity::getName,req.getName())
                .set(PartnerEntity::getRatio,req.getRatio())
                .set(PartnerEntity::getContact,req.getContact())
                .set(PartnerEntity::getPhone,req.getPhone());
        PartnerEntity partnerEntity = new PartnerEntity();
        BeanUtils.copyProperties(req,partnerEntity);
        partnerEntity.setId(id);

        return this.updateById(partnerEntity);

    }

    @Override
    public boolean delete(Integer id) {
        return this.removeById(id);
    }

    @Override
    public void resetPwd(Integer id) {
        String pwd = BCrypt.hashpw("123456",BCrypt.gensalt());
        var uw = new LambdaUpdateWrapper<PartnerEntity>();
        uw
                .set(PartnerEntity::getPassword,pwd)
                .eq(PartnerEntity::getId,id);

        this.update(uw);
    }

    @Override
    public Pager<PartnerEntity> search(Long pageIndex, Long pageSize, String name) {
        Page<PartnerEntity> page = new Page<>(pageIndex,pageSize);
        LambdaQueryWrapper<PartnerEntity> qw = new LambdaQueryWrapper<>();
        if(!Strings.isNullOrEmpty(name)){
            qw.like(PartnerEntity::getName,name);
        }
        this.page(page,qw);
        page.getRecords().forEach(p->{
            p.setPassword("");
            p.setVmCount(10);
        });

        return Pager.build(page);
    }

    @Override
    public Boolean updatePwd(Integer id,PartnerUpdatePwdReq req) {
        var partner = this.getById(id);
        if(partner == null){
            throw new LogicException("合作商不存在");
        }
        if(!BCrypt.checkpw(req.getPassword(),partner.getPassword())){
            throw new LogicException("原始密码错误");
        }
        var uw = new LambdaUpdateWrapper<PartnerEntity>();
        uw
                .set(PartnerEntity::getPassword,BCrypt.hashpw(req.getPassword(),BCrypt.gensalt()))
                .eq(PartnerEntity::getId,id);

        return this.update(uw);
    }
}
