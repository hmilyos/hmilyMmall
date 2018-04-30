package com.hmily.service;

import com.hmily.common.ServerResponse;
import com.hmily.pojo.Category;

import java.util.List;

public interface ICategoryService {
    Category selectByPrimaryKey(Integer id);

    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId, String categoryName);

    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
