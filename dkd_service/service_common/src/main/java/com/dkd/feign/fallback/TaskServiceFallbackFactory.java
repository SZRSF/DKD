package com.dkd.feign.fallback;
import com.dkd.feign.TaskService;
import com.dkd.vo.TaskCollectVo;
import com.google.common.collect.Lists;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class TaskServiceFallbackFactory implements FallbackFactory<TaskService> {
    @Override
    public TaskService create(Throwable throwable) {
        return new TaskService() {
            @Override
            public Integer getSupplyAlertValue() {
                return 30;  //补货警戒线默认值
            }

            @Override
            public Boolean hasTask(String innerCode, int productionType) {
                return null;
            }

        };
    }
}
