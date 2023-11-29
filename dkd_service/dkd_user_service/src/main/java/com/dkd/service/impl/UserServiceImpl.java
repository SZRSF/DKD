package com.dkd.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkd.feign.TaskService;

import com.google.common.base.Strings;
import com.dkd.dao.UserDao;
import com.dkd.entity.UserEntity;
import com.dkd.service.UserService;
import com.dkd.vo.Pager;
import com.dkd.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserDao,UserEntity> implements UserService{
    @Autowired
    private RedisTemplate<String,String> redisTemplate;



    @Override
    public Integer getOperatorCount() {
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getRoleCode,"1002");

        return this.count(wrapper);
    }

    @Override
    public Integer getRepairerCount() {
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getRoleCode,"1003");

        return this.count(wrapper);
    }

    @Override
    public Pager<UserEntity> findPage(long pageIndex, long pageSize, String userName,Integer roleId) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<UserEntity> page =
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageIndex,pageSize);

        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        if(!Strings.isNullOrEmpty(userName)){
            wrapper.like(UserEntity::getUserName,userName);
        }
        if(roleId!=null){
            wrapper.eq(UserEntity::getRoleId,roleId);
        }

        Page<UserEntity> userPage = this.page(page, wrapper);
        userPage.getRecords().forEach( userEntity ->{
            if(userEntity!=null){
                if("1002".equals( userEntity.getRoleCode() )){
                    userEntity.getRole().setRoleName( "运营人员"  );
                }
                if("1003".equals( userEntity.getRoleCode() )){
                    userEntity.getRole().setRoleName( "运维人员"  );
                }
            }
        });

        return Pager.build( userPage);
    }



    @Override
    public List<UserVO> getOperatorList(Long regionId) {
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper
            .eq(UserEntity::getRoleCode,"1002")
            .eq(UserEntity::getRegionId,regionId)
            .eq(UserEntity::getStatus,true);

        return this.list(wrapper)
            .stream()
            .map(u->{
                UserVO vo = new UserVO();
                BeanUtils.copyProperties(u,vo);
                vo.setRoleName("运营人员");
                vo.setRoleCode(u.getRoleCode());
                vo.setUserId(u.getId());
                return vo;
            }).collect(Collectors.toList());
    }

    @Override
    public List<UserVO> getRepairerList(Long regionId) {
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper
            .eq(UserEntity::getRoleCode,"1003")
            .eq(UserEntity::getRegionId,regionId)
            .eq(UserEntity::getStatus,true);

        return this.list(wrapper)
            .stream()
            .map(u->{
                UserVO vo = new UserVO();
                BeanUtils.copyProperties(u,vo);
                vo.setRoleName("运维人员");
                vo.setRoleCode(u.getRoleCode());
                vo.setUserId(u.getId());
                return vo;
            }).collect(Collectors.toList());
    }

    @Override
    public Integer getCountByRegion(Long regionId, Boolean isRepair) {
        var qw = new LambdaQueryWrapper<UserEntity>();
        qw.eq(UserEntity::getRegionId,regionId);
        if(isRepair){
            qw.eq(UserEntity::getRoleCode,"1003");
        }else {
            qw.eq(UserEntity::getRoleCode,"1002");
        }

        return this.count(qw);
    }

    @Autowired
    private TaskService taskService;




}
