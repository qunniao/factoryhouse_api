package com.baoge.personnel.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@ApiIgnore
@Table(name = "realnamesystem", catalog ="baoge")
@ApiModel(value = "RealNameSystem对象", description = "实名制审核")
public class RealNameSystem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rnid")
    @ApiModelProperty(value = "RealNameSystem主键", name = "rnid;")
    private int rnid;

    @Column(name = "uid")
    @ApiModelProperty(value = "关联的用户ID", name = "uid")
    private int uid;

    @Column(name = "type")
    @ApiModelProperty(value = "审批类型 1:个人认证 2:企业认证", name = "type")
    private int type;

    @Column(name = "realName")
    @ApiModelProperty(value = "实名制姓名", name = "realName")
    private String realName;

    @Column(name = "identityCard")
    @ApiModelProperty(value = "身份证号码", name = "identityCard")
    private String identityCard;

    @Column(name = "identityCardImg")
    @ApiModelProperty(value = "上传身份证图片", name = "identityCardImg")
    private String identityCardImg;

    @Column(name = "businessName")
    @ApiModelProperty(value = "企业名称", name = "businessName")
    private String businessName;

    @Column(name = "enterpriseCertification")
    @ApiModelProperty(value = "企业资质认证图片", name = "enterpriseCertification")
    private String enterpriseCertification;

    @Column(name = "isEnd")
    @ApiModelProperty(value = "是否认证 1:未认证 2:认证 3:失效", name = "isEnd")
    private Integer isEnd;

    @OneToOne(targetEntity = UserPO.class)
    @JoinColumn(name = "uid",insertable = false, updatable = false)
    private UserPO user;

    /*@Transient
    private UserPO user;*/
}
