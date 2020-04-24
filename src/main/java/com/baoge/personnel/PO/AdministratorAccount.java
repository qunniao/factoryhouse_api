package com.baoge.personnel.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "administratoraccount", catalog ="baoge")
@ApiModel(value = "AdministratorAccount对象", description = "系统管理员")
public class AdministratorAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aid")
    @ApiModelProperty(value = "administratorAccount主键", name = "aid;", example = "1")
    private int aid;

    @ApiModelProperty(value = "昵称", name = "userName;", example = "小王", required = true)
    @Column(name = "userName")
    private String userName;

    @ApiModelProperty(value = "电话", name = "userPhone;", example = "13564464646464", required = true)
    @Column(name = "userPhone")
    private String userPhone;

    @ApiModelProperty(value = "登陆账号", name = "accountNumber;", example = "aaaaa", required = true)
    @Column(name = "accountNumber")
    private String accountNumber;

    @ApiModelProperty(value = "登陆密码", name = "userPassword;", example = "qwer", required = true)
    @Column(name = "userPassword")
    private String userPassword;

    @ApiModelProperty(value = "创建时间", name = "createTime;", example = "2050-12-12")
    @Column(name = "createTime")
    private Date createTime;

    @ApiModelProperty(value = "是否禁用 1:启用 2:禁用", name = "isEnd;", example = "true/false")
    @Column(name = "isEnd")
    private Integer isEnd;


    @ApiModelProperty(value = "创建人", name = "createAid;", example = "1", required = true)
    @Column(name = "createAid")
    private int createAid;

    @Transient
    private String token;
}
