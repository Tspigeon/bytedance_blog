package com.ty.controller;

import com.ty.domain.http.Result;
import com.ty.domain.vo.param.LoginParam;
import com.ty.service.LoginService;
import com.ty.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api(tags = "登录相关接口")
public class LoginController {

    @Resource
    private LoginService loginService;

    @PostMapping("login")
    @ApiOperation("登录")
    public Result login(@RequestBody LoginParam loginParam) {
        //登录 验证用户 访问用户表, 但是
        return loginService.login(loginParam);
    }

    @GetMapping("logout")
    @ApiOperation("登出")
    public Result login(@RequestHeader("Authorization") String token) {
        return loginService.logout(token);
    }

    @PostMapping("register")
    @ApiOperation("注册")
    public Result register(@RequestBody LoginParam loginParam) {
        return loginService.register(loginParam);
    }

}
