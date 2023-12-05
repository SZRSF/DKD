package com.dkd.service.impl;

import cn.elegent.data.elasticsearch7.Elasticsearch7DataService;
import com.dkd.vo.OrderVO;
import org.springframework.stereotype.Component;


/**
 * @author zengzhicheng
 */
@Component
public class OrderEsService extends Elasticsearch7DataService<OrderVO> {

}
