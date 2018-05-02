package com.hmily.pojo;

import lombok.*;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

//@Getter(AccessLevel.PROTECTED)  //设置生成的get方法的作用域，默认是public
@Getter
@Setter
//@Setter(AccessLevel.PRIVATE)  //设置生成的set方法的作用域，默认是public
@NoArgsConstructor  //无参构造器
@AllArgsConstructor //所有参数的构造器
//@Data   //包括了get、set、ToString、EqualsAndHashCode
//@ToString
//@ToString(exclude = "name") //toString时排除掉name这个字段属性
//@EqualsAndHashCode(of = "id")   //重新EqualsAndHashCode方法, id相同就认为是相同的
//@EqualsAndHashCode(of = {"id", "name"})   //重新EqualsAndHashCode方法, id、name相同就认为是相同的
//@EqualsAndHashCode(exclude = "name")   //重新EqualsAndHashCode方法，比较时排除name字段
//@EqualsAndHashCode(exclude = {"name", "status"})   //重新EqualsAndHashCode方法，比较时排除name、status字段
//@Slf4j  //logback日志，可在类中直接使用log，不需要声明
//@Log4j    //log4j日志，可在类中直接使用log，不需要声明
@EqualsAndHashCode(of = "id")
public class Category {
    private Integer id;

    private Integer parentId;

    private String name;

    private Boolean status;

    private Integer sortOrder;

    private Date createTime;

    private Date updateTime;

}