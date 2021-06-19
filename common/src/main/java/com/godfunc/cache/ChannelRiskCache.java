package com.godfunc.cache;

import com.godfunc.constant.CommonConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ChannelRiskCache {

    private final StringRedisTemplate redisTemplate;

    /**
     * 在渠道风控时，检查当日交易限额
     *
     * @param id
     * @return
     */
    public long getTodayAmount(Long id) {
        String s = redisTemplate.opsForValue().get(id + ":" + LocalDate.now().format(DateTimeFormatter.ofPattern(CommonConstant.DATE_FORMAT)));
        if (s != null) {
            return Long.parseLong(s);
        } else {
            return 0L;
        }
    }

    /**
     * 在请求支付前增加今日交易金额
     *
     * @param id
     * @param amount
     * @return
     */
    public Long addTodayAmount(Long id, Long amount) {
        return redisTemplate.opsForValue().increment(id + ":" + LocalDate.now().format(DateTimeFormatter.ofPattern(CommonConstant.DATE_FORMAT)), amount);
    }

    public Long divideAmount(Long id, Long amount) {
        return redisTemplate.opsForValue().decrement(id + ":" + LocalDate.now().format(DateTimeFormatter.ofPattern(CommonConstant.DATE_FORMAT)), amount);
    }
}
