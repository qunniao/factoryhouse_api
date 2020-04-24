package com.baoge.data.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_config", catalog ="baoge")
@ApiModel(value = "SystemConfig对象")
public class SystemConfig {

    @Id
    @Column(name = "[key]")
    @ApiModelProperty(value = "参数名称", name = "key")
    private String key;

    @Column(name = "[value]")
    @ApiModelProperty(value = "参数名称对应的值", name = "value")
    private String value;

    @Column(name = "[order]")
    @ApiModelProperty(value = "排序", name = "order")
    private int order;

    @Column(name = "content")
    @ApiModelProperty(value = "补充内容", name = "content")
    private String content;
}
