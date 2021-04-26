package com.godfunc.modules.sys.controller;


import com.godfunc.dto.PageDTO;
import com.godfunc.modules.log.annotation.LogRecord;
import com.godfunc.modules.sys.dto.RoleDTO;
import com.godfunc.modules.sys.dto.RoleListDTO;
import com.godfunc.modules.sys.param.RoleAddParam;
import com.godfunc.modules.sys.param.RoleEditParam;
import com.godfunc.modules.sys.service.RoleService;
import com.godfunc.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author Godfunc
 * @since 2019-12-01
 */
@Api(tags = "角色")
@RequiredArgsConstructor
@LogRecord("角色")
@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码", paramType = "path", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "path", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "name", value = "角色名", paramType = "query", dataType = "String", dataTypeClass = String.class)
    })
    @LogRecord("分页")
    @PreAuthorize("hasAuthority('mg:role:page')")
    @GetMapping("page/{page}/{limit}")
    public R<PageDTO<RoleDTO>> page(@PathVariable Long page, @PathVariable Long limit, @RequestParam(required = false) String name) {
        return R.ok(roleService.getPage(page, limit, name));
    }

    @ApiOperation("列表")
    @LogRecord("列表")
    @PreAuthorize("hasAuthority('mg:role:list')")
    @GetMapping("list")
    public R<List<RoleListDTO>> list() {
        return R.ok(roleService.getList());
    }

    @ApiOperation("查询用户角色")
    @ApiImplicitParam(name = "userId", value = "用户id", paramType = "path", required = true, dataType = "Long", dataTypeClass = Long.class)
    @LogRecord("用户角色")
    @PreAuthorize("hasAuthority('mg:role:getByUser')")
    @GetMapping("getByUser/{userId}")
    public R<Set<Long>> getByUser(@PathVariable Long userId) {
        return R.ok(roleService.getByUser(userId));
    }

    @ApiOperation("新增")
    @LogRecord("新增")
    @PreAuthorize("hasAuthority('mg:role:add')")
    @PostMapping("add")
    public R<Long> add(@RequestBody RoleAddParam param) {
        return R.ok(roleService.add(param));
    }

    @ApiOperation("修改")
    @LogRecord("修改")
    @PreAuthorize("hasAuthority('mg:role:edit')")
    @PostMapping("edit")
    public R<Long> edit(@RequestBody RoleEditParam param) {
        return R.ok(roleService.edit(param));
    }

    @ApiOperation("删除")
    @ApiImplicitParam(name = "id", value = "角色id", paramType = "path", required = true, dataType = "Long", dataTypeClass = Long.class)
    @LogRecord("删除")
    @PreAuthorize("hasAuthority('mg:role:remove')")
    @PostMapping("remove/{id}")
    public R<Boolean> remove(@PathVariable Long id) {
        return R.ok(roleService.removeDate(id));
    }

    @ApiOperation("查询角色的菜单")
    @ApiImplicitParam(name = "id", value = "角色id", paramType = "path", required = true, dataType = "Long", dataTypeClass = Long.class)
    @LogRecord("菜单")
    @PreAuthorize("hasAuthority('mg:role:getMenus')")
    @GetMapping("getMenus/{id}")
    public R<Set<Long>> getMenus(@PathVariable Long id) {
        return R.ok(roleService.getMenus(id));
    }
}
