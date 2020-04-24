package com.baoge.Pay.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
@Data
@Entity
@Table(name = "walletorder", catalog ="baoge")
@ApiModel(value = "walletOrder对象", description = "订单详情表")
public class WalletOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "woid")
    @ApiModelProperty(value = "WalletOrder主键", name = "woid;", example = "1")
    private int woid;

    @Column(name = "wid")
    @ApiModelProperty(value = "钱包ID", name = "wid", example = "1")
    private int wid;

    @Column(name = "uid")
    @ApiModelProperty(value = "用户ID", name = "uid", example = "1")
    private int uid;

    @Column(name = "plid")
    @ApiModelProperty(value = "商品外键", name = "plid", example = "1")
    private Integer plid;

    @ApiModelProperty(value = "订单编号", name = "orderNumber;")
    @Column(name = "orderNumber")
    private String orderNumber;

    @ApiModelProperty(value = "订单金额", name = "orderMoney;")
    @Column(name = "orderMoney")
    private double orderMoney;

    @ApiModelProperty(value = "订单名称", name = "orderName;")
    @Column(name = "orderName")
    private String orderName;

    @ApiModelProperty(value = "商品描述", name = "description")
    @Column(name = "description")
    private String description;

    @ApiModelProperty(value = "支付宝订单号", name = "outTradeNo;")
    @Column(name = "outTradeNo")
    private String outTradeNo;

    @ApiModelProperty(value = "订单类型 1:充值,2:开通会员", name = "type")
    @Column(name = "type")
    private int type;

    @ApiModelProperty(value = "支付类型 1:钱包购买,2:支付宝支付", name = "paymentType")
    @Column(name = "paymentType")
    private int paymentType;

    @ApiModelProperty(value = "订单状态 1:支付成功,2:普通到账(不支持退款),3:未付款,4:交易关闭", name = "status")
    @Column(name = "status")
    private int status;

    @ApiModelProperty(value = "创建时间", name = "createTime")
    @Column(name = "createTime")
    private Date createTime;

    @ApiModelProperty(value = "成功支付时间", name = "successTime")
    @Column(name = "successTime")
    private Date successTime;
}
