package com.ty.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ty.dao.dos.Archives;
import com.ty.domain.pojo.Article;
import com.ty.domain.query.ViewCountQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleMapper extends BaseMapper<Article> {

    List<Archives> listArchives();


    //mybatis-plus分页
    IPage<Article> listAticle(Page<Article> page,
                              Long categoryId,
                              Long tagId,
                              String year,
                              String month);

    /**
     * 定时任务更新阅读数
     * @param list
     */
    void bathUpdateArticleViewCount(List<ViewCountQuery> list);


    IPage<Article> listMyAticle(Page<Article> page,
                                Long id,
                                Long categoryId,
                                Long tagId,
                                String year,
                                String month);
}
