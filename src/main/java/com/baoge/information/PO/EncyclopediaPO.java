package com.baoge.information.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "encyclopedia", catalog ="baoge")
@ApiModel(value = "EncyclopediaPO对象", description = "查询地产百科")
public class EncyclopediaPO implements Serializable {
    @Id
    @Column(name = "elid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "EncyclopediaPO主键", name = "bpid")
    private int elid;

    @Column(name = "createName")
    @ApiModelProperty(value = "创建用户名字", name = "createName", required = true)
    private String createName;

    @Column(name = "createId")
    @ApiModelProperty(value = "创建用户ID", name = "createId", required = true)
    private int createId;

    @Column(name = "title")
    @ApiModelProperty(value = "百科标题(提问问题)", name = "title", required = true)
    private String title;

    @Column(name = "createTime")
    @ApiModelProperty(value = "创建时间", name = "createTime")
    private Date createTime;

    @Column(name = "isEnd")
    @ApiModelProperty(value = "是否解决 1:未解决 2:已解决", name = "isEnd")
    private Integer isEnd;

    @Column(name = "type")
    @ApiModelProperty(value = "百科类型 1:企业经营 2:投资融资 3:政策法规 4:厂房装修 5:地产交易", name = "type",required = true)
    private int type;

    @Column(name = "content")
    @ApiModelProperty(value = "详情", name = "content",required = true)
    private String content;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "elid", referencedColumnName = "elid")
    private List<EncyclopediaAnswerPO> EncyclopediaAnswer;
}
