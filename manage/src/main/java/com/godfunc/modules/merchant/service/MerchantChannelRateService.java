package com.godfunc.modules.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.entity.MerchantChannelRate;
import com.godfunc.modules.merchant.dto.MerchantChannelSimpleRateDTO;
import com.godfunc.modules.merchant.param.MerchantChannelRateSaveParam;

import java.util.List;

public interface MerchantChannelRateService extends IService<MerchantChannelRate> {

    boolean removeData(Long id);

    List<MerchantChannelSimpleRateDTO> listByMerchant(String merchantCode);

    boolean saveData(MerchantChannelRateSaveParam param);
}
