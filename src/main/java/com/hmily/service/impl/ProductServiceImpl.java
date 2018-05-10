package com.hmily.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.hmily.common.Const;
import com.hmily.common.ResponseCode;
import com.hmily.common.ServerResponse;
import com.hmily.dao.CategoryMapper;
import com.hmily.dao.ProductMapper;
import com.hmily.pojo.Category;
import com.hmily.pojo.Product;
import com.hmily.service.ICategoryService;
import com.hmily.service.IProductService;
import com.hmily.util.DateTimeUtil;
import com.hmily.util.PropertiesUtil;
import com.hmily.vo.ProductDetailVo;
import com.hmily.vo.ProductListVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("iProductService")
@Slf4j
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;

    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if(product == null){
            return ServerResponse.createByErrorMessage("新增或更新商品参数不正确");
        }
        /* 因为前端是传了一组用“,”分隔的图片地址，要把第一个图片赋值为主图的url*/
        if(StringUtils.isNotBlank(product.getSubImages())){
            String[] subImagesArray = product.getSubImages().split(",");
            if(subImagesArray.length > 0){
                product.setMainImage(subImagesArray[0]);
            }
        }
        if(product.getId() == null){
            //新增
            int row = productMapper.insert(product);
            if(row > 0){
                return ServerResponse.createBySuccess("新增产品成功");
            }
            else {
                return ServerResponse.createByErrorMessage("新增产品失败");
            }
        }
        else {
            //更新
            int row = productMapper.updateByPrimaryKeySelective(product);
            if(row > 0){
                return ServerResponse.createBySuccess("更新产品成功");
            }
            else {
                return ServerResponse.createByErrorMessage("更新产品失败");
            }
        }
    }

    @Override
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
        if(productId == null || status == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("修改产品销售状态成功");
        }
        return ServerResponse.createByErrorMessage("修改产品销售状态失败");
    }

    @Override
    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId) {
        if(productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    /**
     * 补全商品详细信息
     *
     * @param product
     * @return
     */
    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());                  //唯一id
        productDetailVo.setSubtitle(product.getSubtitle());     //副标题
        productDetailVo.setPrice(product.getPrice());           //价格
        productDetailVo.setMainImage(product.getMainImage());   //主图
        productDetailVo.setSubImages(product.getSubImages());   //小图
        productDetailVo.setCategoryId(product.getCategoryId()); //所属品类id
        productDetailVo.setDetail(product.getDetail());         //详细描述
        productDetailVo.setName(product.getName());             //商品名称
        productDetailVo.setStatus(product.getStatus());         //商品状态
        productDetailVo.setStock(product.getStock());           //库存

        //图片url的前缀
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));

        //根据品类id获取品类信息，如果没有找到该品类信息就把该商品的父id置为0
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            productDetailVo.setParentCategoryId(0);//默认根节点
        } else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

        return  productDetailVo;
    }

    @Override
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectList();
        ArrayList<ProductDetailVo> productDetailVoArrayList = Lists.newArrayList();
        for (Product p: productList) {
            productDetailVoArrayList.add(assembleProductDetailVo(p));
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productDetailVoArrayList);
        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize) {
        if(StringUtils.isNotBlank(productName)){
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }

        String pId = null;
        if(StringUtils.isNotBlank(productId.toString())){
            pId = new StringBuilder().append("%").append(productId.toString()).append("%").toString();
        }
        PageHelper.startPage(pageNum, pageSize);

        List<Product> productList = productMapper.selectByNameAndProductId(productName, pId);

        ArrayList<ProductDetailVo> productDetailVoArrayList = Lists.newArrayList();
        for (Product p: productList) {
            productDetailVoArrayList.add(assembleProductDetailVo(p));
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productDetailVoArrayList);
        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }
        if (product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    @Override
    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        List<Integer> categoryIdList = new ArrayList<Integer>();
        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)) {
                //没有该分类,并且还没有关键字,这个时候返回一个空的结果集,不报错
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVo> productListVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVoList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            //找到这个分类及其下级分类
            categoryIdList = iCategoryService.selectCategoryAndChildrenById(category.getId()).getData();
        }

        if (StringUtils.isNotBlank(keyword)) {
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }

        PageHelper.startPage(pageNum, pageSize);
        //排序处理
        if (StringUtils.isNotBlank(orderBy)) {
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");     //判断是根据价格的升序还是降序进行排序，前端传过来的价格_升序 、价格_降序
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);    //PageHelper的排序用法
            }
        }

        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword) ? null : keyword, categoryIdList.size() == 0 ? null : categoryIdList);

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product : productList) {
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }
}
