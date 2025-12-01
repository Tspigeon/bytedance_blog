package com.ty.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ty.domain.pojo.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagMapper extends BaseMapper<Tag> {


    /**
     * 根据文章id查询标签列表
     * @param articleId
     * @return
     */
    List<Tag> selectTagsByArticleId(Long articleId);

    /**
     * 查询最热的标签前n条
     * @param limit
     * @return
     */
    List<Long> findHotsTagIds(int limit);

    List<Tag> findTagsByTagIds(List<Long> tagIds);
}