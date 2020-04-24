package com.baoge.personnel.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@ApiModel(value = "UserPO对象", description = "人员信息")
public class UserVO {

    @ApiModelProperty(value = "user主键", name = "uid;", example = "1")
    private int uid;

    @ApiModelProperty(value = "昵称", name = "userName;", example = "小王")
    private String userName;

    @ApiModelProperty(value = "电话", name = "userPhone;", example = "112345687811")
    private String userPhone;

    @ApiModelProperty(value = "性别0男1女", name = "userSex;", example = "1")
    private int userSex;

    @ApiModelProperty(value = "密码", name = "userPassword;", example = "123456")
    private String userPassword;

    @ApiModelProperty(value = "余额", name = "balance;", example = "10.21")
    private double balance;

    @ApiModelProperty(value = "创建时间", name = "createTime;", example = "2019-12-01 12:01:01")
    private Timestamp createTime;

    @ApiModelProperty(value = "令牌", name = "token;", example = "RiEptDwy+YYChuA01JLQNw==")
    private String token;

    @ApiModelProperty(value = "是否为会员", name = "token;", example = "true/false")
    private boolean isEnd;

    @ApiModelProperty(value = "极光用户名", name = "jiguangUsername;", example = "xxx")
    private String jiguangUsername;

    @ApiModelProperty(value = "极光密码", name = "jiguangPassword;", example = "xxx")
    private String jiguangPassword;

    @ApiModelProperty(value = "头像url", name = "avatarUrl;", example = "2019-12-01 12:01:01")
    private String avatarUrl;
}
