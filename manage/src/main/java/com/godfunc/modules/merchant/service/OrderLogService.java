package com.godfunc.modules.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.dto.PageDTO;
import com.godfunc.entity.OrderLog;
import com.godfunc.modules.merchant.dto.OrderLogDTO;

public interface OrderLogService extends IService<OrderLog> {
    PageDTO<OrderLogDTO> getPage(Integer page, Integer limit, Long orderId);
}
