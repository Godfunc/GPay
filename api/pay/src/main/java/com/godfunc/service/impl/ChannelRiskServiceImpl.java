package com.godfunc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.cache.ChannelRiskCache;
import com.godfunc.constant.CommonConstant;
import com.godfunc.entity.ChannelRisk;
import com.godfunc.entity.Order;
import com.godfunc.entity.PayChannel;
import com.godfunc.entity.PayChannelAccount;
import com.godfunc.enums.ChannelRiskStatusEnum;
import com.godfunc.enums.PayChannelAccountRiskTypeEnum;
import com.godfunc.mapper.ChannelRiskMapper;
import com.godfunc.service.ChannelRiskService;
import com.godfunc.util.AmountUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;


/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelRiskServiceImpl extends ServiceImpl<ChannelRiskMapper, ChannelRisk> implements ChannelRiskService {

    private final ChannelRiskCache channelRiskCache;


    @Override
    public List<ChannelRisk> getRisk(PayChannel payChannel) {
        return list(Wrappers.<ChannelRisk>lambdaQuery()
                .eq(ChannelRisk::getChannelId, payChannel.getId())
                .eq(ChannelRisk::getStatus, ChannelRiskStatusEnum.ENABLE.getValue())
                .isNull(ChannelRisk::getChannelAccountId));

    }

    @Override
    public List<ChannelRisk> getRisk(PayChannelAccount payChannelAccount) {
        return list(Wrappers.<ChannelRisk>lambdaQuery()
                .eq(ChannelRisk::getChannelId, payChannelAccount.getChannelId())
                .eq(ChannelRisk::getStatus, ChannelRiskStatusEnum.ENABLE.getValue())
                .eq(payChannelAccount.getRiskType() == PayChannelAccountRiskTypeEnum.CUSTOMIZE.getValue(),
                        ChannelRisk::getChannelAccountId, payChannelAccount.getId()));

    }

    @Override
    public boolean risk(PayChannel payChannel, Order order) {
        List<ChannelRisk> list = getRisk(payChannel);
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        for (int i = 0; i < list.size(); i++) {
            if (!doRisk(list.get(i), order)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean doRisk(ChannelRisk channelRisk, Order order) {
        // 时间
        LocalTime now = LocalTime.now();
        if (channelRisk.getDayStartTime() != null && !now.isAfter(channelRisk.getDayStartTime())) {
            return false;
        }
        if (channelRisk.getDayEndTime() != null && !now.isBefore(channelRisk.getDayEndTime())) {
            return false;
        }
        // 单笔限额
        if (channelRisk.getOneAmountMin() != null && channelRisk.getOneAmountMin() > order.getAmount()) {
            return false;
        }
        if (channelRisk.getOneAmountMax() != null && channelRisk.getOneAmountMax() < order.getAmount()) {
            return false;
        }
        if (StringUtils.isNotBlank(channelRisk.getOneAmount()) && Arrays.stream(channelRisk.getOneAmount().
                split(CommonConstant.ONE_AMOUNT_SPLIT))
                .mapToLong(AmountUtil::convertDollar2Cent)
                .noneMatch(a -> order.getAmount().equals(a))) {
            return false;
        }

        // 每日最大
        if (channelRisk.getDayAmountMax() != null
                && channelRisk.getDayAmountMax() <
                // 从redis获取当前渠道风控的当日使用金额 + 订单金额
                channelRiskCache.getTodayAmount(channelRisk.getChannelId()) + order.getAmount()) {
            return false;
        }
        return true;
    }
}
