package com.dkd.http.controller;
import com.dkd.entity.PolicyEntity;
import com.dkd.entity.VmPolicyEntity;
import com.dkd.exception.LogicException;
import com.dkd.service.PolicyService;
import com.dkd.service.VmPolicyService;
import com.dkd.vo.Pager;
import com.dkd.vo.VmVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/policy")
public class PolicyController {

    @Autowired
    private PolicyService policyService;

    /**
     * 策略列表

     * @return 列表
     */
    @GetMapping
    public  List<PolicyEntity> findList(){
        return policyService.list();
    }

    /**
     * 根据policyId查询
     * @param policyId
     * @return 实体
     */
    @GetMapping("/{policyId}")
    public PolicyEntity findById(@PathVariable Integer policyId){
        return policyService.getById( policyId );
    }

    /**
     * 新增
     * @param policy
     * @return 是否成功
     */
    @PostMapping
    public boolean add(@RequestBody PolicyEntity policy) throws LogicException {
        return policyService.save( policy );
    }

    /**
     * 修改
     * @param policyId
     * @param policy
     * @return 是否成功
     */
    @PutMapping("/{policyId}")
    public boolean update(@PathVariable Integer policyId,@RequestBody PolicyEntity policy) throws LogicException {
        policy.setPolicyId( policyId );

        return policyService.updateById( policy );
    }

    /**
     * 删除策略
     * @param policyId
     * @return
     */
    @DeleteMapping("/{policyId}")
    public Boolean delete(@PathVariable Integer policyId){
        return policyService.delete(policyId);
    }


    /**
     * 搜索
     * @param policyName 策略名
     * @param pageIndex 当前页码
     * @param pageSize 每页数量
     * @return
     */
    @GetMapping("/search")
    public Pager<PolicyEntity> search(
            @RequestParam(value = "policyName",required = false,defaultValue = "") String policyName,
            @RequestParam(value = "pageIndex",required = false,defaultValue = "1") long pageIndex,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10") long pageSize){
        return policyService.search(policyName,pageIndex,pageSize);
    }

    @Autowired
    private VmPolicyService vmPolicyService;

    @GetMapping("/vmPolicy/{innerCode}")
    public VmPolicyEntity getByInnerCode(@PathVariable String innerCode){
        return vmPolicyService.getPolicyByInnerCode(innerCode);
    }


    @GetMapping("/vmList/{policyId}")
    public Pager<VmVO> vmList(@PathVariable Integer policyId,
                                              @RequestParam(value = "pageIndex",required = false,defaultValue = "1") long pageIndex,
                                              @RequestParam(value = "pageSize",required = false,defaultValue = "10") long pageSize){
        return policyService.vmList(policyId,pageIndex,pageSize);
    }


}
