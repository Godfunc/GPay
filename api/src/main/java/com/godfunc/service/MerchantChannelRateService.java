package com.godfunc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.entity.MerchantChannelRate;

/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
public interface MerchantChannelRateService extends IService<MerchantChannelRate> {
    MerchantChannelRate getByMerchant(String code, Long payCategoryChannelId);
}
