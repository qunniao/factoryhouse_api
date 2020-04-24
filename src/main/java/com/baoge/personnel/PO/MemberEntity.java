package com.baoge.personnel.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "member", catalog ="baoge")
@ApiModel(value = "MemberEntity对象", description = "会员管理")
public class MemberEntity implements Serializable {

    @Id
    @Column(name = "mid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "MemberEntity主键", name = "mid", example = "1")
    private int mid;

    @Column(name = "uid")
    @ApiModelProperty(value = "关联的用户ID", name = "uid", example = "1")
    private int uid;

    @Column(name = "createTime")
    @ApiModelProperty(value = "创建时间", name = "createTime", example = "1")
    private Date createTime;

    @Column(name = "expiryDate")
    @ApiModelProperty(value = "会员到期时间", name = "expiryDate", example = "1")
    private Date expiryDate;
}
