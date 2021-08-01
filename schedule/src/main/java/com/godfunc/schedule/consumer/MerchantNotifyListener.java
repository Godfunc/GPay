package com.godfunc.schedule.consumer;

import com.godfunc.constant.QueueConstant;
import com.godfunc.entity.OrderLog;
import com.godfunc.enums.OrderStatusEnum;
import com.godfunc.enums.OrderStatusLogReasonEnum;
import com.godfunc.queue.model.OrderNotify;
import com.godfunc.schedule.producer.MerchantOrderNotifyDelayQueue;
import com.godfunc.schedule.service.OrderLogService;
import com.godfunc.schedule.service.OrderService;
import com.godfunc.service.NotifyMerchantService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@RocketMQMessageListener(topic = QueueConstant.MERCHANT_NOTIFY_ORDER_TOPIC,
        consumerGroup = QueueConstant.MERCHANT_NOTIFY_ORDER_GROUP)
public class MerchantNotifyListener implements RocketMQListener<OrderNotify> {

    private final OrderService orderService;
    private final MerchantOrderNotifyDelayQueue merchantOrderNotifyDelayQueue;
    private final NotifyMerchantService notifyMerchantService;
    private final OrderLogService orderLogService;

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
            boolean orderFlag = orderService.updateFinish(orderNotify);
            orderLogService.save(new OrderLog(orderNotify.getId(), OrderStatusEnum.PAID.getValue(), OrderStatusEnum.FINISH.getValue(), OrderStatusLogReasonEnum.NOTIFY_MERCHANT.getValue(), orderFlag));
        } else {
            orderNotify.setTime(orderNotify.getTime() + 1);
            merchantOrderNotifyDelayQueue.delayPush(orderNotify);
        }
    }
}
