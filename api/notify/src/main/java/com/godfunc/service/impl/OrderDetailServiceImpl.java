package com.godfunc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.OrderDetail;
import com.godfunc.mapper.OrderDetailMapper;
import com.godfunc.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {


    @Override
    public OrderDetail getByOrderId(Long orderId) {
        return getOne(Wrappers.<OrderDetail>lambdaQuery().eq(OrderDetail::getOrderId, orderId));
    }
}
