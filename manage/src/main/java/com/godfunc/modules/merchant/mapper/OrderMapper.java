package com.godfunc.modules.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.godfunc.entity.Order;
import com.godfunc.modules.merchant.dto.OrderDTO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderMapper extends BaseMapper<Order> {
    List<OrderDTO> selectCustomPage(IPage resultPage, @Param("merchantId") Long merchantId,
                                    @Param("status") Integer status, @Param("payType") String payType,
                                    @Param("outTradeNo") String outTradeNo, @Param("orderNo") String orderNo,
                                    @Param("createTime") LocalDateTime createTime);
}
