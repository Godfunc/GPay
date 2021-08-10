package com.godfunc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.entity.Order;
import com.godfunc.entity.PayChannel;
import com.godfunc.entity.PayChannelAccount;
import com.godfunc.model.PayChannelAccountJoint;

import java.util.List;

/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
public interface PayChannelAccountService extends IService<PayChannelAccount> {
    PayChannelAccountJoint getEnableByWeight(List<PayChannel> payChannelList, Order order);
}
