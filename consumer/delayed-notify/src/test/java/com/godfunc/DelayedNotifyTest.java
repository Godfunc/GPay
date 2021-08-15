package com.godfunc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@RefreshScope
public class DelayedNotifyTest {
    @Value("${delayNotifySecondArray}")
    private Integer[] delayNotifySecondArray;

    @Test
    public void test1() throws InterruptedException {
        System.out.println(Arrays.toString(delayNotifySecondArray));

        TimeUnit.SECONDS.sleep(30);
        System.out.println(Arrays.toString(delayNotifySecondArray));


    }
}
