package com.ty.service;

import com.ty.domain.http.Result;
import com.ty.domain.vo.param.ArticleParam;
import com.ty.domain.vo.param.PageParams;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ArticleService {
    /**
     * 分页查询 文章列表
     * @param pageParams
     * @return
     */
    Result listArticle(PageParams pageParams);

    /**
     * 最热文章
     * @param limit
     * @return
     */
    Result hotArticle(int limit);

    /**
     * 最新文章
     * @param limit
     * @return
     */
    Result newArticle(int limit);

    /**
     * 文章归档
     * @return
     */
    Result listArchives();

    /**
     * 查看文章详情
     * @param articleId
     * @return
     */
    Result findArticleById(Long articleId);

    /**
     * 文章发布服务
     * @param articleParam
     * @return
     */
    Result publish(ArticleParam articleParam);

    /**
     * 文章删除
     * @param articleId
     * @return
     */
    Result delete(Long articleId);

    /**
     * 查看我的文章
     * @param pageParams
     * @return
     */
    Result listMyArticle(PageParams pageParams);
}
