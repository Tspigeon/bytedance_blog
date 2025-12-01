package com.ty.domain.vo.param;

import com.ty.domain.vo.CategoryVo;
import com.ty.domain.vo.TagVo;
import lombok.Data;

import java.util.List;

@Data
public class ArticleParam {

    private Long id;

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;
}

