package com.baoge.information.PO;

import com.baoge.data.PO.PictureStoragePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@Entity
@Table(name = "parkstore", catalog ="baoge")
@ApiModel(value = "ParkStore对象", description = "园区表")
public class ParkStorePO implements Serializable {
    @Id
    @Column(name = "pid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "园区id", name = "pid", example = "1")
    private int pid;

    @Column(name = "title")
    @ApiModelProperty(value = "标题", name = "title", example = "1",required=true)
    private String title;

    @Column(name = "uid")
    @ApiModelProperty(value = "创建用户ID", name = "uid",required=true)
    private int uid;

    @Column(name = "allBuildingsNum")
    @ApiModelProperty(value = "总栋数", name = "allBuildingsNum", example = "12", required = true)
    private int allBuildingsNum;

    @Column(name = "ahtStructure")
    @ApiModelProperty(value = "建筑结构 0:不限 1:其他 2:砖木结构 3:砖混结构 4:钢架结构 5:钢混结构", name = "ahtStructure", example = "钢架结构", required = true)
    private int ahtStructure;

    @Column(name = "fireRating")
    @ApiModelProperty(value = "消防等级", name = "fireRating", example = "一级", required = true)
    private String fireRating;

    @Column(name = "productionCertificate")
    @ApiModelProperty(value = "产证", name = "productionCertificate", example = "xxxx", required = true)
    private String productionCertificate;

    @Column(name = "parkAddress")
    @ApiModelProperty(value = "园区地址", name = "parkAddress", example = "xxx地址", required = true)
    private String parkAddress;

    @Column(name = "policy")
    @ApiModelProperty(value = "园区政策", name = "policy", example = "xxxx政策", required = true)
    private String policy;

    @Column(name = "matching")
    @ApiModelProperty(value = "园区配套", name = "matching", example = "园区配套说明", required = true)
    private String matching;

    @Column(name = "introduce")
    @ApiModelProperty(value = "园区介绍", name = "introduce", example = "xxxxx", required = true)
    private String introduce;

    @Column(name = "parkprice")
    @ApiModelProperty(value = "价格", name = "parkprice", example = "sss", required = true)
    private int parkprice;

    @Column(name = "priceArea")
    @ApiModelProperty(value = "建筑面积", name = "priceArea", example = "200.23", required = true)
    private int priceArea;

    @Column(name = "createTime")
    @ApiModelProperty(value = "创建时间", name = "createTime", example = "2019-03-19 06:01:46")
    private Date createTime;

    @Column(name = "deliveryTime")
    @ApiModelProperty(value = "交付时间", name = "deliveryTime", example = "2019-03-19", required = true)
    private Date deliveryTime;

    @Column(name = "latitude")
    @ApiModelProperty(value = "经度", name = "latitude", example = "213.456", required = true)
    private double latitude;

    @Column(name = "longitude")
    @ApiModelProperty(value = "纬度", name = "longitude", example = "4564.879", required = true)
    private double longitude;

    @Column(name = "suitableBusiness")
    @ApiModelProperty(value = "适合企业", name = "suitableBusiness", example = "电子商务办公,企业总部办公", required = true)
    private String suitableBusiness;

    @Column(name = "contact")
    @ApiModelProperty(value = "联系人", name = "contact", example = "王先生", required = true)
    private String contact;

    @Column(name = "contactPhone")
    @ApiModelProperty(value = "联系人电话", name = "contactPhone", example = "13874565112", required = true)
    private String contactPhone;

    @ApiModelProperty(value = "1启用2禁用", name = "status;", example = "2019-12-01 12:01:01")
    @Column(name = "status")
    private Integer status;

    @Column(name = "titlePicture")
    @ApiModelProperty(value = "标题图片", name = "titlePicture")
    private String titlePicture;

    @OneToMany(fetch = FetchType.LAZY)
    @ApiModelProperty(hidden = true)
    @Where(clause="type = 2")
    @JoinColumn(name = "primaryid", referencedColumnName = "pid")
    private List<PictureStoragePO> pictureStorage;


    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "pid", referencedColumnName = "pid")
    private List<ParkStoreHouseType> parkStoreHouseType;



}
