package com.ty.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ty.dao.ArticleMapper;
import com.ty.dao.CommentMapper;
import com.ty.domain.http.Result;
import com.ty.domain.pojo.Article;
import com.ty.domain.pojo.Comment;
import com.ty.domain.pojo.SysUser;
import com.ty.domain.vo.CommentVo;
import com.ty.domain.vo.UserVo;
import com.ty.domain.vo.param.CommentParam;
import com.ty.service.CommentsService;
import com.ty.service.SysUserService;
import com.ty.utils.UserThreadLocal;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {

    @Resource
    private CommentMapper commentMapper;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private ArticleMapper articleMapper;

    @Override
    public Result commentsByArticleId(Long id) {
        /**
         * 1.根据文章id查询评论列表
         * 2.根据作者id查询作者信息
         * 3.判断 如果level = 1 要去查询它有没有子评论
         * 4.如果有 根据评论id进行查询
         */
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<Comment>();
        queryWrapper.eq(Comment::getArticleId, id);
        queryWrapper.eq(Comment::getLevel, 1);
        queryWrapper.orderByDesc(Comment::getCreateDate);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList = copyList(comments);
        Article article = articleMapper.selectById(id);
        article.setCommentCounts(comments.size());
        articleMapper.updateById(article);
        return Result.success(commentVoList);
    }

    @Override
    public Result commment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        //如果父id为空,则父评论,否则为子评论
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        } else {
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);

        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        commentMapper.insert(comment);
        //插入到数据库
        return Result.success(null);
    }

    @Override
    public Result delete(Long id) {
        boolean res = true;
        res |= commentMapper.deleteById(id) > 0;
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getLevel, 2);
        queryWrapper.eq(Comment::getParentId, id);
        res |= commentMapper.delete(queryWrapper) >= 0;

        if(res)
            Result.success(null);
        return Result.fail(200,"删除失败");
    }

    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        commentVo.setId(String.valueOf(comment.getId()));
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        BeanUtils.copyProperties(comment, commentVo);
        //作者信息
        Long authorId =  comment.getAuthorId();
        UserVo userVo = sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);
        //子评论
        Integer level = comment.getLevel();
        if(1 == level) {
            Long id = comment.getId();
            List<CommentVo> commentVoList = findCommentsByParentId(id);
            commentVo.setChildrens(commentVoList);
        }
        //to User 评论对象
        if(level > 1) {
            Long toUid = comment.getToUid();
            UserVo toUserVo = sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId, id);
        queryWrapper.eq(Comment::getLevel, 2);
        return copyList(commentMapper.selectList(queryWrapper));
    }
}
