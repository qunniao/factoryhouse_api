package com.baoge.data.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "picturestorage", catalog ="baoge")
@ApiModel(value = "PictureStoragePO对象", description = "查询图片信息地址 用状态区分（0:初始 1:出租出售  2:园区）")
public class PictureStoragePO implements Serializable {

    @Id
    @Column(name = "psid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "PictureStoragePO主键", name = "psid", example = "1")
    private int psid;

    @Column(name = "primaryid")
    @ApiModelProperty(value = "关联主键", name = "primaryid", example = "1")
    private int primaryid;

    @Column(name = "url")
    @ApiModelProperty(value = "图片地址", name = "url", example = "c:/xxx/xxx.png")
    private String url;

    @Column(name = "type")
    @ApiModelProperty(value = "图片类型（根据不同类型查询不同表）（0:初始 1:出租出售  2:求租求购  3:园区,4:系统图片）", name = "type", example = "1")
    private int type;

    @Column(name = "createTime")
    @ApiModelProperty(value = "创建时间", name = "createTime", example = "2099-99-99")
    private Date createTime;


    @Column(name = "description")
    @ApiModelProperty(value = "图片说明", name = "description", example = "1")
    private String description;

    @Column(name = "createUserId")
    @ApiModelProperty(value = "创建人的用户ID", name = "createUserId", example = "1")
    private int createUserId;

}
