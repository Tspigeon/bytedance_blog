package com.ty.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ty.dao.ArticleMapper;
import com.ty.domain.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {
    //期望此操作在线程池执行 不会影响原有的主线程
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
        int viewCount = article.getViewCounts();
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(viewCount + 1);
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId, article.getId());
        //为了多线程环境下的线程安全
        updateWrapper.eq(Article::getViewCounts, viewCount);
        //update article set view_count = 100 where view_count == 99 and id == 1
        articleMapper.update(articleUpdate, updateWrapper);

        try {
            Thread.sleep(2000);
            System.out.println("更新完成!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
