package com.godfunc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.Order;
import com.godfunc.entity.PayChannel;
import com.godfunc.mapper.PayChannelMapper;
import com.godfunc.service.ChannelRiskService;
import com.godfunc.service.PayChannelService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
@Service
@RequiredArgsConstructor
public class PayChannelServiceImpl extends ServiceImpl<PayChannelMapper, PayChannel> implements PayChannelService {

    private final ChannelRiskService channelRiskService;

    @Override
    public List<PayChannel> getEnableByCategory(Long payCategoryId) {
        return this.baseMapper.selectEnableByCategory(payCategoryId);
    }

    @Override
    public List<PayChannel> getEnableByRisk(Long payCategoryId, Order order) {
        List<PayChannel> payChannelList = getEnableByCategory(payCategoryId);
        if (CollectionUtils.isEmpty(payChannelList)) {
            return null;
        } else {
            List<PayChannel> enableList = new ArrayList<>();
            for (int i = 0; i < payChannelList.size(); i++) {
                PayChannel payChannel = payChannelList.get(i);
                if (channelRiskService.risk(payChannel, order)) {
                    enableList.add(payChannel);
                }
            }
            return enableList;
        }
    }
}
