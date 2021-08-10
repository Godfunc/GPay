package com.godfunc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.entity.Order;
import com.godfunc.entity.PayChannel;

import java.util.List;

/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
public interface PayChannelService extends IService<PayChannel> {
    List<PayChannel> getEnableByCategory(Long payCategoryId);

    List<PayChannel> getEnableByRisk(Long payCategoryId, Order order);
}
