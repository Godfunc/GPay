package com.godfunc.modules.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.PayChannelAccount;
import com.godfunc.modules.merchant.mapper.PayChannelAccountMapper;
import com.godfunc.modules.merchant.service.PayChannelAccountService;
import org.springframework.stereotype.Service;

@Service
public class PayChannelAccountServiceImpl extends ServiceImpl<PayChannelAccountMapper, PayChannelAccount> implements PayChannelAccountService {
}
