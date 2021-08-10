package com.godfunc.consumer;

import com.godfunc.constant.RabbitMQConstant;
import com.godfunc.entity.OrderLog;
import com.godfunc.enums.OrderStatusEnum;
import com.godfunc.enums.OrderStatusLogReasonEnum;
import com.godfunc.queue.model.OrderExpire;
import com.godfunc.service.OrderLogService;
import com.godfunc.service.OrderService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderExpireListener {

    private final OrderService orderService;
    private final OrderLogService orderLogService;

    /**
     * 订单过期取消
     *
     * @param orderExpire
     */
    @RabbitListener(queues = RabbitMQConstant.DelayOrderExpire.DELAYED_ORDER_EXPIRE_QUEUE, ackMode = "MANUAL")
    public void onMessage(OrderExpire orderExpire, Message message, Channel channel) {
        try {
            boolean flag = orderService.expired(orderExpire.getId(), OrderStatusEnum.CREATED.getValue(), OrderStatusEnum.SCAN.getValue());
            orderLogService.save(new OrderLog(orderExpire.getId(), orderExpire.getStatus(), OrderStatusEnum.EXPIRED.getValue(), OrderStatusLogReasonEnum.ORDER_DELAY_EXPIRED.getValue(), flag));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            log.error("应答异常 orderExpire={}，exchange={}, routingKey={} {}",
                    orderExpire,
                    message.getMessageProperties().getReceivedExchange(),
                    message.getMessageProperties().getReceivedRoutingKey(),
                    e);
        }
    }
}
