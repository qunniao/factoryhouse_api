package com.baoge.information.PO;

import com.baoge.personnel.PO.UserPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "buyingpurchase", catalog ="baoge")
@ApiModel(value = "BuyingPurchasePO对象", description = "状态为求租求购的厂房,仓库,土地")
public class BuyingPurchasePO implements Serializable {

    @Id
    @Column(name = "bpid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "BuyingPurchase主键", name = "bpid")
    private int bpid;

    @Column(name = "uid")
    @ApiModelProperty(value = "创建用户ID", name = "uid")
    private int uid;

    @Column(name = "productId")
    @ApiModelProperty(value = "房源编号", name = "productId", example = "RU9M3VPB")
    private String productId;

    @Column(name = "region")
    @ApiModelProperty(value = "区域信息", name = "region", example = "浙江省_余杭镇_鑫鑫园区",required = true)
    private String region;

    @Column(name = "type")
    @ApiModelProperty(value = "类别 (0初始,1厂房 2 仓库 3土地)", name = "type",required = true)
    private int type;

    @Column(name = "status")
    @ApiModelProperty(value = "二类(0:初始,1:求租,2:求购)", name = "status",required = true)
    private int status;

    @Column(name = "areaCap")
    @ApiModelProperty(value = "求租求购面积上限", name = "areaCap",required = true)
    private int areaCap;

    @Column(name = "areaLower")
    @ApiModelProperty(value = "求租求购面积下限", name = "areaLower",required = true)
    private int areaLower;

    @Column(name = "functionalUse")
    @ApiModelProperty(value = "功能用途", name = "functionalUse",required = true)
    private String functionalUse;

    @Column(name = "singleLayerArea")
    @ApiModelProperty(value = "单层面积", name = "singleLayerArea",required = true)
    private int singleLayerArea;

    @Column(name = "supportingDemand")
    @ApiModelProperty(value = "配套需求", name = "supportingDemand",required = true)
    private String supportingDemand;

    @Column(name = "title")
    @ApiModelProperty(value = "求租求购标题", name = "title",required = true)
    private String title;

    @Column(name = "layerHeight")
    @ApiModelProperty(value = "楼层高度（单位米）", name = "layerHeight",required = true)
    private int layerHeight;

    @Column(name = "loadBearing")
    @ApiModelProperty(value = "承重（单位吨）", name = "loadBearing",required = true)
    private int loadBearing;

    @Column(name = "minimumCharge")
    @ApiModelProperty(value = "最小电量（单位KVA）", name = "minimumCharge",required = true)
    private int minimumCharge;

    @Column(name = "detailedDescription")
    @ApiModelProperty(value = "详情描述", name = "detailedDescription", example = "厂房位于xx工业园,独栋.......",required = true)
    private String detailedDescription;

    @Column(name = "contact")
    @ApiModelProperty(value = "联系人", name = "contact", example = "王先生",required = true)
    private String contact;

    @Column(name = "contactPhone")
    @ApiModelProperty(value = "联系人电话", name = "contactPhone", example = "13874565112",required = true)
    private String contactPhone;

    @Column(name = "createTime")
    @ApiModelProperty(value = "发布时间", name = "createTime", example = "2019-03-19")
    private Date createTime;

    @Column(name = "isEnd")
    @ApiModelProperty(value = "上架下架功能  1:开启  2:禁用", name = "isEnd", example = "true/false")
    private Integer isEnd;

    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName="uid",insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private UserPO user;

}
