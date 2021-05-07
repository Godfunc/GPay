package com.godfunc.modules.merchant.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.ChannelRisk;
import com.godfunc.modules.merchant.mapper.ChannelRiskMapper;
import com.godfunc.modules.merchant.service.ChannelRiskService;
import org.springframework.stereotype.Service;

@Service
public class ChannelRiskServiceImpl extends ServiceImpl<ChannelRiskMapper, ChannelRisk> implements ChannelRiskService {
    @Override
    public boolean removeByChannelAccountId(Long channelAccountId) {
        return remove(Wrappers.<ChannelRisk>lambdaQuery().eq(ChannelRisk::getChannelAccountId, channelAccountId));
    }
}
