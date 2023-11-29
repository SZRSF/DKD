package com.dkd.http.controller;

import com.dkd.entity.SkuClassEntity;
import com.dkd.entity.SkuEntity;
import com.dkd.exception.LogicException;
import com.dkd.minio.MinioUtil;
import com.dkd.service.SkuService;
import com.dkd.vo.Pager;
import com.dkd.vo.SkuVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/sku")
@Slf4j
public class SkuController {
    private String folder = "image";

    @Autowired
    private SkuService skuService;

    @Autowired
    private MinioUtil minioUtil;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/fileUpload")
    public String uploadSkuImage(@RequestParam("fileName") MultipartFile file) throws IOException {
        Boolean b  = minioUtil.upload("dkd", file.getOriginalFilename(), file);
        if(b){
            return minioUtil.preview(file.getOriginalFilename(), "dkd");
        }
        return "";
    }

    /**
     * 根据skuId查询
     * @param skuId
     * @return 实体
     */
    @GetMapping("/{skuId}")
    public SkuVO findById(@PathVariable String  skuId){
        SkuEntity sku = skuService.getById( Long.valueOf(skuId));
        SkuVO skuVO=new SkuVO();
        BeanUtils.copyProperties(sku,skuVO);
        //真实价格
        skuVO.setRealPrice( skuVO.getPrice() );
        return skuVO;
    }

    /**
     * 新增
     * @param sku
     * @return 是否成功
     */
    @PostMapping
    public boolean add(@RequestBody SkuEntity sku) throws LogicException {
        return skuService.save(sku);
    }

    /**
     * 修改
     * @param sku
     * @return 是否成功
     */
    @PutMapping("/{skuId}")
    public boolean update(@PathVariable String skuId,@RequestBody SkuEntity sku) throws LogicException {
        sku.setSkuId(Long.valueOf(skuId));

        return skuService.update(sku);
    }


    /**
     * 分页查询
     * @param pageIndex 页码
     * @param pageSize 页大小
     * @param classId 商品类别Id
     * @param skuName 商品名称
     * @return 分页结果
     */
    @GetMapping("/search")
    public Pager<SkuEntity> findPage(
            @RequestParam(value = "pageIndex",required = false,defaultValue = "1") long pageIndex,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10") long pageSize,
            @RequestParam(value = "skuName",required = false,defaultValue = "") String skuName,
            @RequestParam(value = "classId",required = false,defaultValue = "0") Integer classId){
        return skuService.findPage( pageIndex,pageSize,classId,skuName);
    }


    /**
     * 获取商品品类列表
     * @return
     */
    @GetMapping("/classes")
    public List<SkuClassEntity> getAllSkuClasses(){
        return skuService.getAllClass();
    }

}