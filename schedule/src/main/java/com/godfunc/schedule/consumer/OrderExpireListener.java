package com.godfunc.schedule.consumer;

import com.godfunc.cache.ChannelRiskCache;
import com.godfunc.constant.QueueConstant;
import com.godfunc.queue.model.OrderExpire;
import com.godfunc.schedule.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@RocketMQMessageListener(topic = QueueConstant.EXPIRE_ORDER_TOPIC,
        consumerGroup = QueueConstant.EXPIRE_ORDER_GROUP)
public class OrderExpireListener implements RocketMQListener<OrderExpire> {

    private final OrderService orderService;
    private final ChannelRiskCache channelRiskCache;

    /**
     * 订单过期取消
     *
     * @param orderExpire
     */
    @Override
    public void onMessage(OrderExpire orderExpire) {
        if (orderService.expired(orderExpire.getId(), orderExpire.getStatus())) {
            if (orderExpire.getPayChannelId() != null) {
                channelRiskCache.divideTodayAmount(orderExpire.getPayChannelId(), orderExpire.getAmount());
            }
            if (orderExpire.getPayChannelAccountId() != null) {
                channelRiskCache.divideTodayAmount(orderExpire.getPayChannelAccountId(), orderExpire.getAmount());
            }
        }
    }
}
