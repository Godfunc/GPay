package com.godfunc.schedule.consumer;

import com.godfunc.constant.RabbitMQConstant;
import com.godfunc.entity.OrderLog;
import com.godfunc.enums.OrderStatusEnum;
import com.godfunc.enums.OrderStatusLogReasonEnum;
import com.godfunc.queue.model.OrderNotify;
import com.godfunc.schedule.producer.MerchantOrderNotifyDelayQueue;
import com.godfunc.schedule.service.OrderLogService;
import com.godfunc.schedule.service.OrderService;
import com.godfunc.service.NotifyMerchantService;
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
public class MerchantNotifyListener {

    private final OrderService orderService;
    private final MerchantOrderNotifyDelayQueue merchantOrderNotifyDelayQueue;
    private final NotifyMerchantService notifyMerchantService;
    private final OrderLogService orderLogService;

    /**
     * 通知商户完成订单
     *
     * @param orderNotify
     */
    @RabbitListener(queues = RabbitMQConstant.MerchantNotifyOrder.MERCHANT_NOTIFY_ORDER_QUEUE, ackMode = "MANUAL")
    public void onMessage(OrderNotify orderNotify, Message message, Channel channel) {
        try {
            boolean flag = notifyMerchantService.notifyMerchant(
                    orderNotify.getNotifyUrl(), orderNotify.getOutTradeNo(),
                    orderNotify.getOrderNo(), orderNotify.getAmount(), orderNotify.getRealAmount(),
                    orderNotify.getPayType(), orderNotify.getStatus(),
                    orderNotify.getPlatPrivateKey());
            if (flag) {
                boolean orderFlag = orderService.updateFinish(orderNotify);
                orderLogService.save(new OrderLog(orderNotify.getId(), OrderStatusEnum.PAID.getValue(), OrderStatusEnum.FINISH.getValue(), OrderStatusLogReasonEnum.NOTIFY_MERCHANT.getValue(), orderFlag));
            } else {
                if (orderNotify.getTime() <= 5) {
                    orderNotify.setTime(orderNotify.getTime() + 1);
                    merchantOrderNotifyDelayQueue.delayPush(orderNotify);
                }
            }
        } catch (Exception e) {
            log.error("通知商户异常", e);
        }
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            log.error("应答异常 orderNotify={}，exchange={}, routingKey={} {}",
                    orderNotify,
                    message.getMessageProperties().getReceivedExchange(),
                    message.getMessageProperties().getReceivedRoutingKey(),
                    e);
        }
    }
}
