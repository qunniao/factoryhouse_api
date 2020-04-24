package com.baoge.information.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "encyclopediaanswer", catalog ="baoge")
@ApiModel(value = "EncyclopediaAnswerPO对象", description = "百科答案")
public class EncyclopediaAnswerPO implements Serializable {

    @Id
    @Column(name = "eaid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "EncyclopediaAnswerPO主键", name = "eaid", example = "1")
    private int eaid;

    @Column(name = "elid")
    @ApiModelProperty(value = "百科标题表ID", name = "elid",required=true)
    private int elid;

    @Column(name = "createName")
    @ApiModelProperty(value = "创建用户名字", name = "createName")
    private String createName;

    @Column(name = "createId")
    @ApiModelProperty(value = "创建用户ID", name = "createId")
    private int createId;

    @Column(name = "createTime")
    @ApiModelProperty(value = "创建时间", name = "createTime")
    private Date createTime;

    @Column(name = "content")
    @ApiModelProperty(value = "回答内容", name = "content",required=true)
    private String content;

    @Column(name = "isEnd")
    @ApiModelProperty(value = "是否被采纳 1:未采纳 2:采纳", name = "isEnd")
    private Integer isEnd;

}
