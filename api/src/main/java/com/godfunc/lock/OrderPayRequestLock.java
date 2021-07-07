package com.godfunc.lock;

import com.godfunc.constant.ApiConstant;
import com.godfunc.constant.CommonConstant;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class OrderPayRequestLock {

    private final StringRedisTemplate redisTemplate;

    public Boolean isLock(Long orderId) {
        Boolean flag = redisTemplate.opsForValue().setIfAbsent(orderId.toString(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(CommonConstant.DATETIME_FORMAT)),
                Duration.ofMinutes(ApiConstant.ORDER_PAY_REQUEST_LOCK_EXPIRE));
        if (flag == null || !flag) {
            return true;
        } else {
            return false;
        }
    }

    public void rmLock(Long orderId) {
        redisTemplate.delete(orderId.toString());
    }
}
