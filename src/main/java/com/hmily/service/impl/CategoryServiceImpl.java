package com.hmily.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hmily.common.ServerResponse;
import com.hmily.dao.CategoryMapper;
import com.hmily.pojo.Category;
import com.hmily.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("iCategoryService")
@Slf4j
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Category selectByPrimaryKey(Integer id) {
        return null;
    }

    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if(StringUtils.isBlank(categoryName) || parentId == null){
            return ServerResponse.createByErrorMessage("参数错误");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);//这个分类是可用的
        int row = categoryMapper.insertSelective(category);
        if(row > 0){
            return ServerResponse.createBySuccess("添加分类成功");
        }
        return ServerResponse.createByErrorMessage("添加分类失败");
    }

    @Override
    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        if(categoryId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("更新品类名字成功");
        }
        return ServerResponse.createByErrorMessage("更新品类名字失败");
    }

    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
        List<Category> categories = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if(CollectionUtils.isEmpty(categories)){
            log.info("未找到id为：{}的子分类", categoryId);
        }
        return  ServerResponse.createBySuccess(categories);
    }

    /**
     * 递归查询本节点的id及孩子节点的id
     * @param categoryId
     * @return
     */
    @Override
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,categoryId);
        ArrayList<Integer> categoryIdList = Lists.newArrayList();
        for (Category category: categorySet) {
            categoryIdList.add(category.getId());
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    //递归算法,算出子节点
    private Set<Category> findChildCategory(Set<Category> categorySet ,Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null){
            categorySet.add(category);
        }
        //查找子节点,递归算法一定要有一个退出的条件
        List<Category> categories = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category c: categories) {
            findChildCategory(categorySet, c.getId());
        }
        return categorySet;
    }
}
