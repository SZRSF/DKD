package com.dkd.http.controller;
import com.dkd.entity.ChannelEntity;
import com.dkd.http.vo.ChannelConfig;
import com.dkd.service.ChannelService;
import com.dkd.vo.ChannelVO;
import com.dkd.vo.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/channel")
public class ChannelController {

    @Autowired
    private ChannelService channelService;


    /**
     * 分页查询
     * @param pageIndex 页码
     * @param pageSize 页大小
     * @param searchMap 条件
     * @return 分页结果
     */
    @GetMapping("/page/{pageIndex}/{pageSize}")
    public Pager<ChannelEntity> findPage(@PathVariable long pageIndex, @PathVariable long pageSize, @RequestParam Map searchMap){
        return channelService.findPage( pageIndex,pageSize,searchMap );
    }

    /**
     * 查询货道
     *
     * @param innerCode
     * @return
     */
    @GetMapping("/channelList/{innerCode}")
    public List<ChannelEntity> getChannelList(@PathVariable("innerCode") String innerCode){
        return channelService.getChannelesByInnerCode(innerCode);
    }

    /**
     * 查询某售货机中某商品在哪个货道
     * @param innerCode
     * @param skuId
     * @return
     */
    @GetMapping("/info/innerCode/{innerCode}/sku/{skuId}")
    public ChannelVO getChannel(@PathVariable("innerCode") String innerCode, @PathVariable("skuId") Long skuId){
        return channelService.getChanneleByInnerCodeAndSku(innerCode, skuId );
    }

    @GetMapping("/channelInfo/{innerCode}/{channelCode}")
    public ChannelEntity getChannelInfo(@PathVariable("innerCode") String innerCode,@PathVariable("channelCode") String channelCode){
        return channelService.getChannelInfo(innerCode,channelCode);
    }

    /**
     * 货道设置
     *
     * @param channelConfig
     * @return
     */
    @PutMapping("/channelConfig")
    public Boolean channelConfig(@RequestBody ChannelConfig channelConfig){
        return channelService.channelSet( channelConfig );
    }

}
