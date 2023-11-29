package com.dkd.http.controller;
import com.dkd.entity.OrderCollectEntity;
import com.dkd.service.OrderCollectService;
import com.dkd.vo.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 报表controller
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private OrderCollectService orderCollectService;






}
