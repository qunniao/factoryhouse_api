package com.baoge.Pay.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "wallet", catalog ="baoge")
@ApiModel(value = "Wallet对象", description = "钱包")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wid")
    @ApiModelProperty(value = "Wallet主键", name = "wid;", example = "1")
    private int wid;

    @Column(name = "uid")
    @ApiModelProperty(value = "用户ID", name = "uid", example = "1")
    private int uid;

    @ApiModelProperty(value = "余额", name = "balance;", example = "10.21")
    @Column(name = "balance")
    private double balance;

    @ApiModelProperty(value = "状态 0:初始化,1:正常", name = "status;", example = "10.21")
    @Column(name = "status")
    private int status;

    @Column(name = "payPassWord")
    @ApiModelProperty(value = "用户支付密码", name = "payPassWord", example = "1")
    private String payPassWord;

}
