package com.godfunc.modules.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.godfunc.entity.MerchantChannelRate;
import com.godfunc.modules.merchant.dto.MerchantChannelRateDTO;
import com.godfunc.modules.merchant.dto.MerchantChannelSimpleRateDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MerchantChannelRateMapper extends BaseMapper<MerchantChannelRate> {
    List<MerchantChannelRateDTO> selectCustomPage(IPage resultPage,
                                                  @Param("merchantCode") String merchantCode,
                                                  @Param("channelCode") String channelCode,
                                                  @Param("categoryCode") String categoryCode);

    List<MerchantChannelSimpleRateDTO> selectListByMerchantCode(@Param("merchantCode") String merchantCode);
}
