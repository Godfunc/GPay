package com.godfunc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.entity.MerchantRisk;
import com.godfunc.entity.Order;

import java.util.List;

/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
public interface MerchantRiskService extends IService<MerchantRisk> {
    List<MerchantRisk> getByMerchant(Long merchantId);

    boolean riskMerchant(Long merchantId, Order order);
}
