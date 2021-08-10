package com.godfunc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.OrderLog;
import com.godfunc.mapper.OrderLogMapper;
import com.godfunc.service.OrderLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
@Service
@RequiredArgsConstructor
public class OrderLogServiceImpl extends ServiceImpl<OrderLogMapper, OrderLog> implements OrderLogService {

}
