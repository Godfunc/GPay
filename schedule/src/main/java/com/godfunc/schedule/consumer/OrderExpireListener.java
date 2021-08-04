package com.godfunc.schedule.consumer;

import com.godfunc.constant.RabbitMQConstant;
import com.godfunc.entity.OrderLog;
import com.godfunc.enums.OrderStatusEnum;
import com.godfunc.enums.OrderStatusLogReasonEnum;
import com.godfunc.queue.model.OrderExpire;
import com.godfunc.schedule.service.OrderLogService;
import com.godfunc.schedule.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

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
    @RabbitListener(queues = RabbitMQConstant.DELAYED_ORDER_EXPIRE_QUEUE)
    public void onMessage(OrderExpire orderExpire) {
        boolean flag = orderService.expired(orderExpire.getId(), orderExpire.getStatus());
        orderLogService.save(new OrderLog(orderExpire.getId(), orderExpire.getStatus(), OrderStatusEnum.EXPIRED.getValue(), OrderStatusLogReasonEnum.ORDER_DELAY_EXPIRED.getValue(), flag));
    }
}
