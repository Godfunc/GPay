package com.godfunc.modules.merchant.controller;

import com.godfunc.constant.LogRecordConstant;
import com.godfunc.modules.log.annotation.LogRecord;
import com.godfunc.modules.merchant.dto.MerchantChannelSimpleRateDTO;
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

import javax.validation.Valid;
import java.util.List;


@RestController
@LogRecord("商户渠道费率")
@Api(tags = "商户渠道费率")
@RequestMapping("merchantChannelRate")
@RequiredArgsConstructor
public class MerchantChannelRateController {

    private final MerchantChannelRateService merchantChannelRateService;

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
    public R<Boolean> save(@Valid @RequestBody MerchantChannelRateSaveParam param) {
        return R.ok(merchantChannelRateService.saveData(param));
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
