package com.ty.controller;

import com.ty.domain.http.Result;
import com.ty.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("users")
@Api(tags = "用户接口")
public class UsersController {

    @Resource
    private SysUserService sysUserService;

    // /users/currentUser
    @GetMapping("currentUser")
    @ApiOperation("获取当前用户信息")
    public Result currentUser(@RequestHeader("Authorization") String token) {
        return sysUserService.findUserByToken(token);
    }

    @PostMapping("modify")
    @ApiOperation("修改用户信息")
    public Result modifyUser(@RequestBody Map<String, Object> info) {
        return sysUserService.modifyUser(info) ? Result.success(null) : Result.fail(200, "修改失败");
    }

}
