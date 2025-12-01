package com.ty.controller;


import com.ty.domain.http.Result;
import com.ty.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("categorys")
@Api(tags = "文章类别接口")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping
    @ApiOperation("获取类别列表")
    public Result categories() {
        return categoryService.findAll();
    }

    @GetMapping("detail")
    @ApiOperation("获取所有类别信息")
    public Result categoriesDetail() {
        return categoryService.findAllDetail();
    }

    @GetMapping("detail/{id}")
    @ApiOperation("根据id查找类别信息")
    public Result categoryDetailById(@PathVariable("id") Long id) {

        return categoryService.categoryDetailById(id);
    }
}
