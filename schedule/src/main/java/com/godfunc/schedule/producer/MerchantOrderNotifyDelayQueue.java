package com.godfunc.schedule.producer;

import com.godfunc.constant.QueueConstant;
import com.godfunc.queue.model.OrderNotify;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MerchantOrderNotifyDelayQueue {

    private final RocketMQTemplate rocketMQTemplate;

    public void delayPush(OrderNotify orderNotify) {
        rocketMQTemplate.syncSend(QueueConstant.MERCHANT_NOTIFY_ORDER_TOPIC, new GenericMessage<>(orderNotify), QueueConstant.SYNC_TIME_OUT, orderNotify.getTime() + 1);
    }
}
