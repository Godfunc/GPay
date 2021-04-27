package com.godfunc.modules.log.controller;


import com.godfunc.constant.LogRecordConstant;
import com.godfunc.dto.PageDTO;
import com.godfunc.modules.log.annotation.LogRecord;
import com.godfunc.modules.log.dto.LogDTO;
import com.godfunc.modules.log.service.LogOperationService;
import com.godfunc.result.R;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 操作日志表 前端控制器
 * </p>
 *
 * @author Godfunc
 * @since 2019-12-01
 */
@Api(tags = "日志")
@RequiredArgsConstructor
@LogRecord("日志")
@RestController
@RequestMapping("log")
public class LogOperationController {

    private final LogOperationService logOperationService;

    @ApiOperation(LogRecordConstant.PAGE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码", paramType = "path", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "path", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "status", value = "状态", paramType = "query", dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "operation", value = "用户操作", paramType = "query", dataType = "String", dataTypeClass = String.class)
    })
    @LogRecord(LogRecordConstant.PAGE)
    @PreAuthorize("hasAuthority('mg:log:page')")
    @GetMapping("page/{page}/{limit}")
    public R<PageDTO<LogDTO>> page(@PathVariable Integer page,
                                   @PathVariable Integer limit,
                                   @RequestParam(required = false) Integer status,
                                   @RequestParam(required = false) String operation) {
        return R.ok(logOperationService.getPage(page, limit, status, operation));
    }
}
