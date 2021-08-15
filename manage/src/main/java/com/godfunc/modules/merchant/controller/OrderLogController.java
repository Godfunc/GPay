package com.godfunc.modules.merchant.controller;

import com.godfunc.constant.LogRecordConstant;
import com.godfunc.dto.PageDTO;
import com.godfunc.modules.log.annotation.LogRecord;
import com.godfunc.modules.merchant.dto.OrderLogDTO;
import com.godfunc.modules.merchant.service.OrderLogService;
import com.godfunc.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@LogRecord("订单日志")
@Api(tags = "订单日志")
@RequestMapping("orderLog")
@RequiredArgsConstructor
public class OrderLogController {

    private final OrderLogService orderLogService;

    @GetMapping("page/{page}/{limit}/{orderId}")
    @LogRecord(LogRecordConstant.PAGE)
    @ApiOperation(LogRecordConstant.PAGE)
    @PreAuthorize("hasAuthority('merchant:orderLog:page')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码", paramType = "path", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "path", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "orderId", value = "订单id", paramType = "path", required = true, dataType = "long", dataTypeClass = Long.class)
    })
    public R<PageDTO<OrderLogDTO>> page(@PathVariable Integer page,
                                        @PathVariable Integer limit,
                                        @PathVariable Long orderId) {
        return R.ok(orderLogService.getPage(page, limit, orderId));
    }
}
