package com.godfunc.producer;

import com.godfunc.constant.QueueConstant;
import com.godfunc.queue.model.OrderExpire;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class OrderExpireQueue {

    private final RocketMQTemplate rocketMQTemplate;

    public void push(OrderExpire orderExpire) {
        rocketMQTemplate.syncSend(QueueConstant.EXPIRE_ORDER_TOPIC,
                new GenericMessage<>(orderExpire),
                QueueConstant.SYNC_TIME_OUT, orderExpire.getExpireLevel());
    }
}
