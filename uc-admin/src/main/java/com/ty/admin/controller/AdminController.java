package com.ty.admin.controller;

import com.ty.admin.model.params.PageParam;
import com.ty.admin.model.pojo.Permission;
import com.ty.admin.model.vo.Result;
import com.ty.admin.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("admin")
@Api(tags = "后台管理接口")
public class AdminController {

    @Autowired
    private PermissionService permissionService;

    @ApiOperation("获取权限列表")
    @PostMapping("permission/permissionList")
    public Result permissionList(@RequestBody PageParam pageParam){
        return permissionService.listPermission(pageParam);
    }

    @ApiOperation("添加权限")
    @PostMapping("permission/add")
    public Result add(@RequestBody Permission permission){
        return permissionService.add(permission);
    }

    @ApiOperation("修改权限")
    @PostMapping("permission/update")
    public Result update(@RequestBody Map<String, Object> info){
        return permissionService.update(info);
    }

    @ApiOperation("删除权限")
    @GetMapping("permission/delete/{id}")
    public Result delete(@PathVariable("id") Long id){
        return permissionService.delete(id);
    }
}

