package com.godfunc.modules.sys.controller;


import com.godfunc.dto.PageDTO;
import com.godfunc.modules.log.annotation.LogRecord;
import com.godfunc.modules.sys.dto.UserDTO;
import com.godfunc.modules.sys.dto.UserInfoDTO;
import com.godfunc.modules.sys.param.UserAddParam;
import com.godfunc.modules.sys.param.UserEditParam;
import com.godfunc.modules.sys.param.UserPasswordParam;
import com.godfunc.modules.sys.service.UserService;
import com.godfunc.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统用户 前端控制器
 * </p>
 *
 * @author Godfunc
 * @since 2019-12-01
 */
@Api(tags = "用户管理")
@RequiredArgsConstructor
@LogRecord("用户")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @ApiOperation("用户信息")
    @GetMapping("info")
    public R<UserInfoDTO> info() {
        return R.ok(userService.getUserInfo());
    }


    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码", paramType = "path", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "path", required = true, dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "status", value = "状态", paramType = "query", dataType = "int", dataTypeClass = Integer.class)
    })
    @LogRecord("分页")
    @PreAuthorize("hasAuthority('mg:user:page')")
    @GetMapping("page/{page}/{limit}")
    public R<PageDTO<UserDTO>> page(@PathVariable Integer page, @PathVariable Integer limit,
                                    @RequestParam(required = false) String username,
                                    @RequestParam(required = false) Integer status) {
        return R.ok(userService.getPage(page, limit, status, username));
    }

    @ApiOperation("新增")
    @LogRecord("新增")
    @PreAuthorize("hasAuthority('mg:user:add')")
    @PostMapping("add")
    public R<Long> add(@RequestBody UserAddParam param) {
        return R.ok(userService.add(param));
    }

    @ApiOperation("修改")
    @LogRecord("修改")
    @PreAuthorize("hasAuthority('mg:user:edit')")
    @PostMapping("edit")
    public R<Long> edit(@RequestBody UserEditParam param) {
        return R.ok(userService.edit(param));
    }


    @ApiOperation("修改密码")
    @LogRecord("修改密码")
    @PostMapping("password")
    public R<Boolean> password(@RequestBody UserPasswordParam param) {
        return R.ok(userService.password(param));
    }

    @ApiOperation("删除")
    @ApiImplicitParam(name = "id", value = "用户id", paramType = "path", required = true, dataType = "Long", dataTypeClass = Long.class)
    @LogRecord("删除")
    @PreAuthorize("hasAuthority('mg:user:remove')")
    @PostMapping("remove/{id}")
    public R<Boolean> remove(@PathVariable Long id) {
        return R.ok(userService.removeData(id));
    }

}
