package com.godfunc.modules.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.entity.MerchantRisk;
import com.godfunc.modules.merchant.dto.MerchantRiskDTO;
import com.godfunc.modules.merchant.param.MerchantRiskAddParam;
import com.godfunc.modules.merchant.param.MerchantRiskEditParam;

import java.util.List;

public interface MerchantRiskService extends IService<MerchantRisk> {

    List<MerchantRiskDTO> getList(String merchantCode);

    Long add(MerchantRiskAddParam param);

    Long edit(MerchantRiskEditParam param);

    boolean removeData(Long id);
}
