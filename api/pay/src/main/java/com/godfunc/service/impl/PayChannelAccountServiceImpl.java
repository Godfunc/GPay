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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    public List<PayChannelAccount> getEnableByChannel(Long channelId) {
        return list(Wrappers.<PayChannelAccount>lambdaQuery()
                .eq(PayChannelAccount::getChannelId, channelId)
                .eq(PayChannelAccount::getStatus, PayChannelAccountStatusEnum.ENABLE.getValue())
                .isNotNull(PayChannelAccount::getAccountCode));
    }

    @Override
    public PayChannelAccountJoint getEnableByWeight(List<PayChannel> payChannelList, Order order) {
        // TODO 根据根据客户端类型进行渠道选择
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

    /**
     * 根据渠道子类获取可用的渠道账号列表
     *
     * @param payChannelList 渠道子类
     * @param order          订单
     * @return 渠道账号列表
     */
    public List<PayChannelAccount> getEnableByRisk(List<PayChannel> payChannelList, Order order) {
        Iterator<PayChannel> payChannelIterator = payChannelList.iterator();
        List<PayChannelAccount> payChannelAccountList = new ArrayList<>();
        // 迭代渠道子类列表
        while (payChannelIterator.hasNext()) {
            PayChannel payChannel = payChannelIterator.next();
            if (payChannel.getWeight() > 0) {
                // 根据渠道子类id查询可用的渠道账号列表
                List<PayChannelAccount> temp = getEnableByChannel(payChannel.getId());
                if (CollectionUtils.isNotEmpty(temp)
                        && CollectionUtils.isNotEmpty((
                        temp = temp.parallelStream()
                                .filter(account -> doRisk(account, order))
                                .collect(Collectors.toList())))) {
                    // 风控渠道账号列表，选取可用的渠道账号添加到列表
                    payChannelAccountList.addAll(temp);
                } else {
                    // 将没有渠道子账号
                    payChannelIterator.remove();
                }
            } else {
                // 丢弃渠道子类权重 <=0 的
                payChannelIterator.remove();
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
