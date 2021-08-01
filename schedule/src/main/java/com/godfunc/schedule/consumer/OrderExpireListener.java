package com.godfunc.schedule.consumer;

import com.godfunc.cache.ChannelRiskCache;
import com.godfunc.constant.QueueConstant;
import com.godfunc.entity.OrderLog;
import com.godfunc.enums.OrderStatusEnum;
import com.godfunc.enums.OrderStatusLogReasonEnum;
import com.godfunc.queue.model.OrderExpire;
import com.godfunc.schedule.service.OrderLogService;
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
    private final OrderLogService orderLogService;

    /**
     * 订单过期取消
     *
     * @param orderExpire
     */
    @Override
    public void onMessage(OrderExpire orderExpire) {
        boolean flag = orderService.expired(orderExpire.getId(), orderExpire.getStatus());
        if (flag) {
            if (orderExpire.getPayChannelId() != null) {
                channelRiskCache.divideTodayAmount(orderExpire.getPayChannelId(), orderExpire.getAmount());
            }
            if (orderExpire.getPayChannelAccountId() != null) {
                channelRiskCache.divideTodayAmount(orderExpire.getPayChannelAccountId(), orderExpire.getAmount());
            }
        }
        orderLogService.save(new OrderLog(orderExpire.getId(), orderExpire.getStatus(), OrderStatusEnum.EXPIRED.getValue(), OrderStatusLogReasonEnum.ORDER_DELAY_EXPIRED.getValue(), flag));
    }
}
