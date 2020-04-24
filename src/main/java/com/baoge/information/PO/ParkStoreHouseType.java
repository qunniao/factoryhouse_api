package com.baoge.information.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "parkstorehousetype", catalog ="baoge")
@ApiModel(value = "ParkStoreHouseType对象", description = "园区户型")
public class ParkStoreHouseType implements Serializable {

    @Id
    @Column(name = "ptid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ParkStoreHouseType主键", name = "ptid", example = "1")
    private int ptid;

    @Column(name = "pid")
    @ApiModelProperty(value = "外键", name = "pid",required=true)
    private int pid;

    @Column(name = "type")
    @ApiModelProperty(value = "类型", name = "type", required = true)
    private String type;

    @Column(name = "priceArea")
    @ApiModelProperty(value = "建筑面积", name = "priceArea", required = true)
    private String priceArea;

    @Column(name = "layerHeight")
    @ApiModelProperty(value = "层高", name = "layerHeight", required = true)
    private String layerHeight;

    @Column(name = "imgUrl")
    @ApiModelProperty(value = "图片地址", name = "imgUrl", required = true)
    private String imgUrl;


}
