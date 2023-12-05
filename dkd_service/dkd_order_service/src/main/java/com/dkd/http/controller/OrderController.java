package com.dkd.http.controller;

import cn.elegent.data.core.vo.Pager;
import cn.elegent.idempotence.annotation.ElegentIdem;
import cn.elegent.pay.ElegentPay;
import cn.elegent.pay.constant.Platform;
import cn.elegent.pay.dto.PayRequest;
import cn.elegent.pay.dto.PayResponse;
import cn.elegent.pay.dto.RefundRequest;
import com.dkd.entity.OrderEntity;
import com.dkd.service.OrderService;
import com.dkd.vo.OrderVO;
import com.dkd.vo.PayVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author zengzhicheng
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ElegentPay elegentPay;

    /**
     * 获取openId
     *
     * @param jsCode
     * @return
     */
    @GetMapping("/openid/{jsCode}")
    public String getOpenid(@PathVariable("jsCode")  String jsCode){
        return elegentPay.getOpenid(jsCode, Platform.WX);
    }


    /**
     * 支付
     *
     * @param payVO
     * @return
     */
    @ElegentIdem(type="sn" ,name="requestId")
    @PostMapping("/requestPay/{tradeType}/{platform}")
    public PayResponse requestPay(@RequestBody PayVO payVO, @PathVariable("tradeType") String tradeType, @PathVariable("platform") String platform) {
        //创建订单
        OrderEntity orderEntity = orderService.createOrder(payVO, platform);
        //封装支付请求对象调用支付
        PayRequest param = new PayRequest();
        //商品名称
        param.setBody(orderEntity.getSkuName());
        //订单编号
        param.setOrderSn(orderEntity.getOrderNo());
        //订单金额
        param.setTotalFee(orderEntity.getAmount());
        //openid
        param.setOpenid(payVO.getOpenId());
        return elegentPay.requestPay(param, tradeType, platform);
    }


    /**
     * 退款
     *
     * @param orderNo
     * @param platform
     * @return
     */
    @GetMapping("/refund/{orderNo}/{platform}")
    public boolean refund(@PathVariable("orderNo") String orderNo, @PathVariable("platform") String platform) {
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setTotalFee(1);
        refundRequest.setRefundAmount(1);
        refundRequest.setOrderSn(orderNo);
        refundRequest.setRequestNo(System.currentTimeMillis()+"");
        return elegentPay.refund(refundRequest, platform);
    }


    /**
     * 搜索
     * @param pageIndex
     * @param pageSize
     * @param orderNo
     * @param openId
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/search")
    public Pager<OrderVO> search(
            @RequestParam(value = "pageIndex",required = false,defaultValue = "1") Integer pageIndex,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize,
            @RequestParam(value = "orderNo",required = false,defaultValue = "") String orderNo,
            @RequestParam(value = "openId",required = false,defaultValue = "") String openId,
            @RequestParam(value = "startDate",required = false,defaultValue = "") String startDate,
            @RequestParam(value = "endDate",required = false,defaultValue = "") String endDate){
        return orderService.search(pageIndex,pageSize,orderNo,openId,startDate,endDate);
    }
}