package com.ty.controller;


import com.ty.domain.http.Result;
import com.ty.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("tags")
@Api(tags = "文章标签接口")
public class TagsController {

    @Resource
    public TagService tagService;

    /**
     * /tags/hot
     * @return
     */
    @GetMapping("hot")
    @ApiOperation("获取最热标签")
    public Result hot() {
        int limit = 6;
        return tagService.hots(limit);
    }

    @GetMapping
    @ApiOperation("查询所有标签")
    public Result findAll() {
        return tagService.findAll();
    }

    @GetMapping("detail")
    @ApiOperation("查询所有标签信息")
    public Result findAllDetail() {
        return tagService.findAllDetail();
    }

    @GetMapping("detail/{id}")
    @ApiOperation("根据id查询标签细节")
    public Result findDetailById(@PathVariable("id") Long id) {
        return tagService.findDetailById(id);
    }

}
