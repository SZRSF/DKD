package com.dkd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dkd.entity.UserEntity;
import com.dkd.vo.Pager;
import com.dkd.vo.UserVO;

import java.util.List;

public interface UserService extends IService<UserEntity> {
    /**
     * 获取所有运营人员数量
     */
    Integer getOperatorCount();

    /**
     * 获取所有维修员数量
     * @return
     */
    Integer getRepairerCount();

    /**
     * 分页查询
     *
     * @param pageIndex
     * @param pageSize
     * @param userName
     * @return
     */
    Pager<UserEntity> findPage(long pageIndex, long pageSize, String userName,Integer roleId);

    /**
     * 获取某区域下所有运营人员
     * @param regionId
     * @return
     */
    List<UserVO> getOperatorList(Long regionId);

    /**
     * 获取某区域下所有运维人员
     * @param regionId
     * @return
     */
    List<UserVO> getRepairerList(Long regionId);

    /**
     * 获取某区域下维修员/运营员总数
     * @param isRepair
     * @return
     */
    Integer getCountByRegion(Long regionId,Boolean isRepair);




}
