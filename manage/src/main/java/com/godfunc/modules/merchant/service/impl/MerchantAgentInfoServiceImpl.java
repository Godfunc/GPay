package com.godfunc.modules.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.MerchantAgentInfo;
import com.godfunc.modules.merchant.mapper.MerchantAgentInfoMapper;
import com.godfunc.modules.merchant.service.MerchantAgentInfoService;
import org.springframework.stereotype.Service;

@Service
public class MerchantAgentInfoServiceImpl extends ServiceImpl<MerchantAgentInfoMapper, MerchantAgentInfo> implements MerchantAgentInfoService {
}
