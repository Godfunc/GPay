package com.godfunc.modules.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.godfunc.entity.OrderLog;
import com.godfunc.modules.merchant.dto.OrderLogDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderLogMapper extends BaseMapper<OrderLog> {
    List<OrderLogDTO> selectCustomPage(IPage resultPage, @Param("orderId") Long orderId, @Param("merchantId") Long merchantId);
}
