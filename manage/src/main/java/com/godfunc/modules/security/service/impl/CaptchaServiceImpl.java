package com.godfunc.modules.security.service.impl;

import com.godfunc.modules.security.service.CaptchaService;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

/**
 * @author Godfunc
 * @date 2019/12/1 21:59
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {

    private final Producer producer;
    private final StringRedisTemplate redisTemplate;

    public CaptchaServiceImpl(Producer producer, StringRedisTemplate redisTemplate) {
        this.producer = producer;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 有需要可以替换成redis
     */

    @Override
    public BufferedImage create(String uuid) {
        String code = producer.createText();
        redisTemplate.opsForValue().set(uuid, code, 5, TimeUnit.MINUTES);
        return producer.createImage(code);
    }

    @Override
    public boolean validate(String uuid, String code) {
        String text = redisTemplate.opsForValue().get(uuid);
        if (StringUtils.isNotBlank(text)) {
            redisTemplate.delete(uuid);
            return code.equalsIgnoreCase(text);
        }
        return false;
    }
}
