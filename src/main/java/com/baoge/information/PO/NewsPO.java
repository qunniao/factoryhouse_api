package com.baoge.information.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "news", catalog ="baoge")
@ApiModel(value = "NewsPO对象", description = "查询新闻资讯")
public class NewsPO implements Serializable {

    @Id
    @Column(name = "nid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "news主键", name = "nid", example = "1")
    private int nid;

    @Column(name = "createName")
    @ApiModelProperty(value = "创建用户名", name = "createName", example = "1",required=true)
    private String createName;

    @Column(name = "createAid")
    @ApiModelProperty(value = "创建用户ID", name = "createAid", example = "1",required=true)
    private int createAid;

    @Column(name = "createTime")
    @ApiModelProperty(value = "创建时间", name = "createTime", example = "1")
    private Date createTime;

    @Column(name = "type")
    @ApiModelProperty(value = "资讯类型 1:xx有话说 2:监测与分析 3:项目招商 4:园区动态 5:指数分析", name = "type", example = "1",required=true)
    private int type;

    @Column(name = "titlePicture")
    @ApiModelProperty(value = "标题图片路径", name = "titlePicture", example = "1")
    private String titlePicture;

    @Column(name = "title")
    @ApiModelProperty(value = "标题", name = "title", example = "1",required=true)
    private String title;

    @Column(name = "content")
    @ApiModelProperty(value = "新闻内容 (副文本 HTML标签形式)", name = "comment", example = "1",required=true)
    private String content;

    @Column(name = "isEnd")
    @ApiModelProperty(value = "是否可见 1:可见  2:不可见", name = "isEnd")
    private Integer isEnd;

}
