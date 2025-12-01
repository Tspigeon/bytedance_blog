package com.ty.service;

import com.ty.domain.http.Result;
import com.ty.domain.vo.CategoryVo;

public interface CategoryService {
    CategoryVo findCategoryById(Long categoryId);

    /**
     * 查询所有类别
     * @return
     */
    Result findAll();

    Result findAllDetail();

    Result categoryDetailById(Long id);
}
