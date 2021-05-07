package com.godfunc.modules.merchant.controller;

import com.godfunc.constant.LogRecordConstant;
import com.godfunc.dto.PageDTO;
import com.godfunc.modules.log.annotation.LogRecord;
import com.godfunc.modules.merchant.dto.PayCategorySimpleDTO;
import com.godfunc.modules.merchant.dto.PayChannelDTO;
import com.godfunc.modules.merchant.dto.PayChannelSimpleDTO;
import com.godfunc.modules.merchant.param.PayChannelAddParam;
import com.godfunc.modules.merchant.param.PayChannelEditParam;
import com.godfunc.modules.merchant.service.PayChannelService;
import com.godfunc.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@LogRecord("渠道子类")
@Api(tags = "渠道子类")
@RequestMapping("payChannel")
@RequiredArgsConstructor
public class PayChannelController {

    private final PayChannelService payChannelService;

    @GetMapping("page/{page}/{limit}")
    @LogRecord(LogRecordConstant.PAGE)
    @ApiOperation(LogRecordConstant.PAGE)
    @PreAuthorize("hasAuthority('merchant:payChannel:page')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码", paramType = "path", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "path", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "status", value = "状态", paramType = "query", dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "name", value = "渠道名", paramType = "query", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "code", value = "渠道编号", paramType = "query", dataType = "String", dataTypeClass = String.class)
    })
    public R<PageDTO<PayChannelDTO>> page(@PathVariable Integer page,
                                          @PathVariable Integer limit,
                                          @RequestParam(required = false) Integer status,
                                          @RequestParam(required = false) String code,
                                          @RequestParam(required = false) String name) {
        return R.ok(payChannelService.getPage(page, limit, status, code, name));
    }

    @GetMapping("list")
    @LogRecord(LogRecordConstant.LIST)
    @ApiOperation(LogRecordConstant.LIST)
    @PreAuthorize("hasAuthority('merchant:payChannel:list')")
    public R<List<PayChannelSimpleDTO>> list() {
        return R.ok(payChannelService.getList());
    }

    @PostMapping("add")
    @LogRecord(LogRecordConstant.ADD)
    @ApiOperation(LogRecordConstant.ADD)
    @PreAuthorize("hasAuthority('merchant:payChannel:add')")
    public R<Long> add(@RequestBody PayChannelAddParam param) {
        return R.ok(payChannelService.add(param));
    }

    @PostMapping("edit")
    @LogRecord(LogRecordConstant.EDIT)
    @ApiOperation(LogRecordConstant.EDIT)
    @PreAuthorize("hasAuthority('merchant:payChannel:edit')")
    public R<Long> edit(@RequestBody PayChannelEditParam param) {
        return R.ok(payChannelService.edit(param));
    }

    @PostMapping("remove/{id}")
    @LogRecord(LogRecordConstant.REMOVE)
    @ApiOperation(LogRecordConstant.REMOVE)
    @PreAuthorize("hasAuthority('merchant:payChannel:remove')")
    @ApiImplicitParam(name = "id", value = "渠道子类id", paramType = "path", required = true, dataType = "Long", dataTypeClass = Long.class)
    public R<Boolean> remove(@PathVariable Long id) {
        return R.ok(payChannelService.removeData(id));
    }
}
