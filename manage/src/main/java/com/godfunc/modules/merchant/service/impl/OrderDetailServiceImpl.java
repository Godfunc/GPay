package com.godfunc.modules.merchant.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.OrderDetail;
import com.godfunc.modules.merchant.mapper.OrderDetailMapper;
import com.godfunc.modules.merchant.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

    @Override
    public OrderDetail getByOrderId(Long orderId) {
        return getOne(Wrappers.<OrderDetail>lambdaQuery().eq(OrderDetail::getOrderId, orderId));
    }
}
