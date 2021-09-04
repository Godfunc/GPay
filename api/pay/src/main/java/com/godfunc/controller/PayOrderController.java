package com.godfunc.controller;

import com.godfunc.service.CreateOrderService;
import com.godfunc.dto.PayOrderDTO;
import com.godfunc.param.PayOrderParam;
import com.godfunc.pay.PayOrderService;
import com.godfunc.result.R;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class PayOrderController {

    private final CreateOrderService createOrderService;
    private final PayOrderService payOrderService;

    @PostMapping(value = "pay", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public R<PayOrderDTO> pay(@Valid PayOrderParam param, HttpServletRequest request) {
        return R.ok(createOrderService.create(param, request));
    }

    @PostMapping(value = "pay", produces = MediaType.TEXT_HTML_VALUE)
    public void pay(@Valid PayOrderParam param, HttpServletRequest request, HttpServletResponse response) {
        createOrderService.create(param, request, response);
    }

    @PostMapping("goPay/{orderNo}")
    public void goPay(@PathVariable String orderNo, HttpServletRequest request, HttpServletResponse response) {
        payOrderService.goPay(orderNo, request, response);
    }
}
