package com.godfunc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.MerchantRisk;
import com.godfunc.entity.Order;
import com.godfunc.enums.MerchantRiskStatusEnum;
import com.godfunc.exception.GException;
import com.godfunc.mapper.MerchantRiskMapper;
import com.godfunc.service.MerchantRiskService;
import com.godfunc.util.AmountUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;


/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
@Service
@RequiredArgsConstructor
public class MerchantRiskServiceImpl extends ServiceImpl<MerchantRiskMapper, MerchantRisk> implements MerchantRiskService {
    @Override
    public List<MerchantRisk> getByMerchant(Long merchantId) {
        return list(Wrappers.<MerchantRisk>lambdaQuery()
                .eq(MerchantRisk::getMerchantId, merchantId)
                .eq(MerchantRisk::getStatus, MerchantRiskStatusEnum.ENABLE.getValue()));
    }

    @Override
    public boolean riskMerchant(Long merchantId, Order order) {
        List<MerchantRisk> merchantRiskList = getByMerchant(merchantId);
        if (CollectionUtils.isEmpty(merchantRiskList)) {
            return true;
        } else {
            for (int i = 0; i < merchantRiskList.size(); i++) {
                MerchantRisk merchantRisk = merchantRiskList.get(i);
                if (!doRiskMerchant(merchantRisk, order)) {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean doRiskMerchant(MerchantRisk merchantRisk, Order order) {
        LocalTime now = LocalTime.now();
        // 时间
        if (merchantRisk.getDayStartTime() != null && !now.isAfter(merchantRisk.getDayStartTime())) {
            throw new GException("商户还未到交易开始时间");
        }
        if (merchantRisk.getDayEndTime() != null && !now.isBefore(merchantRisk.getDayEndTime())) {
            throw new GException("商户交易时间已结束");
        }

        // 单笔
        if (merchantRisk.getOneAmountMin() != null && merchantRisk.getOneAmountMin() > order.getAmount()) {
            throw new GException("订单金额不能小于{}元", AmountUtil.convertCent2Dollar(merchantRisk.getOneAmountMin()));
        }
        if (merchantRisk.getOneAmountMax() != null && merchantRisk.getOneAmountMax() < order.getAmount()) {
            throw new GException("订单金额不能大于{}元", AmountUtil.convertCent2Dollar(merchantRisk.getOneAmountMax()));
        }
        return true;
    }
}
