package com.dkd.feign;
import com.dkd.feign.fallback.TaskServiceFallbackFactory;
import com.dkd.vo.TaskCollectVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;

@FeignClient(value = "task-service",fallbackFactory = TaskServiceFallbackFactory.class)
public interface TaskService {

    /**
     * 获取补货预警值
     * @return
     */
    @GetMapping("/task/supplyAlertValue")
    public Integer getSupplyAlertValue();

    /**
     *
     */
    @GetMapping("/task/hasTask/{innerCode}/{productionType}")
    public Boolean hasTask(@PathVariable("innerCode") String innerCode,
                           @PathVariable int productionType);



}
