package com.baoge.personnel.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "user", catalog = "baoge")
@ApiModel(value = "UserPO对象", description = "人员信息")
public class UserPO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    @ApiModelProperty(value = "user主键", name = "uid;", example = "1")
    private int uid;

    @ApiModelProperty(value = "昵称", name = "userName;", example = "小王")
    @Column(name = "userName")
    private String userName;

    @ApiModelProperty(value = "电话", name = "userPhone;", example = "112345687811")
    @Column(name = "userPhone")
    private String userPhone;

    @ApiModelProperty(value = "性别0男1女", name = "userSex;", example = "1")
    @Column(name = "userSex")
    private int userSex;

    @ApiModelProperty(value = "密码", name = "userPassword;", example = "123456")
    @Column(name = "userPassword")
    private String userPassword;

    @ApiModelProperty(value = "创建时间", name = "createTime", example = "2019-12-01 12:01:01")
    @Column(name = "createTime")
    private Timestamp createTime;

    @ApiModelProperty(value = "0有效1无效", name = "status;", example = "2019-12-01 12:01:01")
    @Column(name = "status")
    private int status;

    @ApiModelProperty(value = "极光用户名", name = "jiguangUsername;", example = "xxx")
    @Column(name = "jiguangUsername")
    private String jiguangUsername;

    @ApiModelProperty(value = "极光密码", name = "jiguangPassword;", example = "xxx")
    @Column(name = "jiguangPassword")
    private String jiguangPassword;

    @ApiModelProperty(value = "头像url", name = "avatarUrl;", example = "2019-12-01 12:01:01")
    @Column(name = "avatarUrl")
    private String avatarUrl;

    @ApiModelProperty(value = "实名制审核(0:未审核,1:用户认证,2:企业认证,3:正在审核)", name = "realNameReview;", example = "2019-12-01 12:01:01")
    @Column(name = "realNameReview")
    private int realNameReview;

    @ApiModelProperty(value = "用户类型(1:个人,2:中介,3:园区)", name = "type;", example = "1")
    @Column(name = "type")
    private int type;

    @ApiModelProperty(value = "负责区域", name = "mainArea;", example = "1")
    @Column(name = "mainArea")
    private String mainArea;

    @ApiModelProperty(value = "个人说明", name = "introduction;", example = "1")
    @Column(name = "introduction")
    private String introduction;


    @Transient
    private boolean isEnd = false;

    public boolean getIsEnd(){
        return this.isEnd;
    }
}
