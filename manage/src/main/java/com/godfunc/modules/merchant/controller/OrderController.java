package com.godfunc.modules.merchant.controller;

import com.godfunc.constant.LogRecordConstant;
import com.godfunc.dto.PageDTO;
import com.godfunc.modules.log.annotation.LogRecord;
import com.godfunc.modules.merchant.dto.OrderDTO;
import com.godfunc.modules.merchant.service.OrderService;
import com.godfunc.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@LogRecord("订单")
@Api(tags = "订单")
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("page/{page}/{limit}")
    @LogRecord(LogRecordConstant.PAGE)
    @ApiOperation(LogRecordConstant.PAGE)
    @PreAuthorize("hasAuthority('merchant:order:page')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码", paramType = "path", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "path", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "status", value = "状态", paramType = "query", dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "payType", value = "支付类型", paramType = "query", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "tradeNo", value = "商户单号", paramType = "query", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "orderNo", value = "创建时间", paramType = "query", dataType = "LocalDateTime", dataTypeClass = LocalDateTime.class)
    })
    public R<PageDTO<OrderDTO>> page(@PathVariable Integer page,
                                     @PathVariable Integer limit,
                                     @RequestParam(required = false) Integer status,
                                     @RequestParam(required = false) String payType,
                                     @RequestParam(required = false) String tradeNo,
                                     @RequestParam(required = false) String orderNo,
                                     @RequestParam(required = false) LocalDateTime createTime) {
        return R.ok(orderService.getPage(page, limit, status, payType, tradeNo, orderNo, createTime));
    }

    @PostMapping("updatePaid/{id}")
    @LogRecord("修改为已支付")
    @ApiOperation("修改为已支付")
    @PreAuthorize("hasAuthority('merchant:order:updatePaid')")
    @ApiImplicitParam(name = "id", value = "订单id", paramType = "path", required = true, dataType = "Long", dataTypeClass = Long.class)
    public R updatePaid(@PathVariable Long id) {
        if (orderService.updatePaid(id)) {
            return R.ok();
        }
        return R.fail("操作失败");
    }

    @PostMapping("notifyMerchant/{id}")
    @LogRecord("通知商户")
    @ApiOperation("通知商户")
    @PreAuthorize("hasAuthority('merchant:order:notify')")
    @ApiImplicitParam(name = "id", value = "订单id", paramType = "path", required = true, dataType = "Long", dataTypeClass = Long.class)
    public R notifyMerchant(@PathVariable Long id) {
        if (orderService.notifyMerchant(id)) {
            return R.ok();

        }
        return R.fail("操作失败");
    }
}
