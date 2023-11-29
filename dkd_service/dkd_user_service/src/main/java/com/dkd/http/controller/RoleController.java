package com.dkd.http.controller;

import com.dkd.http.vo.RoleVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RoleController {


    @GetMapping("/role")
    public List<RoleVo> list(){
        List<RoleVo> list=new ArrayList<>();
        RoleVo roleVo1=new RoleVo();
        roleVo1.setRoleId(2);
        roleVo1.setRoleCode("1002");
        roleVo1.setRoleName("运营人员");

        RoleVo roleVo2=new RoleVo();
        roleVo2.setRoleId(3);
        roleVo2.setRoleCode("1003");
        roleVo2.setRoleName("运维人员");

        list.add(roleVo1);
        list.add(roleVo2);
        return list;
    }

}
