package com.dkd.feign;
import com.dkd.dto.VendoutRunningDTO;
import com.dkd.feign.fallback.VmServiceFallbackFactory;
import com.dkd.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 售货机微服务feign接口
 *
 * @author zengzhicheng
 */
@FeignClient(value = "vm-service",fallbackFactory = VmServiceFallbackFactory.class)
public interface VMService{

    /**
     * 新增售货机日志
     *
     * @param vendoutRunning
     * @return 是否成功
     */
    @PostMapping("/vendoutRunning")
    boolean addVendoutRunning(@RequestBody VendoutRunningDTO vendoutRunning);

    /**
     * 根据售货机编号查询策略
     *
     * @param innerCode
     * @return
     */
    @GetMapping("/policy/vmPolicy/{innerCode}")
    PolicyVO getPolicy(@PathVariable String innerCode);

    /**
     * 根据售货机编号查询售货机
     * @param innerCode
     * @return
     */
    @GetMapping("/vm/info/{innerCode}")
    VmVO getVMInfo(@PathVariable String innerCode);

    /**
     * 根据售货机编号查询商品列表
     * @param innerCode
     * @return
     */
    @GetMapping("/vm/skuList/{innerCode}")
    List<SkuVO> getSkuListByInnerCode(@PathVariable String innerCode);

    /**
     * 查询某售货机某商品所在货道
     * @param innerCode
     * @param skuId
     * @return
     */
    @GetMapping("/channel/info/innerCode/{innerCode}/sku/{skuId}")
    public ChannelVO getChannel(@PathVariable("innerCode") String innerCode, @PathVariable("skuId") Long skuId);

    /**
     * 根据商品id查询商品
     * @param skuId
     * @return
     */
    @GetMapping("/sku/{skuId}")
    SkuVO getSku(@PathVariable String skuId);

    /**
     * 根据售货机和商品id查询是否存在该商品
     * @param innerCode
     * @param skuId
     * @return
     */
    @GetMapping("/vm/hasCapacity/{innerCode}/{skuId}")
    Boolean hasCapacity(@PathVariable String innerCode,@PathVariable Long skuId);


    /**
     * 查询售货机商品当前库存数量
     * @param innerCode
     * @param skuId
     * @return
     */
    @GetMapping("/vm/countCapacity/{innerCode}/{skuId}")
    int countCapacity(@PathVariable String innerCode,@PathVariable Long skuId);



    /**
     * 查询售货机商品最大库存数量
     * @param innerCode
     * @param skuId
     * @return
     */
    @GetMapping("/vm/countMaxCapacity/{innerCode}/{skuId}")
    int countMaxCapacity(@PathVariable String innerCode,@PathVariable Long skuId);


    /**
     * 查查询售货机商品库存率
     * @param innerCode
     * @param skuId
     * @return
     */
    @GetMapping("/vm/countCapacityRatio/{innerCode}/{skuId}")
    int countCapacityRatio(@PathVariable String innerCode,@PathVariable Long skuId);


    /**
     * 获取点位名称
     * @param id
     * @return
     */
    @GetMapping("/node/nodeName/{id}")
    String getNodeName(@PathVariable Long id);


    /**
     * 新增
     * @param vendoutRunning
     * @return 是否成功
     */
    @PostMapping("/vendoutRunning")
    public boolean add(@RequestBody VendoutRunningVo vendoutRunning);



}
