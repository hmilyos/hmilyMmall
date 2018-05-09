package com.hmily.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailVo {
    private Integer  id;
    private Integer categoryId;
    private String name;
    private String subtitle;
    private String mainImage;
    private String subImages;
    private String detail;
    private BigDecimal price;
    private Integer stock;
    private Integer status;

    //实体Product的时间为Date，传到前端要转一下，用string类型
    private String createTime;  //
    private String updateTime;

    /* 数据库不需要存的两个字段*/
    private String imageHost;   //图片的地址的前缀
    private Integer parentCategoryId;

}
