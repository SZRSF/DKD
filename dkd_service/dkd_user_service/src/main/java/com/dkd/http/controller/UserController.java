package com.dkd.http.controller;
import cn.elegent.security.common.base.AuthenticateResult;
import cn.elegent.security.common.base.UserAuth;
import cn.elegent.security.token.core.AuthenticationManager;
import com.dkd.entity.UserEntity;
import com.dkd.feign.VMService;
import com.dkd.http.vo.LoginReq;
import com.dkd.http.vo.LoginResp;
import com.dkd.http.vo.UserReq;
import com.dkd.service.UserService;
import com.dkd.vo.Pager;
import com.dkd.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;


    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 登录
     *
     * @param loginReq
     * @return
     */
    @PostMapping("/login")
    public LoginResp login(@RequestBody LoginReq loginReq) {
        UserAuth userAuth = new UserAuth();
        BeanUtils.copyProperties(loginReq, userAuth);
        userAuth.setUsername(loginReq.getLoginName());

        // 管理员
        if (loginReq.getLoginType() ==0){
            userAuth.setType("admin");
        }

        // 员工
        if (loginReq.getLoginType() == 1) {
            userAuth.setUsername(loginReq.getMobile());
            userAuth.setType("user");
        }

        // 合作商
        if (loginReq.getLoginType() == 2) {
            userAuth.setType("partner");
            userAuth.setUsername(loginReq.getAccount());
        }

        AuthenticateResult authenticateResult = authenticationManager.authenticate(userAuth);
        LoginResp loginResp  = new LoginResp();
        loginResp.setSuccess(authenticateResult.isAuthenticated());

        if (authenticateResult.isAuthenticated()) {
            loginResp.setUserName(authenticateResult.getUserDetails().getUsername());
            loginResp.setToken(authenticateResult.getTokenDetails().getAccessToken());
            loginResp.setUserId(Integer.parseInt(authenticateResult.getUserDetails().getUserId()));
        } else {
            loginResp.setMsg(authenticateResult.getErrorInfo());
        }
        return loginResp;
    }

    /**
     * 根据id查询
     * @param id
     * @return 实体
     */
    @GetMapping("/{id}")
    public UserVO findById(@PathVariable Integer id){
        UserEntity userEntity = userService.getById(id);
        if(userEntity == null) return null;

        return convert(userEntity);
    }

    /**
     * 新增
     * @param req
     * @return 是否成功
     */
    @PostMapping
    public boolean add(@RequestBody UserReq req){
        UserEntity user = new UserEntity();
        user.setUserName(req.getUserName());
        user.setRegionId(Long.valueOf(req.getRegionId()));
        user.setRegionName(req.getRegionName());
        user.setMobile(req.getMobile());

        if(req.getRoleId()==2){
            user.setRoleCode( "1002" );
        }
        if(req.getRoleId()==3){
            user.setRoleCode( "1003" );
        }

        user.setStatus(req.getStatus());
        user.setImage(req.getImage());

        return userService.save(user);
    }

    /**
     * 修改
     * @param id
     * @param req
     * @return 是否成功
     */
    @PutMapping("/{id}")
    public boolean update(@PathVariable Integer id,@RequestBody UserReq req){
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setUserName(req.getUserName());
        user.setRegionId(Long.valueOf(req.getRegionId()));
        user.setRegionName(req.getRegionName());
        user.setMobile(req.getMobile());
        //user.setRoleId(req.getRoleId());
        user.setStatus(req.getStatus());

        return userService.updateById(user);
    }

    /**
     * 删除
     * @param id
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public  boolean delete(@PathVariable Integer id){
        return userService.removeById(id);
    }

    /**
     * 分页查询
     * @param pageIndex 页码
     * @param pageSize 页大小
     * @param userName 用户名
     * @return 分页结果
     */
    @GetMapping("/search")
    public Pager<UserEntity> findPage(
            @RequestParam(value = "pageIndex",required = false,defaultValue = "1") long pageIndex,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10") long pageSize,
            @RequestParam(value = "userName",required = false,defaultValue = "") String userName,
            @RequestParam(value = "roleId",required = false,defaultValue = "0") Integer roleId){
        return userService.findPage( pageIndex,pageSize,userName,roleId);
    }


    /**
     * 获取运营员数量
     * @return
     */
    @GetMapping("/operaterCount")
    public Integer getOperatorCount(){
        return userService.getOperatorCount();
    }

    /**
     * 获取维修员数量
     * @return
     */
    @GetMapping("/repairerCount")
    public Integer getRepairerCount(){
        return userService.getRepairerCount();
    }

    /**
     * 获取某区域下所有运营员
     * @param regionId
     * @return
     */
    @GetMapping("/operators/{regionId}")
    public List<UserVO> getOperatorList(@PathVariable String regionId){
        return userService.getOperatorList(Long.valueOf(regionId));
    }

    /**
     * 获取某区域下所有运维员
     * @param regionId
     * @return
     */
    @GetMapping("/repairers/{regionId}")
    public List<UserVO> getRepairerList(@PathVariable String regionId){
        return userService.getRepairerList(Long.valueOf(regionId));
    }

    @Autowired
    private VMService vmService;

    /**
     * 通过售货机编号获取同区域下所有运营员
     * @param innerCode
     * @return
     */
    @GetMapping("/operatorList/{innerCode}")
    public List<UserVO> getOperatorListByInnerCode(@PathVariable String innerCode){
        var vm = vmService.getVMInfo(innerCode);
        if(vm == null) return null;

        return userService.getOperatorList(vm.getRegionId());
    }

    /**
     * 通过售货机编号获取同区域下所有维修员
     * @param innerCode
     * @return
     */
    @GetMapping("/repairerList/{innerCode}")
    public List<UserVO> getRepairerListByInnerCode(@PathVariable String innerCode){
        var vm = vmService.getVMInfo(innerCode);
        if(vm == null) return null;
        return userService.getRepairerList(vm.getRegionId());
    }



    /**
     * 获取某区域下维修员/运营员总数
     * @param isRepair
     * @return
     */
    @GetMapping("/countByRegion/{regionId}/{isRepair}")
    public Integer getCountByRegion(@PathVariable String  regionId,@PathVariable Boolean isRepair){
        return userService.getCountByRegion(Long.valueOf(regionId),isRepair);
    }



    private UserVO convert(UserEntity userEntity){
        UserVO userVO = new UserVO();
        userVO.setMobile(userEntity.getMobile());
        userVO.setRoleCode(userEntity.getRoleCode());
        userVO.setUserId(userEntity.getId());
        userVO.setRoleName("");
        userVO.setUserName(userEntity.getUserName());
        userVO.setStatus(userEntity.getStatus());
        userVO.setRegionId(userEntity.getRegionId());
        userVO.setRegionName(userEntity.getRegionName());
        userVO.setImage(userEntity.getImage());

        return userVO;
    }



}
