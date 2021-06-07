package com.godfunc.controller;

import com.godfunc.dto.PayOrderDTO;
import com.godfunc.param.PayOrderParam;
import com.godfunc.result.R;
import com.godfunc.service.PayOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class PayOrderController {

    private final PayOrderService payOrderService;

    @PostMapping("pay")
    public R<PayOrderDTO> pay(PayOrderParam param, HttpServletRequest request, HttpServletResponse response) {
        return R.ok(payOrderService.create(param, request, response));
    }
}
