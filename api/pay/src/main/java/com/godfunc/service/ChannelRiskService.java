package com.godfunc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.entity.ChannelRisk;
import com.godfunc.entity.Order;
import com.godfunc.entity.PayChannel;
import com.godfunc.entity.PayChannelAccount;

import java.util.List;

/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
public interface ChannelRiskService extends IService<ChannelRisk> {
    boolean risk(PayChannel payChannel, Order order);

    List<ChannelRisk> getRisk(PayChannelAccount payChannelAccount);

    List<ChannelRisk> getRisk(PayChannel payChannelAccount);

    boolean doRisk(ChannelRisk channelRisk, Order order);
}
