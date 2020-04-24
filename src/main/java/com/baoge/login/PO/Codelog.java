package com.baoge.login.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author 王志鹏
 * @title: Codelog
 * @projectName baoge
 * @description: TODO
 * @date 2019/3/22 17:38
 */
@Data
@Entity
@Table(name = "codelog", catalog = "baoge")
@ApiModel(value = "Codelog对象", description = "手机验证码日志表")
public class Codelog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "验证码日志表主键", name = "codeId;", example = "1")
    @Column(name = "codeId")
    private int codeId;

    @ApiModelProperty(value = "手机号", name = "codePhone;", example = "186265846589")
    @Column(name = "codePhone")
    private String codePhone;

    @ApiModelProperty(value = "验证码", name = "code;", example = "123465")
    @Column(name = "code")
    private String code;

    @ApiModelProperty(value = "使用验证码功能类型", name = "type;", example = "0:初始,1:注册,2登录,3.修改密码,4.修改手机号")
    @Column(name = "type")
    private short type;

    @ApiModelProperty(value = "短信接口错误日志", name = "errlog;", example = "")
    @Column(name = "errlog")
    private String errlog;

    @ApiModelProperty(value = "创建时间", name = "creatTime;", example = "")
    @Column(name = "creatTime")
    private Timestamp creatTime;

}
