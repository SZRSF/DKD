package com.dkd.service;
import cn.elegent.data.core.vo.Pager;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dkd.entity.OrderEntity;
import com.dkd.vo.OrderVO;
import com.dkd.vo.PayVO;

public interface OrderService extends IService<OrderEntity> {


    /**
     * 通过订单编号获取订单实体
     * @param orderNo
     * @return
     */
    OrderEntity getByOrderNo(String orderNo);

    /**
     * 微信小程序支付创建订单
     *
     * @param payVO
     * @return
     */
    OrderEntity createOrder(PayVO payVO, String platform);

    /**
     * 搜索订单
     * @param pageIndex
     * @param pageSize
     * @param orderNo
     * @param openId
     * @param startDate
     * @param endDate
     * @return
     */
    Pager<OrderVO> search(Integer pageIndex, Integer pageSize, String orderNo, String openId, String startDate, String endDate);

}
