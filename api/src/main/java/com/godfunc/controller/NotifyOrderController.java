package com.godfunc.controller;

import com.godfunc.notify.NotifyOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NotifyOrderController {

    private final NotifyOrderService notifyOrderService;

    @RequestMapping("notify/{logical}")
    public String notifyOrder(@PathVariable String logical, HttpServletRequest request) {
        return notifyOrderService.notifyOrder(logical, request);
    }
}
