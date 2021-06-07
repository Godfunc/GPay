package com.godfunc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.MerchantChannelRate;
import com.godfunc.mapper.MerchantChannelRateMapper;
import com.godfunc.service.MerchantChannelRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
@Service
@RequiredArgsConstructor
public class MerchantChannelRateServiceImpl extends ServiceImpl<MerchantChannelRateMapper, MerchantChannelRate> implements MerchantChannelRateService {

    @Override
    public MerchantChannelRate getByMerchant(String merchantCode, Long payCategoryChannelId) {
        return getOne(Wrappers.<MerchantChannelRate>lambdaQuery()
                .eq(MerchantChannelRate::getMerchantCode, merchantCode)
                .eq(MerchantChannelRate::getCategoryChannelId, payCategoryChannelId));
    }
}
