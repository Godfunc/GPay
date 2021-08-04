package com.godfunc.schedule.consumer;

import com.godfunc.cache.ChannelRiskCache;
import com.godfunc.constant.RabbitMQConstant;
import com.godfunc.entity.Order;
import com.godfunc.enums.OrderStatusEnum;
import com.godfunc.queue.model.FixChannelRisk;
import com.godfunc.schedule.service.OrderService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class FixChannelRiskListener {

    private final OrderService orderService;
    private final ChannelRiskCache channelRiskCache;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String PREFIX = "CHECK:CHANNEL_RISK:";

    /**
     * 渠道风控修复：订单过期未支付
     *
     * @param fixChannelRisk
     */
    @RabbitListener(queues = RabbitMQConstant.DelayFixChannelRisk.DELAYED_FIX_CHANNEL_RISK_QUEUE, ackMode = "MANUAL")
    public void onMessage(FixChannelRisk fixChannelRisk, Message message, Channel channel) {
        try {
            Order order = orderService.getById(fixChannelRisk.getId());
            if (order != null && check(order.getId()) && (order.getStatus() == OrderStatusEnum.SCAN.getValue() || order.getStatus() == OrderStatusEnum.EXPIRED.getValue())) {
                if (fixChannelRisk.getPayChannelId() != null) {
                    channelRiskCache.divideTodayAmount(fixChannelRisk.getPayChannelId(), fixChannelRisk.getAmount());
                }
                if (fixChannelRisk.getPayChannelAccountId() != null) {
                    channelRiskCache.divideTodayAmount(fixChannelRisk.getPayChannelAccountId(), fixChannelRisk.getAmount());
                }
            }
        } catch (Exception e) {
            log.error("渠道风控金额恢复处理异常", e);
        }
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            log.error("应答异常 fixChannelRisk={}，exchange={}, routingKey={} {}",
                    fixChannelRisk,
                    message.getMessageProperties().getReceivedExchange(),
                    message.getMessageProperties().getReceivedRoutingKey(),
                    e);
        }
    }

    /**
     * 防止消息重复
     *
     * @param id
     * @return
     */
    public Boolean check(Long id) {
        // TODO 需要定期清除数据
        return redisTemplate.opsForValue().setIfAbsent(PREFIX + id, id);
    }
}
