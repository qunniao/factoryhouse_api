package com.baoge.information.PO;


import com.baoge.data.PO.PictureStoragePO;
import com.baoge.personnel.PO.UserPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "fplhouse", catalog ="baoge")
@ApiModel(value = "FplHousePO对象", description = "查询状态为出租出售的厂房,仓库,土地")
public class FplHousePO implements Serializable,Comparable<FplHousePO>{
    @Id
    @Column(name = "fid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "FplHouse主键", name = "fid", example = "1")
    private int fid;

    @Column(name = "uid")
    @ApiModelProperty(value = "创建用户ID", name = "uid", example = "1")
    private int uid;

    @Column(name = "productId")
    @ApiModelProperty(value = "房源编号", name = "productId", example = "RU9M3VPB")
    private String productId;

    @Column(name = "region")
    @ApiModelProperty(value = "区域信息", name = "region", example = "浙江省_余杭镇_鑫鑫园区")
    private String region;

    @Column(name = "types")
    @ApiModelProperty(value = "类别 (0初始,1厂房 2 仓库 3土地)", name = "type", example = "1")
    private int types;

    @Column(name = "status")
    @ApiModelProperty(value = "二类(0:初始,1:出租,2:出售)", name = "status", example = "1")
    private int status;

    @Column(name = "subleaseType")
    @ApiModelProperty(value = "是否分租 0:分租 1不分租", name = "subleaseType", example = "0")
    private int subleaseType;

    @Column(name = "rent")
    @ApiModelProperty(value = "租金/售价(根据类型不同显示不同)", name = "rent", example = "20")
    private int rent;

    @Column(name = "rentUnit")
    @ApiModelProperty(value = "租金/售价 的单位", name = "rentUnit", example = "元/m²/月")
    private String rentUnit;

    @Column(name = "area")
    @ApiModelProperty(value = "面积", name = "area", example = "13500")
    private int area;

    @Column(name = "areaUnit")
    @ApiModelProperty(value = "面积单位", name = "areaUnit")
    private String areaUnit;

    @Column(name = "leasePeriod")
    @ApiModelProperty(value = "租期", name = "leasePeriod", example = "3年")
    private String leasePeriod;

    @Column(name = "title")
    @ApiModelProperty(value = "标题", name = "title", example = "出租黄湖独门独院13500平方米厂房")
    private String title;

    @Column(name = "address")
    @ApiModelProperty(value = "地址", name = "address", example = "五常街道常河路")
    private String address;

    @Column(name = "latitude")
    @ApiModelProperty(value = "经度", name = "latitude", example = "213.456")
    private double latitude;

    @Column(name = "longitude")
    @ApiModelProperty(value = "纬度", name = "longitude", example = "4564.879")
    private double longitude;

    @Column(name = "infrastructure")
    @ApiModelProperty(value = "基础设施", name = "infrastructure", example = "餐饮,住宿,超市,便利店")
    private String infrastructure;

    @Column(name = "peripheralPackage")
    @ApiModelProperty(value = "周边配套", name = "peripheralPackage", example = "商业综合体,政务中心,农贸市场")
    private String peripheralPackage;

    @Column(name = "suitableBusiness")
    @ApiModelProperty(value = "适合企业", name = "suitableBusiness", example = "电子商务办公,企业总部办公")
    private String suitableBusiness;

    @Column(name = "detailedDescription")
    @ApiModelProperty(value = "详情描述", name = "detailedDescription", example = "厂房位于xx工业园,独栋.......")
    private String detailedDescription;

    @Column(name = "buildingStructure")
    @ApiModelProperty(value = "建筑结构 0:不限 1:其他 2:砖木结构 3:砖混结构 4:钢架结构 5:钢混结构", name = "buildingStructure", example = "砖木结构")
    private int buildingStructure;

    @Column(name = "contact")
    @ApiModelProperty(value = "联系人", name = "contact", example = "王先生")
    private String contact;

    @Column(name = "contactPhone")
    @ApiModelProperty(value = "联系人电话", name = "contactPhone", example = "13874565112")
    private String contactPhone;

    @Column(name = "createTime")
    @ApiModelProperty(value = "发布时间", name = "createTime", example = "2019-03-19")
    private Date createTime;

    @Column(name = "titlePicture")
    @ApiModelProperty(value = "标题图片", name = "titlePicture", example = "2019-03-19")
    private String titlePicture;

    @OneToMany(fetch = FetchType.EAGER)
    @Where(clause="type = 1")
    @JoinColumn(name = "primaryid", referencedColumnName = "fid")
    @ApiModelProperty(hidden = true)
    private List<PictureStoragePO> pictureStorage;

    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName="uid",insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private UserPO user;

    @Column(name = "isEnd")
    @ApiModelProperty(value = "启动禁用 1:启用 2:禁用 ", name = "isEnd", example = "c:/xxx/xxx.png")
    private Integer isEnd;


    @Transient
    @ApiModelProperty(value = "商家距离 (仅供距离查询排序时使用 其他接口为null)", name = "distance")
    private Double distance;

    @Override
    public int compareTo(FplHousePO o) {
        double f1=this.distance;
        double f2=o.getDistance();
        return f1>f2?1:f1==f2?0:-1;
    }
}
