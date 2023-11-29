package com.dkd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dkd.entity.PartnerEntity;
import com.dkd.http.vo.PartnerReq;
import com.dkd.http.vo.PartnerUpdatePwdReq;
import com.dkd.vo.Pager;

/**
 * 合作商接口
 */
public interface PartnerService extends IService<PartnerEntity> {

    /**
     * 更新合作商
     * @param
     * @return
     */
    Boolean modify(Integer id,PartnerReq req);

    /**
     * 删除
     * @param id
     * @return
     */
    boolean delete(Integer id);

    /**
     * 重置密码
     * @param id
     */
    void resetPwd(Integer id);

    /**
     * 查询合作商
     * @param pageIndex
     * @param pageSize
     * @param name
     * @return
     */
    Pager<PartnerEntity> search(Long pageIndex,Long pageSize,String name);

    /**
     * 更新密码
     * @param req
     * @return
     */
    Boolean updatePwd(Integer id,PartnerUpdatePwdReq req);
}
