package com.baoge.login.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author 王志鹏
 * @title: Loginlog
 * @projectName baoge
 * @description: TODO
 * @date 2019/3/27 11:13
 */
@Data
@Entity
@Table(name = "loginlog", catalog = "baoge")
@ApiModel(value = "Loginlog对象", description = "登录日志")
public class Loginlog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "loginlog表主键", name = "lid", example = "1")
    @Column(name = "lid")
    private int lid;

    @ApiModelProperty(value = "用户编号", name = "userid", example = "MTD1fB53190325")
    @Column(name = "userid")
    private String userid;

    @ApiModelProperty(value = "令牌", name = "token", example = "kOto0ZIagtA7fpKKBTYskw==")
    @Column(name = "token")
    private String token;

    @ApiModelProperty(value = "手机设备唯一号", name = "fid", example = "")
    @Column(name = "deviceId")
    private String deviceId;

    @ApiModelProperty(value = "登录时间", name = "logTime", example = "2019-03-21 12:01:30")
    @Column(name = "logTime")
    private Timestamp logTime;

}
