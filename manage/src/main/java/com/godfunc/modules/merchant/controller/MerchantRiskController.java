package com.godfunc.modules.merchant.controller;

import com.godfunc.constant.LogRecordConstant;
import com.godfunc.modules.log.annotation.LogRecord;
import com.godfunc.modules.merchant.dto.MerchantRiskDTO;
import com.godfunc.modules.merchant.param.MerchantRiskAddParam;
import com.godfunc.modules.merchant.param.MerchantRiskEditParam;
import com.godfunc.modules.merchant.service.MerchantRiskService;
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
@LogRecord("商户风控")
@Api(tags = "商户风控")
@RequestMapping("merchantRisk")
@RequiredArgsConstructor
public class MerchantRiskController {

    private final MerchantRiskService merchantRiskService;

    @GetMapping("list/{merchantCode}")
    @LogRecord(LogRecordConstant.LIST)
    @ApiOperation(LogRecordConstant.LIST)
    @PreAuthorize("hasAuthority('merchant:merchantRisk:list')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchantCode", value = "商户号", paramType = "query", dataType = "String", dataTypeClass = String.class)
    })
    public R<List<MerchantRiskDTO>> list(@PathVariable String merchantCode) {
        return R.ok(merchantRiskService.getList(merchantCode));
    }

    @PostMapping("add")
    @LogRecord(LogRecordConstant.ADD)
    @ApiOperation(LogRecordConstant.ADD)
    @PreAuthorize("hasAuthority('merchant:merchantRisk:add')")
    public R<Long> add(@RequestBody MerchantRiskAddParam param) {
        return R.ok(merchantRiskService.add(param));
    }

    @PostMapping("edit")
    @LogRecord(LogRecordConstant.EDIT)
    @ApiOperation(LogRecordConstant.EDIT)
    @PreAuthorize("hasAuthority('merchant:merchantRisk:edit')")
    public R<Long> edit(@RequestBody MerchantRiskEditParam param) {
        return R.ok(merchantRiskService.edit(param));
    }

    @PostMapping("remove/{id}")
    @LogRecord(LogRecordConstant.REMOVE)
    @ApiOperation(LogRecordConstant.REMOVE)
    @PreAuthorize("hasAuthority('merchant:merchantRisk:remove')")
    @ApiImplicitParam(name = "id", value = "风控id", paramType = "path", required = true, dataType = "Long", dataTypeClass = Long.class)
    public R<Boolean> remove(@PathVariable Long id) {
        return R.ok(merchantRiskService.removeData(id));
    }
}
