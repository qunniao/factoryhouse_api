package com.baoge.data.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "sys_module", catalog = "baoge")
@ApiModel(value = "ModulePO", description = "动态列表页面")
public class ModulePO implements Serializable {

    @Id
    @Column(name = "mid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键", name = "mid", example = "1")
    private int mid;

    @Column(name = "fatherModuleId")
    @ApiModelProperty(value = "父id", name = "fatherModuleId", example = "1")
    private String fatherModuleId;

    @Column(name = "name")
    @ApiModelProperty(value = "名称", name = "name", example = "1")
    private String name;

    @Column(name = "order")
    @ApiModelProperty(value = "排序", name = "order", example = "xxxx")
    private int order;

    @Column(name = "icon")
    @ApiModelProperty(value = "图片", name = "icon", example = "xxxx")
    private String icon;

    @Column(name = "url")
    @ApiModelProperty(value = "url", name = "url", example = "1")
    private String url;

    @Column(name = "level")
    @ApiModelProperty(value = "列表等级", name = "level", example = "1")
    private int level;

}