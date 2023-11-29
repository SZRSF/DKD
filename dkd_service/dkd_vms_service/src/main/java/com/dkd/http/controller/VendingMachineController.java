package com.dkd.http.controller;
import com.dkd.entity.VendingMachineEntity;
import com.dkd.entity.VmTypeEntity;
import com.dkd.http.vo.CreateVMReq;
import com.dkd.http.vo.PolicyReq;
import com.dkd.service.*;
import com.dkd.vo.Pager;
import com.dkd.vo.SkuVO;
import com.dkd.vo.VmVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/vm")
@Slf4j
public class VendingMachineController {

    @Autowired
    private VendingMachineService vendingMachineService;


    @Autowired
    private ChannelService channelService;

    @Autowired
    private VmTypeService vmTypeService;

    @Autowired
    private VmPolicyService vmPolicyService;

    @Autowired
    private SkuService skuService;

    /**
     * 获取售货机商品列表
     *
     * @param innerCode
     * @return
     */
    @RequestMapping("/skuList/{innerCode}")
    public List<SkuVO> getSkuListByInnerCode(@PathVariable String innerCode){
        return skuService.findSkuByInnerCode(innerCode);
    }


    /**
     * 根据id查询
     * @param id
     * @return 实体
     */
    @GetMapping("/{id}")
    public VendingMachineEntity findById(@PathVariable Long id){
        return vendingMachineService.getById(id);
    }

    /**
     * 修改点位
     * @param id
     * @param nodeId
     * @return 是否成功
     */
    @PutMapping("/{id}/{nodeId}")
    public boolean update(@PathVariable String id,@PathVariable String nodeId){
        return vendingMachineService.update(Long.valueOf(id),Long.valueOf(nodeId));
    }




    @GetMapping("/allTypes")
    public List<VmTypeEntity> getAllType(){
        return vmTypeService.list();
    }

    /**
     * 获取在运行的机器列表
     * @param isRunning
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/allByStatus/{isRunning}/{pageIndex}/{pageSize}")
    public Pager<String> getAllByStatus(@PathVariable boolean isRunning,
                                        @PathVariable long pageIndex,
                                        @PathVariable long pageSize){
        return vendingMachineService.getAllInnerCodes(isRunning,pageIndex,pageSize);
    }

    /**
     * 查询设备
     * @param pageIndex
     * @param pageSize
     * @param status
     * @return
     */
    @GetMapping("/search")
    public Pager<VendingMachineEntity> query(
            @RequestParam(value = "pageIndex",required = false,defaultValue = "1") Long pageIndex,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10") long pageSize,
            @RequestParam(value = "status",defaultValue = "",required = false) Integer status,
            @RequestParam(value = "innerCode",defaultValue = "",required = false) String innerCode){
        Pager<VendingMachineEntity> pager = vendingMachineService.query(pageIndex, pageSize, status, innerCode);
        vendingMachineService.readAll( pager.getCurrentPageRecords() );
        return pager;
    }


    /**
     * 获取售货机信息
     * @param innerCode
     * @return
     */
    @RequestMapping("/info/{innerCode}")
    public VmVO getByInnerCode(@PathVariable("innerCode") String innerCode){
        return vendingMachineService.findByInnerCode(innerCode);
    }

    /**
     * 售货机商品是否还有库存
     * @param innerCode
     * @param skuId
     * @return
     */
    @GetMapping("/hasCapacity/{innerCode}/{skuId}")
    public Boolean hasCapacity(@PathVariable String innerCode,@PathVariable Long skuId){
        return vendingMachineService.hasCapacity(innerCode,skuId);
    }



    /**
     * 查询售货机商品当前库存数量
     * @param innerCode
     * @param skuId
     * @return
     */
    @GetMapping("/countCapacity/{innerCode}/{skuId}")
    public int countCapacity(@PathVariable String innerCode,@PathVariable Long skuId){
        return vendingMachineService.countCapacity(innerCode,skuId);
    }



    /**
     * 查询售货机商品最大库存数量
     * @param innerCode
     * @param skuId
     * @return
     */
    @GetMapping("/countMaxCapacity/{innerCode}/{skuId}")
    public int countMaxCapacity(@PathVariable String innerCode,@PathVariable Long skuId){
        return vendingMachineService.countMaxCapacity(innerCode,skuId);
    }



    /**
     * 查查询售货机商品库存率
     * @param innerCode
     * @param skuId
     * @return
     */
    @GetMapping("/countCapacityRatio/{innerCode}/{skuId}")
    public int countCapacityRatio(@PathVariable String innerCode,@PathVariable Long skuId){
        return vendingMachineService.countCapacityRatio(innerCode, skuId);
    }

    /**
     * 新增
     * @param vm
     * @return 是否成功
     */
    @PostMapping
    public boolean add(@RequestBody CreateVMReq vm){
        return vendingMachineService.add( vm );
    }

    /**
     * 给设备应用策略
     *
     * @param policyReq
     * @return
     */
    @PutMapping("/applyPolicy")
    public boolean applyPolicy(@RequestBody PolicyReq policyReq){
        return vmPolicyService.applyPolicy(policyReq);
    }

    /**
     * 取消设备上的策略
     *
     * @param innerCode
     * @param policyId
     * @return
     */
    @PutMapping("/cancelPolicy/{innerCode}/{policyId}")
    public boolean cancelPolicy(@PathVariable String innerCode, @PathVariable int policyId){
        return vmPolicyService.cancelPolicy(innerCode,policyId);
    }

}
