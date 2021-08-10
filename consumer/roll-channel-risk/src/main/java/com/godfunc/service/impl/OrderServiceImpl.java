package com.godfunc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.Order;
import com.godfunc.mapper.OrderMapper;
import com.godfunc.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
}
