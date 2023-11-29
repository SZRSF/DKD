package com.dkd.feign;
import com.dkd.feign.fallback.UserServiceFallbackFactory;
import com.dkd.vo.PartnerVO;
import com.dkd.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 用户微服务feign接口
 */
@FeignClient(value = "user-service",fallbackFactory = UserServiceFallbackFactory.class)
public interface UserService{

    /**
     * 根据用户id查询用户
     * @param id
     * @return
     */
    @GetMapping("/user/{id}")
    UserVO getUser(@PathVariable("id") int id);

    /**
     * 获取某区域下的运维人员列表
     * @param regionId
     * @return
     */
    @GetMapping("/user/repairers/{regionId}")
    List<UserVO> getRepairers(@PathVariable("regionId") Long regionId);

    /**
     * 获取某区域下的运营人员列表
     * @param regionId
     * @return
     */
    @GetMapping("/user/operators/{regionId}")
    List<UserVO> getOperators(@PathVariable("regionId") Long regionId);

    /**
     * 统计运营人员数量
     * @return
     */
    @GetMapping("/user/operaterCount")
    Integer getOperatorCount();

    /**
     * 统计运维人员数量
     * @return
     */
    @GetMapping("/user/repairerCount")
    Integer getRepairerCount();


    /**
     * 根据售货机编号获取运维人员列表
     * @param innerCode
     * @return
     */
    @GetMapping("/user/repairerList/{innerCode}")
    List<UserVO> getRepairerListByInnerCode(@PathVariable String innerCode);

    /**
     * 根据售货机编号获取运营人员列表
     * @param innerCode
     * @return
     */
    @GetMapping("/user/operatorList/{innerCode}")
    List<UserVO> getOperatorListByInnerCode(@PathVariable String innerCode);

    /**
     * 根据id获取合作商
     * @param id
     * @return
     */
    @GetMapping("/partner/{id}")
    PartnerVO getPartner(@PathVariable Integer id);

    /**
     * 根据区域和类型查询人员数量
     * @param regionId
     * @param isRepair
     * @return
     */
    @GetMapping("/user/countByRegion/{regionId}/{isRepair}")
    Integer getCountByRegion(@PathVariable Long regionId,@PathVariable Boolean isRepair);

    /**
     * 根据id获取合作商名称
     * @param id
     * @return
     */
    @GetMapping("/partner/name")
    String getPartnerName(@PathVariable Integer id);


}
