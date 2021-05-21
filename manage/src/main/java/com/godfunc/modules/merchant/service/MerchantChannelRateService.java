package com.godfunc.modules.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.dto.PageDTO;
import com.godfunc.entity.MerchantChannelRate;
import com.godfunc.modules.merchant.dto.MerchantChannelRateDTO;
import com.godfunc.modules.merchant.dto.MerchantChannelSimpleRateDTO;
import com.godfunc.modules.merchant.param.MerchantChannelRateAddParam;
import com.godfunc.modules.merchant.param.MerchantChannelRateEditParam;
import com.godfunc.modules.merchant.param.MerchantChannelRateSaveParam;

import java.util.List;

public interface MerchantChannelRateService extends IService<MerchantChannelRate> {
    PageDTO<MerchantChannelRateDTO> getPage(Integer page, Integer limit, String merchantCode, String channelCode, String categoryCode);

    Long add(MerchantChannelRateAddParam param);

    Long edit(MerchantChannelRateEditParam param);

    boolean removeData(Long id);

    List<MerchantChannelSimpleRateDTO> listByMerchant(String merchantCode);

    boolean saveData(MerchantChannelRateSaveParam param);
}
