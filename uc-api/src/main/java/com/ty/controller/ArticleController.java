package com.ty.controller;


import com.ty.common.aop.LogAnnotation;
import com.ty.domain.http.Result;
import com.ty.domain.vo.param.ArticleParam;
import com.ty.domain.vo.param.PageParams;
import com.ty.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("articles")
@Api(tags = "文章接口")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    /**
     * 首页文章列表
     * @param pageParams
     * @return
     */
    @PostMapping
    @LogAnnotation(module="文章", operator="获取文章列表")
    @Cacheable(value = "Article", key = "#pageParams.page")
    @ApiOperation("获取文章列表")
    public Result listArticle(@RequestBody PageParams pageParams) {
        return articleService.listArticle(pageParams);
    }

    /**
     * 首页最热文章
     * @return
     */
    @PostMapping("hot")
    @Cacheable(value = "Article", key = "'hotArticle'")
    @ApiOperation("获取最热文章列表 5")
    public Result hotArticle() {
        int limit = 5;
        return articleService.hotArticle(limit);
    }

    /**
     * 首页最新文章
     * @return
     */
    @PostMapping("new")
    @Cacheable(value = "Article", key = "'newArticle'")
    @ApiOperation("获取最新文章列表 5")
    public Result newArticle() {
        int limit = 5;
        return articleService.newArticle(limit);
    }

    /**
     * 首页文章归档
     * @return
     */
    @PostMapping("listArchives")
    @ApiOperation("获取归档列表")
    public Result listArchives() {
        return articleService.listArchives();
    }

    /**
     * 查看文章详情
     * @param articleId
     * @return
     */
    @PostMapping("view/{id}")
    @ApiOperation("根据文章id查询文章详情")
    public Result findArticleById(@PathVariable("id") Long articleId){
        return articleService.findArticleById(articleId);

    }

    @PostMapping("publish")
    @ApiOperation("写文章")
    @Caching(evict ={
            @CacheEvict(value = "Article",key = "1"),
            @CacheEvict(value = "Article",key = "'newArticle'")
    } )
    public Result publish(@RequestBody ArticleParam articleParam) {
        return articleService.publish(articleParam);
    }

    @PostMapping("delete")
    @ApiOperation("删除文章")
    @CacheEvict(value = "Article", allEntries = true)
    public Result delete(@RequestBody String articleId) {
        return articleService.delete(Long.valueOf(articleId));
    }

    @GetMapping("mylist")
    @ApiOperation("查看我的文章")
    public Result listMyArticle(@RequestBody PageParams pageParams) {
        return articleService.listMyArticle(pageParams);
    }
}
