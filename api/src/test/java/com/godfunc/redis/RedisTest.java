package com.godfunc.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Test
    public void test1(){
        redisTemplate.opsForValue().set(1L, 2L);
        Long o = (Long)redisTemplate.opsForValue().get(1);
        System.out.println(o);
    }
}
