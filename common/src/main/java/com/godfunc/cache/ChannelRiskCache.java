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

    private StringRedisTemplate redisTemplate;

    /**
     * 在渠道风控时，检查当日交易限额
     *
     * @param channelId
     * @return
     */
    public long getTodayAmount(Long channelId) {
        String s = redisTemplate.opsForValue().get(channelId + ":" + LocalDate.now().format(DateTimeFormatter.ofPattern(CommonConstant.DATE_FORMAT)));
        if (s != null) {
            return Long.parseLong(s);
        } else {
            return 0L;
        }
    }

    /**
     * 在请求支付前增加今日交易金额
     *
     * @param channelId
     * @param amount
     * @return
     */
    public Long addTodayAmount(Long channelId, Long amount) {
        return redisTemplate.opsForValue().increment(channelId + ":" + LocalDate.now().format(DateTimeFormatter.ofPattern(CommonConstant.DATE_FORMAT)), amount);
    }
}
