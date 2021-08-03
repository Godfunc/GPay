package com.godfunc.queue;

import com.godfunc.producer.OrderExpireQueue;
import com.godfunc.queue.model.OrderExpire;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;


@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderExpireQueueTest {

    @Autowired
    private OrderExpireQueue orderExpireQueue;
    @Test
    public void test1() {
        OrderExpire orderExpire = new OrderExpire();
        orderExpire.setId(13L);
        orderExpire.setAmount(100L);
        orderExpire.setStatus(1);
        orderExpire.setPayChannelId(101L);
        orderExpire.setPayChannelAccountId(102L);
        orderExpireQueue.push(orderExpire);
    }
}
