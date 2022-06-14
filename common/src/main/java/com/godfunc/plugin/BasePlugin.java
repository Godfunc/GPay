package com.godfunc.plugin;

import com.godfunc.dto.PayInfoDTO;
import com.godfunc.entity.Order;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BasePlugin extends Plugin {
    /**
     * Constructor to be used by plugin manager for plugin instantiation.
     * Your plugins have to provide constructor with this exact signature to
     * be successfully loaded by manager.
     *
     * @param wrapper
     */
    public BasePlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    /**
     * create order
     * @param order
     * @return
     */
    public abstract PayInfoDTO doPay(Order order);

    /**
     * default handler response
     * @param payInfo
     * @param request
     * @param response
     */
    public void handleResponse(PayInfoDTO payInfo, HttpServletRequest request, HttpServletResponse response) {
        try {
            response.sendRedirect(payInfo.getPayUrl());
        } catch (IOException e) {
            log.error("跳转到支付链接异常", e);
        }
    }


}
