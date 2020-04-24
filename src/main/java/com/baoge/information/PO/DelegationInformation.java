package com.baoge.information.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "delegationinformation", catalog ="baoge")
@ApiModel(value = "DelegationInformation对象", description = "委托信息表")
public class DelegationInformation {

    @Id
    @Column(name = "did")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "DelegationInformation主键", name = "did")
    private int did;

    @Column(name = "uid")
    @ApiModelProperty(value = "创建用户ID", name = "uid",required = true)
    private int uid;

    @Column(name = "type")
    @ApiModelProperty(value = "类别 (0初始,1厂房 2 仓库 3土地)", name = "type",required = true)
    private int type;

    @Column(name = "status")
    @ApiModelProperty(value = "二类(0:初始,1:出租,2:出售,3:求租,4求购)", name = "status",required = true)
    private int status;

    @Column(name = "contact")
    @ApiModelProperty(value = "联系人", name = "contact", example = "王先生",required = true)
    private String contact;

    @Column(name = "contactPhone")
    @ApiModelProperty(value = "联系人电话", name = "contactPhone", example = "13874565112",required = true)
    private String contactPhone;

    @Column(name = "content")
    @ApiModelProperty(value = "详情", name = "content",required = true)
    private String content;

    @Column(name = "createTime")
    @ApiModelProperty(value = "创建时间", name = "createTime")
    private Date createTime;

}
