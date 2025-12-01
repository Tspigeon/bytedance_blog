package com.ty.controller;


import com.ty.domain.http.Result;
import com.ty.domain.vo.param.CommentParam;
import com.ty.service.CommentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("comments")
@Api(tags = "评论接口")
public class CommentsController {

    @Resource
    private CommentsService commentsService;

    @GetMapping("article/{id}")
    @ApiOperation("根据文章id查询评论列表")
    public Result comments(@PathVariable("id") Long id) {
        return commentsService.commentsByArticleId(id);
    }

    @PostMapping("create/change")
    @ApiOperation("评论功能")
    public Result comment(@RequestBody CommentParam commentParam) {
        return commentsService.commment(commentParam);
    }

    @PostMapping("delete")
    @ApiOperation("删除评论")
    public Result delete(@RequestBody String id) {
        return commentsService.delete(Long.valueOf(id));
    }

}
