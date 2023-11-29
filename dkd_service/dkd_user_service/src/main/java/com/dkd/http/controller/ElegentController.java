package com.dkd.http.controller;


import cn.elegent.security.common.base.TokenDetails;
import cn.elegent.security.token.core.AuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zengzhicheng
 */
@RestController
@RequestMapping("/elegent")
public class ElegentController {

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 刷新令牌
     *
     * @param refresh
     * @return
     */
    @PostMapping("/refreshToken")
    public Map<String, Map<String,String>> refreshToken(@RequestBody Map<String,String> refresh){
        String refreshToken = refresh.get("refreshToken");
        if(refreshToken==null ) {
            return null;
        }

        TokenDetails tokenDetails = authenticationManager.refresh(refreshToken, "admin");
        Map<String,Map<String,String>> result=new HashMap<>();

        Map<String,String> accessMap=new HashMap<>();
        accessMap.put("token",tokenDetails.getAccessToken());

        Map<String,String> refreshMap =new HashMap<>();
        refreshMap.put("token",tokenDetails.getRefreshToken());

        result.put( "accessToken",accessMap);
        result.put( "refreshToken", refreshMap);
        return result;
    }
}
