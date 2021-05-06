package com.godfunc.modules.merchant.controller;

import com.godfunc.constant.LogRecordConstant;
import com.godfunc.dto.PageDTO;
import com.godfunc.modules.log.annotation.LogRecord;
import com.godfunc.modules.merchant.dto.PayCategoryDTO;
import com.godfunc.modules.merchant.dto.PayCategorySimpleDTO;
import com.godfunc.modules.merchant.param.PayCategoryAddParam;
import com.godfunc.modules.merchant.param.PayCategoryEditParam;
import com.godfunc.modules.merchant.service.PayCategoryService;
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
@LogRecord("渠道主类")
@Api(tags = "渠道主类")
@RequestMapping("payCategory")
@RequiredArgsConstructor
public class PayCategoryController {

    private final PayCategoryService payCategoryService;

    @GetMapping("page/{page}/{limit}")
    @LogRecord(LogRecordConstant.PAGE)
    @ApiOperation(LogRecordConstant.PAGE)
    @PreAuthorize("hasAuthority('merchant:payCategory:page')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码", paramType = "path", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "path", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "status", value = "状态", paramType = "query", dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "name", value = "商户名", paramType = "query", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "code", value = "商户号", paramType = "query", dataType = "String", dataTypeClass = String.class)
    })
    public R<PageDTO<PayCategoryDTO>> page(@PathVariable Integer page,
                                           @PathVariable Integer limit,
                                           @RequestParam(required = false) Integer status,
                                           @RequestParam(required = false) String code,
                                           @RequestParam(required = false) String name) {
        return R.ok(payCategoryService.getPage(page, limit, status, code, name));
    }

    @GetMapping("list")
    @LogRecord(LogRecordConstant.LIST)
    @ApiOperation(LogRecordConstant.LIST)
    @PreAuthorize("hasAuthority('merchant:payCategory:list')")
    public R<List<PayCategorySimpleDTO>> list() {
        return R.ok(payCategoryService.getList());
    }

    @PostMapping("add")
    @LogRecord(LogRecordConstant.ADD)
    @ApiOperation(LogRecordConstant.ADD)
    @PreAuthorize("hasAuthority('merchant:payCategory:add')")
    public R<Long> add(@RequestBody PayCategoryAddParam param) {
        return R.ok(payCategoryService.add(param));
    }

    @PostMapping("edit")
    @LogRecord(LogRecordConstant.EDIT)
    @ApiOperation(LogRecordConstant.EDIT)
    @PreAuthorize("hasAuthority('merchant:payCategory:edit')")
    public R<Long> edit(@RequestBody PayCategoryEditParam param) {
        return R.ok(payCategoryService.edit(param));
    }

    @PostMapping("remove/{id}")
    @LogRecord(LogRecordConstant.REMOVE)
    @ApiOperation(LogRecordConstant.REMOVE)
    @PreAuthorize("hasAuthority('merchant:payCategory:remove')")
    @ApiImplicitParam(name = "id", value = "商户id", paramType = "path", required = true, dataType = "Long", dataTypeClass = Long.class)
    public R<Boolean> remove(@PathVariable Long id) {
        return R.ok(payCategoryService.removeData(id));
    }
}
