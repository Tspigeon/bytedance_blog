package com.ty.service;

import com.ty.domain.http.Result;
import com.ty.domain.vo.param.CommentParam;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CommentsService {

    /**
     * 根据文章id 获取评论列表
     * @param id
     * @return
     */
    Result commentsByArticleId(Long id);

    /**
     * 评论功能
     * @param commentParam
     * @return
     */
    Result commment(CommentParam commentParam);

    /**
     * 删除评论
     * @param id
     * @return
     */
    Result delete(Long id);
}
