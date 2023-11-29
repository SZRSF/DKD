package com.dkd.service.impl;

import cn.elegent.security.common.base.UserDetails;
import cn.elegent.security.token.core.UserDetailsServices;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dkd.entity.AdminEntity;
import com.dkd.entity.PartnerEntity;
import com.dkd.entity.UserEntity;
import com.dkd.service.AdminService;
import com.dkd.service.PartnerService;
import com.dkd.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * UserDetailsServices的实现类
 *
 * @author zengzhicheng
 */
@Component
@Slf4j
public class UserDetailServiceImpl implements UserDetailsServices {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private PartnerService partnerService;

    @Override
    public UserDetails loadUserByUsername(String username, String type) {
        UserDetails userDetails = new UserDetails();
        // 管理员登录
        if ("admin".equals(type)) {
            QueryWrapper<AdminEntity> qw = new QueryWrapper<>();
            qw.lambda()
                    .eq(AdminEntity::getLoginName, username);
            AdminEntity adminEntity = adminService.getOne(qw);
            if (adminEntity==null) {return null;}
            userDetails.setUsername(adminEntity.getUserName());
            userDetails.setPassword(adminEntity.getPassword());
            List<String> roles = new ArrayList<>();
            roles.add("ADMIN");
            userDetails.setUserId(adminEntity.getId().toString());
            userDetails.setRoles(roles);
            userDetails.setSuperUser(true);
        }
        // 员工登录
        if ("user".equals(type)) {
            QueryWrapper<UserEntity> qw = new QueryWrapper<>();
            qw.lambda()
                    .eq(UserEntity::getMobile, username);
            UserEntity userEntity = userService.getOne(qw);
            if (userEntity==null) {return null;}
            userDetails.setUsername(userEntity.getMobile());
            List<String> roles = new ArrayList<>();
            roles.add("USER");
            userDetails.setUserId(userEntity.getId().toString());
            userDetails.setRoles(roles);
            userDetails.setSuperUser(true);
        }
        // 合作商登录
        if ("partner".equals(type)) {
            QueryWrapper<PartnerEntity> qw = new QueryWrapper<>();
            qw.lambda()
                    .eq(PartnerEntity::getAccount, username);
            PartnerEntity  partnerEntity = partnerService.getOne(qw);
            if (partnerEntity==null) {return null;}
            userDetails.setUsername(partnerEntity.getAccount());
            userDetails.setPassword(partnerEntity.getPassword());
            List<String> roles = new ArrayList<>();
            roles.add("PARTNER");
            userDetails.setUserId(partnerEntity.getId().toString());
            userDetails.setRoles(roles);
            userDetails.setSuperUser(true);
        }
        return userDetails;
    }
}
