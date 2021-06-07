package com.godfunc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.ChannelRisk;
import com.godfunc.entity.Order;
import com.godfunc.entity.PayChannel;
import com.godfunc.entity.PayChannelAccount;
import com.godfunc.enums.PayChannelAccountRiskTypeEnum;
import com.godfunc.enums.PayChannelAccountStatusEnum;
import com.godfunc.exception.GException;
import com.godfunc.mapper.PayChannelAccountMapper;
import com.godfunc.model.PayChannelAccountJoint;
import com.godfunc.service.ChannelRiskService;
import com.godfunc.service.PayChannelAccountService;
import com.godfunc.util.Assert;
import com.godfunc.util.WeightRandomUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayChannelAccountServiceImpl extends ServiceImpl<PayChannelAccountMapper, PayChannelAccount> implements PayChannelAccountService {

    private final ChannelRiskService channelRiskService;

    public List<PayChannelAccount> getEnableByChannel(Set<Long> channelIds) {
        return list(Wrappers.<PayChannelAccount>lambdaQuery()
                .in(PayChannelAccount::getChannelId, channelIds)
                .eq(PayChannelAccount::getStatus, PayChannelAccountStatusEnum.ENABLE.getValue())
                .isNotNull(PayChannelAccount::getAccountCode));
    }

    @Override
    public PayChannelAccountJoint getEnableByWeight(List<PayChannel> payChannelList, Order order) {
        List<PayChannelAccount> payChannelAccountList = getEnableByRisk(payChannelList, order);
        if (CollectionUtils.isEmpty(payChannelList)) {
            log.error("渠道中的权重为0或者没有渠道账号 {}", payChannelList);
            throw new GException("没有可用的支付渠道");
        }
        log.info("待选择的渠道列表为 {}", payChannelList);

        PayChannel payChannel = payChannelList.get(WeightRandomUtils.getByWeight(payChannelList));
        Assert.isNull(payChannel, "没有可用的支付渠道");
        log.info("当前选择的支付渠道为{}", payChannel);

        payChannelAccountList = payChannelAccountList.parallelStream().filter(x -> x.getChannelId().equals(payChannel.getId())).collect(Collectors.toList());
        log.info("待选择的渠道账号列表为 {}", payChannelAccountList);

        return new PayChannelAccountJoint(payChannel, payChannelAccountList.get(WeightRandomUtils.getByWeight(payChannelAccountList)));
    }

    public List<PayChannelAccount> getEnableByRisk(List<PayChannel> payChannelList, Order order) {
        Iterator<PayChannel> payChannelIterator = payChannelList.iterator();
        List<PayChannelAccount> payChannelAccountList = new ArrayList<>();
        while (payChannelIterator.hasNext()) {
            PayChannel payChannel = payChannelIterator.next();
            if (payChannel.getWeight() > 0) {
                List<PayChannelAccount> temp = getEnableByChannel(payChannelList.stream().map(PayChannel::getId).collect(Collectors.toSet()));
                if (CollectionUtils.isNotEmpty(temp)) {
                    payChannelAccountList.addAll(temp.parallelStream().filter(account -> doRisk(account, order)).collect(Collectors.toList()));
                } else {
                    payChannelList.remove(payChannel);
                }
            } else {
                payChannelList.remove(payChannel);
            }
        }
        return payChannelAccountList;
    }

    public boolean doRisk(PayChannelAccount payChannelAccount, Order order) {
        List<ChannelRisk> riskList = getRisk(payChannelAccount);
        if (CollectionUtils.isEmpty(riskList)) {
            return true;
        } else {
            for (int i = 0; i < riskList.size(); i++) {
                if (!channelRiskService.doRisk(riskList.get(i), order)) {
                    return false;
                }
            }
            return true;
        }
    }

    public List<ChannelRisk> getRisk(PayChannelAccount payChannelAccount) {
        if (payChannelAccount.getRiskType() == PayChannelAccountRiskTypeEnum.NONE.getValue()) {
            return null;
        } else {
            return channelRiskService.getRisk(payChannelAccount);
        }
    }


}
