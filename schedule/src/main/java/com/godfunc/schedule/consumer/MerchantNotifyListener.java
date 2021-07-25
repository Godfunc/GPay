package com.godfunc.schedule.consumer;

import com.godfunc.constant.QueueConstant;
import com.godfunc.queue.model.OrderNotify;
import com.godfunc.schedule.producer.MerchantOrderNotifyDelayQueue;
import com.godfunc.schedule.service.OrderService;
import com.godfunc.service.NotifyMerchantService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@RocketMQMessageListener(topic = QueueConstant.MERCHANT_NOTIFY_ORDER_TOPIC,
        consumerGroup = "${rocketmq.consumer.group}")
public class MerchantNotifyListener implements RocketMQListener<OrderNotify> {

    private final OrderService orderService;
    private final MerchantOrderNotifyDelayQueue merchantOrderNotifyDelayQueue;
    private final NotifyMerchantService notifyMerchantService;

    /**
     * 通知商户完成订单
     *
     * @param orderNotify
     */
    @Override
    public void onMessage(OrderNotify orderNotify) {
        boolean flag = notifyMerchantService.notifyMerchant(
                orderNotify.getNotifyUrl(), orderNotify.getOutTradeNo(),
                orderNotify.getOrderNo(), orderNotify.getAmount(), orderNotify.getRealAmount(),
                orderNotify.getPayType(), orderNotify.getStatus(),
                orderNotify.getPlatPrivateKey());
        if (flag) {
            orderService.updateFinish(orderNotify);
        } else {
            orderNotify.setTime(orderNotify.getTime() + 1);
            merchantOrderNotifyDelayQueue.delayPush(orderNotify);
        }
    }
}
