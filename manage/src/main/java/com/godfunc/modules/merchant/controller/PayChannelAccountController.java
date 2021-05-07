package com.godfunc.modules.merchant.controller;

import com.godfunc.constant.LogRecordConstant;
import com.godfunc.dto.PageDTO;
import com.godfunc.modules.log.annotation.LogRecord;
import com.godfunc.modules.merchant.dto.PayChannelAccountDTO;
import com.godfunc.modules.merchant.param.PayChannelAccountAddParam;
import com.godfunc.modules.merchant.param.PayChannelAccountEditParam;
import com.godfunc.modules.merchant.service.PayChannelAccountService;
import com.godfunc.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@LogRecord("渠道账号")
@Api(tags = "渠道账号")
@RequestMapping("payChannelAccount")
@RequiredArgsConstructor
public class PayChannelAccountController {

    private final PayChannelAccountService payChannelAccountService;

    @GetMapping("page/{page}/{limit}")
    @LogRecord(LogRecordConstant.PAGE)
    @ApiOperation(LogRecordConstant.PAGE)
    @PreAuthorize("hasAuthority('merchant:payChannelAccount:page')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码", paramType = "path", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "path", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "status", value = "状态", paramType = "query", dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "channelCode", value = "渠道子类编号", paramType = "query", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "name", value = "账号名", paramType = "query", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "accountCode", value = "账号编号", paramType = "query", dataType = "String", dataTypeClass = String.class)
    })
    public R<PageDTO<PayChannelAccountDTO>> page(@PathVariable Integer page,
                                                 @PathVariable Integer limit,
                                                 @RequestParam(required = false) Integer status,
                                                 @RequestParam(required = false) String channelCode,
                                                 @RequestParam(required = false) String name,
                                                 @RequestParam(required = false) String accountCode) {
        return R.ok(payChannelAccountService.getPage(page, limit, status, channelCode, name, accountCode));
    }

    @PostMapping("add")
    @LogRecord(LogRecordConstant.ADD)
    @ApiOperation(LogRecordConstant.ADD)
    @PreAuthorize("hasAuthority('merchant:payChannelAccount:add')")
    public R<Long> add(@RequestBody PayChannelAccountAddParam param) {
        return R.ok(payChannelAccountService.add(param));
    }

    @PostMapping("edit")
    @LogRecord(LogRecordConstant.EDIT)
    @ApiOperation(LogRecordConstant.EDIT)
    @PreAuthorize("hasAuthority('merchant:payChannelAccount:edit')")
    public R<Long> edit(@RequestBody PayChannelAccountEditParam param) {
        return R.ok(payChannelAccountService.edit(param));
    }

    @PostMapping("remove/{id}")
    @LogRecord(LogRecordConstant.REMOVE)
    @ApiOperation(LogRecordConstant.REMOVE)
    @PreAuthorize("hasAuthority('merchant:payChannelAccount:remove')")
    @ApiImplicitParam(name = "id", value = "渠道账号id", paramType = "path", required = true, dataType = "Long", dataTypeClass = Long.class)
    public R<Boolean> remove(@PathVariable Long id) {
        return R.ok(payChannelAccountService.removeData(id));
    }
}
