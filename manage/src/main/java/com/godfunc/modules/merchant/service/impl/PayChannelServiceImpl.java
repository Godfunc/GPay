package com.godfunc.modules.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.PayChannel;
import com.godfunc.modules.merchant.mapper.PayChannelMapper;
import com.godfunc.modules.merchant.service.PayChannelService;
import org.springframework.stereotype.Service;

@Service
public class PayChannelServiceImpl extends ServiceImpl<PayChannelMapper, PayChannel> implements PayChannelService {
}
