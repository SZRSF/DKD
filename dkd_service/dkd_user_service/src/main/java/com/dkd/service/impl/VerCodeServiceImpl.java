package com.dkd.service.impl;

import cn.elegent.security.token.core.VerCodeService;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 自定义验证码服务类
 *
 * @author zengzhicheng
 */
@Component
public class VerCodeServiceImpl implements VerCodeService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void saveCode(String clientToken, String content) {
        redisTemplate.boundValueOps(clientToken).set( content, 5, TimeUnit.MINUTES);
    }

    @Override
    public String readCode(String clientToken) {
        return redisTemplate.boundValueOps(clientToken).get();
    }
}
