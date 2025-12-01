package com.ty.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ty.dao.ArticleBodyMapper;
import com.ty.dao.ArticleMapper;
import com.ty.dao.ArticleTagMapper;
import com.ty.dao.CommentMapper;
import com.ty.dao.dos.Archives;
import com.ty.domain.http.Result;
import com.ty.domain.pojo.*;
import com.ty.domain.query.ViewCountQuery;
import com.ty.domain.vo.ArticleBodyVo;
import com.ty.domain.vo.TagVo;
import com.ty.domain.vo.param.ArticleParam;
import com.ty.domain.vo.param.PageParams;
import com.ty.domain.vo.ArticleVo;
import com.ty.service.*;
import com.ty.utils.UserThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private TagService tagService;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private ArticleTagMapper articleTagMapper;

    @Resource
    private CommentMapper commentMapper;

//    @Override
//    public Result listArticle(PageParams pageParams) {
//        //1. 这个是分页查询的类（代表着分离模式），要传入的是页面的页数和页面总数
//        Page<Article> page = new Page<>(pageParams.getPageNum(),pageParams.getPageSize());
//        //2. LambdaQueryWrapper
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        //判断类型筛选
//        if(pageParams.getCategoryId() != null)
//            queryWrapper.eq(Article::getCategoryId, pageParams.getCategoryId());
//        //判断标签筛选
//        List<Long> articleIdList = new ArrayList<>();
//        if(pageParams.getTagId() != null) {
//            //article_tag
//            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId, pageParams.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
//            for(ArticleTag articleTag : articleTags)
//                articleIdList.add(articleTag.getArticleId());
//            if(articleIdList.size() > 0)
//                queryWrapper.in(Article::getId, articleIdList);
//        }
//        //3. 这里是根据字体排序
//        //queryWrapper.orderByDesc(Article::getWeight);
//        //4. 这里设置的是根据时间排序
//        //queryWrapper.orderByDesc(Article::getCreateDate);
//        //5. 这个方法    default Children orderByDesc(boolean condition, R column, R... columns) {是可变长参数的
//        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
//        // 因为articleMapper继承了BaseMapper了的，所有设查询的参数和查询出来的排序方式
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
//        //这个就是我们查询出来的数据的数组了
//        List<Article> records = articlePage.getRecords();
//        //因为页面展示出来的数据不一定和数据库一样，所有我们要做一个转换
//        //将在查出数据库的数组复制到articleVo中实现解耦合,vo和页面数据交互
//        List<ArticleVo> articleVoList = copyList(records, true, true);
//        return Result.success(articleVoList);
//    }

    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.listAticle(
                page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        List<Article> recordes = articleIPage.getRecords();
        return Result.success(copyList(recordes,true,true));
    }


    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        //select id, title from article order by view_counts desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles, false, false));
    }

    @Override
    public Result newArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        //select id, title from article order by create_date desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles, false, false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    //@Resource
    //private ThreadService threadService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Result findArticleById(Long articleId) {
        /**
         * 1.根据id查询文章信息
         * 2. 根据bodyid和categoryid 做关联查询
         */
        Article article = articleMapper.selectById(articleId);

        String redisKey = "VIEW_COUNT" + articleId.toString();
        Integer viewCounts = article.getViewCounts();
        redisTemplate.opsForValue().set(redisKey, viewCounts + "");
        redisTemplate.opsForValue().increment(redisKey, 1);

        ArticleVo articleVo = copy(article, true, true, true, true);

        //查看文章 新增阅读数 使用线程池 避免影响主线程
        //threadService.updateArticleViewCount(articleMapper, article);

        return Result.success(articleVo);
    }

    @Scheduled(cron = "0 30 4 ? * *")//每天凌晨四点半触发
    public void updateViewCount(){
        log.info("更新文章阅读数");
        Set<String> keys = redisTemplate.keys("VIEW_COUNT"+"*");
        List<ViewCountQuery> list = new ArrayList<>();
        if(!keys.isEmpty()){
            for (String key : keys) {
                ViewCountQuery query = new ViewCountQuery();
                String ArticleIdStr = key.substring("VIEW_COUNT".length(), key.length());
                String viewCount = redisTemplate.opsForValue().get(key);
                log.info("Id{}" + ArticleIdStr);
                log.info("viewCount{}"+viewCount);
                long articleId = Long.parseLong(ArticleIdStr);
                query.setArticleId(articleId);
                query.setViewCount(Integer.parseInt(viewCount));
                list.add(query);
                redisTemplate.delete(key);
            }
        }
        if (list.size()>0) {
            articleMapper.bathUpdateArticleViewCount(list);
        }
    }

    @Override
    public Result publish(ArticleParam articleParam) {
        //此接口要加入到登录拦截当中
        SysUser sysUser = UserThreadLocal.get();
        /**
         * 1.发布文章 构建Article对象
         * 2.作者id 登录用户
         * 3.标签 要将标签加入到关联列表当中
         */
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setWeight(Article.Article_Common);
        article.setViewCounts(0);
        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setCategoryId(Long.valueOf(articleParam.getCategory().getId()));
        article.setLock_id(0);


        //插入生成文章id
        articleMapper.insert(article);
        Long articleId = article.getId();
        //tag
        List<TagVo> tags = articleParam.getTags();
        if(!Objects.isNull(tags)) {
            for(TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(articleId);
                articleTag.setTagId(Long.valueOf(tag.getId()));
                articleTagMapper.insert(articleTag);
            }
        }
        //body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(articleId);
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);//再次更新 否则body无法存入数据库

        Map<String, String> map = new HashMap<>();
        map.put("id",articleId.toString());//返回string（精度损失）

        return Result.success(map);
    }

    @Override
    public Result delete(Long articleId) {
        boolean res = true;

        //删除文章
        res |= articleMapper.deleteById(articleId) > 0;
        //删除文章标签
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId, articleId);
        res |= articleTagMapper.delete(articleTagLambdaQueryWrapper) > 0;
        //删除文章评论
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getArticleId, articleId);
        res |= commentMapper.delete(commentLambdaQueryWrapper) >= 0;

        if(res)
            return Result.success(null);
        return Result.fail(200, "删除失败");
    }

    @Override
    public Result listMyArticle(PageParams pageParams) {
        Long id = UserThreadLocal.get().getId();
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.listMyAticle(
                page,
                id,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        List<Article> recordes = articleIPage.getRecords();
        return Result.success(copyList(recordes,true,true));
    }


    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, false, false));
        }
        return articleVoList;
    }
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, isBody, isCategory));
        }
        return articleVoList;
    }

    @Resource
    private CategoryService categoryService;

    //这个方法是主要点是BeanUtils，由Spring提供的，专门用来拷贝的，想Article和articlevo相同属性的拷贝过来返回
    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        BeanUtils.copyProperties(article,articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        if(isTag) {
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if(isAuthor) {
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        if(isBody) {
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if(isCategory) {
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }

    @Resource
    private ArticleBodyMapper articleBodyMapper;

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }
}
