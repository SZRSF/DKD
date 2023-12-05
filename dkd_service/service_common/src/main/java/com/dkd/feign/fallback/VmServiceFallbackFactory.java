package com.dkd.feign.fallback;
import com.dkd.dto.VendoutRunningDTO;
import com.dkd.vo.*;
import com.google.common.collect.Lists;
import com.dkd.feign.VMService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zengzhicheng
 */
@Component
@Slf4j
public class VmServiceFallbackFactory implements FallbackFactory<VMService> {
    @Override
    public VMService create(Throwable throwable) {
        log.error("调用售货机服务失败",throwable);
        return new VMService() {
            @Override
            public VmVO getVMInfo(String innerCode) {
                return null;
            }

            @Override
            public List<SkuVO> getSkuListByInnerCode(String innerCode) {
                return Lists.newArrayList();
            }

            @Override
            public ChannelVO getChannel(String innerCode, Long skuId) {
                return null;
            }

            @Override
            public SkuVO getSku(String skuId) {
                return null;
            }

            @Override
            public Boolean hasCapacity(String innerCode, Long skuId) {
                return null;
            }

            @Override
            public int countCapacity(String innerCode, Long skuId) {
                return 0;
            }

            @Override
            public int countMaxCapacity(String innerCode, Long skuId) {
                return 0;
            }

            @Override
            public int countCapacityRatio(String innerCode, Long skuId) {
                return 0;
            }

            @Override
            public String getNodeName(Long id) {
                return null;
            }

            @Override
            public boolean add(VendoutRunningVo vendoutRunning) {
                return false;
            }


            @Override
            public PolicyVO getPolicy(String innerCode) {
                PolicyVO policyVO=new PolicyVO();
                policyVO.setInnerCode(innerCode);
                policyVO.setDiscount(100);
                return policyVO;
            }

            @Override
            public boolean addVendoutRunning(VendoutRunningDTO vendoutRunning) {
                return false;
            }
        };
    }
}
