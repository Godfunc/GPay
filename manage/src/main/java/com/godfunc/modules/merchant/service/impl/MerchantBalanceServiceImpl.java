package com.godfunc.modules.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.MerchantBalance;
import com.godfunc.modules.merchant.mapper.MerchantBalanceMapper;
import com.godfunc.modules.merchant.service.MerchantBalanceService;
import org.springframework.stereotype.Service;

@Service
public class MerchantBalanceServiceImpl extends ServiceImpl<MerchantBalanceMapper, MerchantBalance> implements MerchantBalanceService {
}
