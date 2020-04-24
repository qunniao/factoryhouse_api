package com.baoge.personnel.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "brokercomment", catalog ="baoge")
@ApiModel(value = "BrokerCommentPO对象", description = "查询经纪人评论信息")
public class BrokerCommentPO implements Serializable {

    @Id
    @Column(name = "bcid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "BrokerCommentPO主键", name = "bcid", example = "1")
    private int bcid;

    @Column(name = "uid")
    @ApiModelProperty(value = "经纪人ID", name = "uid", example = "1", required = true)
    private int uid;

    @Column(name = "createTime")
    @ApiModelProperty(value = "创建时间", name = "createTime", example = "1")
    private Date createTime;

    @Column(name = "userComment")
    @ApiModelProperty(value = "用户评论信息", name = "userComment", example = "1", required = true)
    private String userComment;

    @Column(name = "userRating")
    @ApiModelProperty(value = "用户评论评分（5分制保留一位小数）", name = "userRating", example = "3.6", required = true)
    private double userRating;

    @Column(name = "commentUserID")
    @ApiModelProperty(value = "评论用户ID", name = "commentUserID", example = "1", required = true)
    private int commentUserID;

    @Column(name = "userName")
    @ApiModelProperty(value = "评论人名字", name = "userName", example = "张三", required = true)
    private String userName;

}
