package com.godfunc.modules.merchant.controller;

import com.godfunc.constant.LogRecordConstant;
import com.godfunc.dto.PageDTO;
import com.godfunc.modules.log.annotation.LogRecord;
import com.godfunc.modules.merchant.dto.MerchantDTO;
import com.godfunc.modules.merchant.dto.MerchantKeysDTO;
import com.godfunc.modules.merchant.param.MerchantAddParam;
import com.godfunc.modules.merchant.param.MerchantEditParam;
import com.godfunc.modules.merchant.service.MerchantService;
import com.godfunc.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@LogRecord("商户")
@Api(tags = "商户")
@RequestMapping("merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping("page/{page}/{limit}")
    @LogRecord(LogRecordConstant.PAGE)
    @ApiOperation(LogRecordConstant.PAGE)
    @PreAuthorize("hasAuthority('merchant:merchant:page')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码", paramType = "path", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "path", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "type", value = "商户类型", paramType = "query", dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "status", value = "状态", paramType = "query", dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "name", value = "商户名", paramType = "query", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "code", value = "商户号", paramType = "query", dataType = "String", dataTypeClass = String.class)
    })
    public R<PageDTO<MerchantDTO>> page(@PathVariable Integer page,
                                        @PathVariable Integer limit,
                                        @RequestParam(required = false) Integer type,
                                        @RequestParam(required = false) Integer status,
                                        @RequestParam(required = false) String code,
                                        @RequestParam(required = false) String name) {
        return R.ok(merchantService.getPage(page, limit, type, status, code, name));
    }

    @PostMapping("add")
    @LogRecord(LogRecordConstant.ADD)
    @ApiOperation(LogRecordConstant.ADD)
    @PreAuthorize("hasAuthority('merchant:merchant:add')")
    public R<Long> add(@RequestBody MerchantAddParam param) {
        return R.ok(merchantService.add(param));
    }

    @PostMapping("edit")
    @LogRecord(LogRecordConstant.EDIT)
    @ApiOperation(LogRecordConstant.EDIT)
    @PreAuthorize("hasAuthority('merchant:merchant:edit')")
    public R<Long> edit(@RequestBody MerchantEditParam param) {
        return R.ok(merchantService.edit(param));
    }

    @GetMapping("keys/{id}")
    @LogRecord("查看密钥")
    @ApiOperation("查看密钥")
    @PreAuthorize("hasAuthority('merchant:merchant:keys')")
    @ApiImplicitParam(name = "id", value = "商户id", paramType = "path", required = true, dataType = "Long", dataTypeClass = Long.class)
    public R<MerchantKeysDTO> keys(@PathVariable Long id) {
        return R.ok(merchantService.getKeys(id));
    }

    @PostMapping("refreshKeys/{id}")
    @LogRecord("刷新密钥")
    @ApiOperation("刷新密钥")
    @PreAuthorize("hasAuthority('merchant:merchant:refreshKeys')")
    @ApiImplicitParam(name = "id", value = "商户id", paramType = "path", required = true, dataType = "Long", dataTypeClass = Long.class)
    public R<Boolean> refreshKeys(@PathVariable Long id) {
        return R.ok(merchantService.refreshKeys(id));
    }

    @PostMapping("remove/{id}")
    @LogRecord(LogRecordConstant.REMOVE)
    @ApiOperation(LogRecordConstant.REMOVE)
    @PreAuthorize("hasAuthority('merchant:merchant:remove')")
    @ApiImplicitParam(name = "id", value = "商户id", paramType = "path", required = true, dataType = "Long", dataTypeClass = Long.class)
    public R<Boolean> remove(@PathVariable Long id) {
        return R.ok(merchantService.removeData(id));
    }
}
