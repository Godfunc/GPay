package com.godfunc.modules.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.godfunc.entity.MerchantChannelRate;
import com.godfunc.modules.merchant.dto.MerchantChannelSimpleRateDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MerchantChannelRateMapper extends BaseMapper<MerchantChannelRate> {

    List<MerchantChannelSimpleRateDTO> selectListByMerchantCode(@Param("merchantCode") String merchantCode);
}
