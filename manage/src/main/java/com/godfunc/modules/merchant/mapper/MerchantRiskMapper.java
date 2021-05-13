package com.godfunc.modules.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.godfunc.entity.MerchantRisk;
import com.godfunc.modules.merchant.dto.MerchantRiskDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MerchantRiskMapper extends BaseMapper<MerchantRisk> {
    List<MerchantRiskDTO> selectMerchantList(@Param("merchantCode") String merchantCode);
}
