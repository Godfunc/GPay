package com.godfunc.modules.merchant.controller;

import com.godfunc.constant.LogRecordConstant;
import com.godfunc.dto.PageDTO;
import com.godfunc.modules.log.annotation.LogRecord;
import com.godfunc.modules.merchant.dto.ChannelRiskDTO;
import com.godfunc.modules.merchant.dto.ChannelRiskSimpleDTO;
import com.godfunc.modules.merchant.param.ChannelRiskAddParam;
import com.godfunc.modules.merchant.param.ChannelRiskEditParam;
import com.godfunc.modules.merchant.service.ChannelRiskService;
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
@LogRecord("渠道风控")
@Api(tags = "渠道风控")
@RequestMapping("channelRisk")
@RequiredArgsConstructor
public class ChannelRiskController {

    private final ChannelRiskService channelRiskService;

    @GetMapping("page/{page}/{limit}")
    @LogRecord(LogRecordConstant.PAGE)
    @ApiOperation(LogRecordConstant.PAGE)
    @PreAuthorize("hasAuthority('merchant:channelRisk:page')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码", paramType = "path", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "path", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "status", value = "状态", paramType = "query", dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "channelCode", value = "渠道子类编号", paramType = "query", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "accountCode", value = "账号商户号", paramType = "query", dataType = "String", dataTypeClass = String.class)
    })
    public R<PageDTO<ChannelRiskDTO>> page(@PathVariable Integer page,
                                           @PathVariable Integer limit,
                                           @RequestParam(required = false) Integer status,
                                           @RequestParam(required = false) String channelCode,
                                           @RequestParam(required = false) String accountCode) {
        return R.ok(channelRiskService.getPage(page, limit, status, channelCode, accountCode));
    }

    @GetMapping("listByAccount/{channelAccountId}")
    @LogRecord("listByAccount")
    @ApiOperation("listByAccount")
    @PreAuthorize("hasAuthority('merchant:channelRisk:listByAccount')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "channelAccountId", value = "渠道账号id", paramType = "query", dataType = "Long", dataTypeClass = Long.class)
    })
    public R<List<ChannelRiskSimpleDTO>> listByAccount(@PathVariable Long channelAccountId) {
        return R.ok(channelRiskService.getByAccount(channelAccountId));
    }

    @GetMapping("listByChannel/{channelId}")
    @LogRecord("listByChannel")
    @ApiOperation("listByChannel")
    @PreAuthorize("hasAuthority('merchant:channelRisk:listByChannel')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "channelId", value = "渠道子类id", paramType = "query", dataType = "Long", dataTypeClass = Long.class)
    })
    public R<List<ChannelRiskSimpleDTO>> listByChannel(@PathVariable Long channelId) {
        return R.ok(channelRiskService.getByChannel(channelId));
    }

    @PostMapping("add")
    @LogRecord(LogRecordConstant.ADD)
    @ApiOperation(LogRecordConstant.ADD)
    @PreAuthorize("hasAuthority('merchant:channelRisk:add')")
    public R<Long> edit(@RequestBody ChannelRiskAddParam param) {
        return R.ok(channelRiskService.add(param));
    }

    @PostMapping("edit")
    @LogRecord(LogRecordConstant.EDIT)
    @ApiOperation(LogRecordConstant.EDIT)
    @PreAuthorize("hasAuthority('merchant:channelRisk:edit')")
    public R<Long> edit(@RequestBody ChannelRiskEditParam param) {
        return R.ok(channelRiskService.edit(param));
    }

    @PostMapping("remove/{id}")
    @LogRecord(LogRecordConstant.REMOVE)
    @ApiOperation(LogRecordConstant.REMOVE)
    @PreAuthorize("hasAuthority('merchant:channelRisk:remove')")
    @ApiImplicitParam(name = "id", value = "渠道风控id", paramType = "path", required = true, dataType = "Long", dataTypeClass = Long.class)
    public R<Boolean> remove(@PathVariable Long id) {
        return R.ok(channelRiskService.removeData(id));
    }
}
