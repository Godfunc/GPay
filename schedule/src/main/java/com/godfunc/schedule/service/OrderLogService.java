package com.godfunc.schedule.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.OrderLog;
import com.godfunc.mapper.OrderLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
@Service
@RequiredArgsConstructor
public class OrderLogService extends ServiceImpl<OrderLogMapper, OrderLog> {

}
