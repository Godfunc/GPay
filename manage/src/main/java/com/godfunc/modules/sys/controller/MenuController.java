package com.godfunc.modules.sys.controller;


import com.godfunc.modules.log.annotation.LogRecord;
import com.godfunc.modules.sys.dto.MenuDTO;
import com.godfunc.modules.sys.dto.MenuListDTO;
import com.godfunc.modules.sys.dto.MenuTreeDTO;
import com.godfunc.modules.sys.param.MenuAddParam;
import com.godfunc.modules.sys.param.MenuEditParam;
import com.godfunc.modules.sys.service.MenuService;
import com.godfunc.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author Godfunc
 * @since 2019-12-01
 */
@Api(tags = "菜单")
@RequiredArgsConstructor
@LogRecord("菜单")
@RestController
@RequestMapping("menu")
public class MenuController {

    private final MenuService menuService;

    @ApiOperation("菜单列表")
    @LogRecord("管理")
    @PreAuthorize("hasAuthority('mg:menu:getAll')")
    @GetMapping("getAll")
    public R<List<MenuDTO>> page() {
        return R.ok(menuService.getAll());
    }

    @ApiOperation("选项树")
    @LogRecord("选项树")
    @PreAuthorize("hasAuthority('mg:menu:getTree')")
    @GetMapping("getTree")
    public R<List<MenuTreeDTO>> getTree() {
        return R.ok(menuService.getTree());
    }

    @ApiOperation("获取菜单")
    @LogRecord("获取菜单")
    @GetMapping("list")
    public R<List<MenuListDTO>> list() {
        return R.ok(menuService.getList());
    }

    @ApiOperation("新增")
    @LogRecord("新增")
    @PreAuthorize("hasAuthority('mg:menu:add')")
    @PostMapping("add")
    public R<Long> add(@RequestBody MenuAddParam param) {
        return R.ok(menuService.add(param));
    }

    @ApiOperation("修改")
    @LogRecord("修改")
    @PreAuthorize("hasAuthority('mg:menu:edit')")
    @PostMapping("edit")
    public R<Long> edit(@RequestBody MenuEditParam param) {
        return R.ok(menuService.edit(param));
    }

    @ApiOperation("删除")
    @ApiImplicitParam(name = "id", value = "菜单id", paramType = "path", required = true, dataType = "Long", dataTypeClass = Long.class)
    @LogRecord("删除")
    @PreAuthorize("hasAuthority('mg:menu:remove')")
    @PostMapping("remove/{id}")
    public R<Boolean> remove(@PathVariable Long id) {
        return R.ok(menuService.removeData(id));
    }

}
