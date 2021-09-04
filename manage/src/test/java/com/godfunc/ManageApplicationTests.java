package com.godfunc;

import com.godfunc.param.PayOrderParam;
import com.godfunc.service.CreateOrderService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class ManageApplicationTests {

    @DubboReference
    private CreateOrderService createOrderService;

    @Test
    void contextLoads() {
        PayOrderParam payOrderParam = new PayOrderParam();
        createOrderService.create(payOrderParam, new MockHttpServletRequest(), new MockHttpServletResponse());
    }

}
