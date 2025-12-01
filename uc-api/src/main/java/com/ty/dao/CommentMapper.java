package com.ty.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ty.domain.pojo.Comment;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentMapper extends BaseMapper<Comment> {
}
