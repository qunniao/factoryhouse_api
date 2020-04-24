package com.baoge.data.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "productlist", catalog ="baoge")
@ApiModel(value = "productList对象", description = "会员商品列表")
public class ProductList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plid")
    @ApiModelProperty(value = "ProductList主键", name = "plid;")
    private int plid;

    @Column(name = "productName")
    @ApiModelProperty(value = "商品名称", name = "productName", required = true)
    private String productName;

    @Column(name = "balance")
    @ApiModelProperty(value = "商品价格", name = "balance", required = true)
    private double balance;

    @Column(name = "type")
    @ApiModelProperty(value = "商品类型 1:会员", name = "type", required = true)
    private int type;

    @Column(name = "content")
    @ApiModelProperty(value = "商品说明", name = "content", required = true)
    private String content;

    @Column(name = "remarks")
    @ApiModelProperty(value = "商品补充", name = "remarks", required = true)
    private String remarks;

    @Column(name = "[order]")
    @ApiModelProperty(value = "排序", name = "order", example = "xxxx", required = true)
    private int order;
}
