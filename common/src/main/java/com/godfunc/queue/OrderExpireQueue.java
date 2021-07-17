package com.godfunc.queue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;

@Slf4j
@Component
@AllArgsConstructor
public class OrderExpireQueue {

    private final RedisTemplate<Object, Object> redisTemplate;

    private final String KEY = "ORDER:EXPIRE:QUEUE:";

    public void push(OrderExpire orderExpire) {
        redisTemplate.opsForList().leftPush(KEY + orderExpire.getId(), orderExpire);
    }

    public OrderExpire pop(Long id) {
        return (OrderExpire) redisTemplate.opsForList().rightPop(KEY + id);
    }

    @Data
    public static class OrderExpire implements Serializable {
        private Long id;
        private Long amount;
        private Integer status;
        private LocalDateTime createTime;
        private LocalDateTime expiredTime;
    }

}
