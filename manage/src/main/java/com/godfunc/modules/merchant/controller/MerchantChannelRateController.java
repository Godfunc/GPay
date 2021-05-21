package com.godfunc.modules.merchant.controller;

import com.godfunc.constant.LogRecordConstant;
import com.godfunc.dto.PageDTO;
import com.godfunc.modules.log.annotation.LogRecord;
import com.godfunc.modules.merchant.dto.MerchantChannelRateDTO;
import com.godfunc.modules.merchant.dto.MerchantChannelSimpleRateDTO;
import com.godfunc.modules.merchant.param.MerchantChannelRateAddParam;
import com.godfunc.modules.merchant.param.MerchantChannelRateEditParam;
import com.godfunc.modules.merchant.param.MerchantChannelRateSaveParam;
import com.godfunc.modules.merchant.service.MerchantChannelRateService;
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
@LogRecord("商户渠道费率")
@Api(tags = "商户渠道费率")
@RequestMapping("merchantChannelRate")
@RequiredArgsConstructor
public class MerchantChannelRateController {

    private final MerchantChannelRateService merchantChannelRateService;

    @GetMapping("page/{page}/{limit}")
    @LogRecord(LogRecordConstant.PAGE)
    @ApiOperation(LogRecordConstant.PAGE)
    @PreAuthorize("hasAuthority('merchant:merchantChannelRate:page')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码", paramType = "path", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "path", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "merchantCode", value = "商户号", paramType = "query", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "channelCode", value = "渠道子类编号", paramType = "query", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "categoryCode", value = "渠道主类编号", paramType = "query", dataType = "String", dataTypeClass = String.class)
    })
    public R<PageDTO<MerchantChannelRateDTO>> page(@PathVariable Integer page,
                                                   @PathVariable Integer limit,
                                                   @RequestParam(required = false) String merchantCode,
                                                   @RequestParam(required = false) String channelCode,
                                                   @RequestParam(required = false) String categoryCode) {
        return R.ok(merchantChannelRateService.getPage(page, limit, merchantCode, channelCode, categoryCode));
    }

    @GetMapping("list/{merchantCode}")
    @LogRecord(LogRecordConstant.LIST)
    @ApiOperation(LogRecordConstant.LIST)
    @PreAuthorize("hasAuthority('merchant:merchantChannelRate:list')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchantCode", value = "商户号", paramType = "query", dataType = "String", dataTypeClass = String.class)
    })
    public R<List<MerchantChannelSimpleRateDTO>> list(@PathVariable String merchantCode) {
        return R.ok(merchantChannelRateService.listByMerchant(merchantCode));
    }

    @PostMapping("save")
    @LogRecord(LogRecordConstant.SAVE)
    @ApiOperation(LogRecordConstant.SAVE)
    @PreAuthorize("hasAuthority('merchant:merchantChannelRate:save')")
    public R<Boolean> save(@RequestBody MerchantChannelRateSaveParam param) {
        return R.ok(merchantChannelRateService.saveData(param));
    }

    @PostMapping("add")
    @LogRecord(LogRecordConstant.ADD)
    @ApiOperation(LogRecordConstant.ADD)
    @PreAuthorize("hasAuthority('merchant:merchantChannelRate:add')")
    public R<Long> edit(@RequestBody MerchantChannelRateAddParam param) {
        return R.ok(merchantChannelRateService.add(param));
    }

    @PostMapping("edit")
    @LogRecord(LogRecordConstant.EDIT)
    @ApiOperation(LogRecordConstant.EDIT)
    @PreAuthorize("hasAuthority('merchant:merchantChannelRate:edit')")
    public R<Long> edit(@RequestBody MerchantChannelRateEditParam param) {
        return R.ok(merchantChannelRateService.edit(param));
    }

    @PostMapping("remove/{id}")
    @LogRecord(LogRecordConstant.REMOVE)
    @ApiOperation(LogRecordConstant.REMOVE)
    @PreAuthorize("hasAuthority('merchant:merchantChannelRate:remove')")
    @ApiImplicitParam(name = "id", value = "商户渠道费率id", paramType = "path", required = true, dataType = "Long", dataTypeClass = Long.class)
    public R<Boolean> remove(@PathVariable Long id) {
        return R.ok(merchantChannelRateService.removeData(id));
    }
}
