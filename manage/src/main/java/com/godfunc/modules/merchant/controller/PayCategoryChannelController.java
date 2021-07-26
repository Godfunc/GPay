package com.godfunc.modules.merchant.controller;

import com.godfunc.constant.LogRecordConstant;
import com.godfunc.modules.log.annotation.LogRecord;
import com.godfunc.modules.merchant.dto.PayCategoryChannelDTO;
import com.godfunc.modules.merchant.param.PayCategoryChannelWeightParam;
import com.godfunc.modules.merchant.service.PayCategoryChannelService;
import com.godfunc.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@LogRecord("渠道关联")
@Api(tags = "渠道关联")
@RequestMapping("payCategoryChannel")
@RequiredArgsConstructor
public class PayCategoryChannelController {

    private final PayCategoryChannelService payCategoryChannelService;

    @GetMapping("getByChannel/{channelId}")
    @LogRecord("查询主类")
    @ApiOperation("查询子类关联的主类")
    @PreAuthorize("hasAuthority('merchant:payCategoryChannel:getByChannel')")
    public R<Set<Long>> getByChannel(@PathVariable Long channelId) {
        return R.ok(payCategoryChannelService.getByChannel(channelId));
    }

    @GetMapping("list")
    @LogRecord(LogRecordConstant.LIST)
    @ApiOperation(LogRecordConstant.LIST)
    @PreAuthorize("hasAuthority('merchant:payCategoryChannel:list')")
    public R<List<PayCategoryChannelDTO>> list(@RequestParam(required = false) Long payCategoryId) {
        return R.ok(payCategoryChannelService.getList(payCategoryId));
    }

    @PostMapping("weight")
    @LogRecord("权重设置")
    @ApiOperation("权重设置")
    @PreAuthorize("hasAuthority('merchant:payCategoryChannel:weight')")
    public R<Boolean> weight(@RequestBody PayCategoryChannelWeightParam param) {
        return R.ok(payCategoryChannelService.weight(param));
    }
}
