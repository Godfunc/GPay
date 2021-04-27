package com.godfunc.modules.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.MerchantRisk;
import com.godfunc.modules.merchant.mapper.MerchantRiskMapper;
import com.godfunc.modules.merchant.service.MerchantRiskService;
import org.springframework.stereotype.Service;

@Service
public class MerchantRiskServiceImpl extends ServiceImpl<MerchantRiskMapper,MerchantRisk> implements MerchantRiskService {
}
