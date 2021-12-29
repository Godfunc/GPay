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
        // 查询商户的可用风控数据列表
        List<MerchantRisk> merchantRiskList = getByMerchant(merchantId);
        if (CollectionUtils.isEmpty(merchantRiskList)) {
            return true;
        } else {
            for (int i = 0; i < merchantRiskList.size(); i++) {
                MerchantRisk merchantRisk = merchantRiskList.get(i);
                // 进行风控
                if (!doRiskMerchant(merchantRisk, order)) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * 进行商户风控，对使用时间段、单笔金额等进行风控，不符合风控规则就抛出异常或者返回false
     * 如果是抛出异常，将会直接返回给客户端
     *
     * @param merchantRisk 风控规则
     * @param order        订单
     * @return true表示未被风控 false或者异常表示被风控了
     */
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
