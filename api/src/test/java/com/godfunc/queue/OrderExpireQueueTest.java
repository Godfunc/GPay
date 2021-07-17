package com.godfunc.queue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderExpireQueueTest {

    @Autowired
    private OrderExpireQueue orderExpireQueue;

    @Test
    public void seriTest() {
        OrderExpireQueue.OrderExpire orderExpire = new OrderExpireQueue.OrderExpire();
        orderExpire.setAmount(1000L);
        orderExpire.setId(1L);
        orderExpire.setCreateTime(LocalDateTime.now());
        orderExpire.setStatus(1);
        orderExpireQueue.push(orderExpire);

        OrderExpireQueue.OrderExpire pop = orderExpireQueue.pop(1L);
        System.out.println(pop);
    }

}
