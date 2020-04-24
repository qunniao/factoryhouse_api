package com.baoge.information.PO;

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
@Table(name = "collectioninformation", catalog ="baoge")
@ApiModel(value = "CollectionInformation对象", description = "用户收藏信息")
public class CollectionInformation implements Serializable {

    @Id
    @Column(name = "ciid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "CollectionInformation主键", name = "ciid", example = "1")
    private int ciid;

    @Column(name = "uid")
    @ApiModelProperty(value = "创建用户ID", name = "uid", required = true)
    private int uid;

    @Column(name = "primaryid")
    @ApiModelProperty(value = "关联表ID", name = "primaryid", required = true)
    private int primaryid;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "fid", referencedColumnName = "primaryid")
    @ApiModelProperty(value = "求租求购的厂房,仓库,土地的ID", name = "fid", required = true)
    @NotFound(action = NotFoundAction.IGNORE)
    private List<FplHousePO> fplHouseList;

    @Column(name = "createTime")
    @ApiModelProperty(value = "创建时间", name = "createTime")
    private Date createTime;

    @Column(name = "type")
    @ApiModelProperty(value = "类型 1:出租出售 2:求租求购", name = "type", example = "1",hidden = true)
    private int type;


}
