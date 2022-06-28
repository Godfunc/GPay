package com.godfunc.plugin;

import com.godfunc.dto.PayInfoDTO;
import com.godfunc.entity.Order;
import org.pf4j.ExtensionPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface PayPluginExecutor extends ExtensionPoint {

    /**
     * create order
     *
     * @param order
     * @return
     */
    PayInfoDTO doPay(Order order);

    /**
     * default handler response
     *
     * @param payInfo
     * @param request
     * @param response
     */
    default void handleResponse(PayInfoDTO payInfo, HttpServletRequest request, HttpServletResponse response) {
        try {
            response.sendRedirect(payInfo.getPayUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
