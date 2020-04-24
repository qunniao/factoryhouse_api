package com.baoge.Pay.Controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.baoge.Pay.Dao.WalletDao;
import com.baoge.Pay.Dao.WalletOrderDao;
import com.baoge.Pay.PO.AlipayBean;
import com.baoge.Pay.PO.Wallet;
import com.baoge.Pay.PO.WalletOrder;
import com.baoge.Pay.Util.Alipay;
import com.baoge.Pay.Util.AlipayProperties;
import com.baoge.Util.*;
import com.baoge.Util.Enums.ResultEnum;
import com.baoge.data.Dao.ProductListDao;
import com.baoge.data.PO.ProductList;
import com.baoge.personnel.Component.MenberComponent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.baoge.Pay.Util.AlipayProperties.*;

@Api(value = "/order", tags = "支付宝支付接口")
@RestController
@RequestMapping(value = "/order")
public class OrderController {


    @Autowired
    private Alipay alipay;

    @Autowired
    private WalletDao walletDao;

    @Autowired
    private WalletOrderDao walletOrderDao;

    @Autowired
    private MenberComponent menberComponent;

    @Autowired
    private ProductListDao productListDao;

    /**
     * 阿里支付
     * @param
     * @return
     * @throws AlipayApiException
     */
    @PostMapping(value = "alipay")
    @ApiOperation("支付宝支付 HTML5支付")
    @RequestMapping(value = "/alipay", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "交易类型 1:充值,2:开通会员", required = true),
            @ApiImplicitParam(name = "plid", value = "开通会员商品主键"),
            @ApiImplicitParam(name = "money", value = "充值金额"),
    })
    public String alipay(HttpServletRequest request,int type,@RequestParam(required = false)Integer plid,@RequestParam(required = false)Double money) throws Exception {
        int uid = Integer.valueOf((String)request.getAttribute("userId"));
        Wallet wallet = walletDao.queryByUid(uid);
        String orderNum = RandomUtil.getLongUuid() + DateUtil.current("yyMMdd");
        AlipayBean alipayBean = new AlipayBean();
        alipayBean.setOut_trade_no(orderNum);//订单编号

        WalletOrder walletOrder = new WalletOrder();
        walletOrder.setUid(uid);
        walletOrder.setWid(wallet.getWid());
        walletOrder.setOrderNumber(orderNum);
        walletOrder.setType(type);
        walletOrder.setStatus(3);
        walletOrder.setCreateTime(new Date());
        walletOrder.setPaymentType(2);
        if(type == 1){

            walletOrder.setOrderMoney(money);
            walletOrder.setOrderName("钱包充值");
            walletOrder.setDescription("钱包充值");

            alipayBean.setSubject("钱包充值");//商品名称
            alipayBean.setTotal_amount(String.valueOf(money));//商品钱
        }else if (type == 2){
            ProductList productList =  productListDao.getOne(plid);

            walletOrder.setPlid(productList.getPlid());
            walletOrder.setOrderMoney(productList.getBalance());
            walletOrder.setOrderName(productList.getProductName());
            walletOrder.setDescription(productList.getContent());

            alipayBean.setSubject(productList.getProductName());//商品名称
            alipayBean.setTotal_amount(String.valueOf(productList.getBalance()));//商品钱
            alipayBean.setBody(productList.getContent());//商品描述
        }

        String html = alipay.pay(request,alipayBean);
        walletOrderDao.save(walletOrder);
        return html;
    }

    @PostMapping(value = "walletPay")
    @ApiOperation("钱包支付")
    @RequestMapping(value = "/walletPay", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID"),
            @ApiImplicitParam(name = "plid", value = "开通会员商品主键"),
    })
    public Boolean walletPay(int uid,int plid) throws Exception {

        Wallet wallet = walletDao.queryByUid(uid);
        if(wallet == null || wallet.getWid() ==0){
            throw new SCException(ResultEnum.NULL_USER);
        }
        String orderNum = RandomUtil.getLongUuid() + DateUtil.current("yyMMdd");
        ProductList productList =  productListDao.queryByPlid(plid);
        Double spread = Arith.sub(wallet.getBalance(),productList.getBalance());
        if(spread<0 || spread == null){
            throw new SCException(ResultEnum.NOT_BALANCE);
        }
        wallet.setBalance(spread);
        walletDao.save(wallet);
        menberComponent.increaseMemberTime(uid,productList);
        WalletOrder walletOrder = new WalletOrder();
        walletOrder.setUid(uid);
        walletOrder.setWid(wallet.getWid());
        walletOrder.setOrderNumber(orderNum);
        walletOrder.setType(2);
        walletOrder.setStatus(2);
        walletOrder.setCreateTime(new Date());
        walletOrder.setOrderName(productList.getProductName());
        walletOrder.setDescription(productList.getContent());
        walletOrder.setOrderMoney(productList.getBalance());
        walletOrder.setSuccessTime(new Date());
        walletOrder.setPaymentType(1);
        walletOrderDao.save(walletOrder);
        return true;
    }

    @RequestMapping(value = "/alipaySynchronousCallback",method = {RequestMethod.POST,RequestMethod.GET})
    public void alipaySynchronousCallback(HttpServletRequest request,HttpServletResponse response)throws Exception{
        response.sendRedirect("http://127.0.0.1/index.html");
    }


    @RequestMapping(value = "/alipayAsynchronousCallback",method = {RequestMethod.POST,RequestMethod.GET})
    public String alipayAsynchronousCallback(HttpServletRequest request)throws Exception{
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, String> paramsMap = new HashMap<>();
        requestMap.forEach((key, values) -> {
            String strs = "";
            for(String value : values) {
                strs = strs + value;
            }
            paramsMap.put(key, strs);
            System.out.println("key:"+key+" value:"+strs);
        });

        //调用SDK验证签名
        boolean signVerified = false;
        try {
            System.out.println("验证成功,更新状态");
            signVerified=  AlipaySignature.rsaCheckV2(paramsMap,PUBLIC_KEY,CHARSET, SIGN_TYPE);
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            System.out.println("验证失败,不去更新状态");
            e.printStackTrace();
        }
        //交易状态
        String trade_status = paramsMap.get("trade_status");
        // 商户订单号
        String out_trade_no = paramsMap.get("out_trade_no");
        //支付成功时间
        String timestamp = paramsMap.get("gmt_payment");
        // 支付宝交易号
        String trade_no = paramsMap.get("trade_no");

        WalletOrder walletOrder = walletOrderDao.queryByOrderNumber(out_trade_no);
        if(trade_status.equals("WAIT_BUYER_PAY")){
            walletOrder.setStatus(3);
        }else if(trade_status.equals("TRADE_FINISHED")){
            walletOrder.setStatus(2);
        }else if(trade_status.equals("TRADE_SUCCESS")){
            walletOrder.setStatus(1);
        }else if(trade_status.equals("TRADE_CLOSED")){
            walletOrder.setStatus(4);
        }

        if(!trade_status.equals("TRADE_SUCCESS")){
            return "fail";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        walletOrder.setSuccessTime(sdf.parse(timestamp));
        walletOrder.setOutTradeNo(trade_no);
        walletOrderDao.save(walletOrder);

        System.out.println("type："+walletOrder.getType()+" signVerified:"+signVerified);
        if(walletOrder.getType() == 1 ){
            Wallet wallet = walletDao.queryByUid(walletOrder.getUid());
            wallet.setBalance(Arith.add(wallet.getBalance(),walletOrder.getOrderMoney()));
            System.out.println("余额："+wallet.getBalance());
            walletDao.save(wallet);
        }else if(walletOrder.getType() == 2 ){
            ProductList productList =  productListDao.queryByPlid(walletOrder.getPlid());
            menberComponent.increaseMemberTime(walletOrder.getUid(),productList);
            System.out.println("添加会员");
        }

        return "success";

    }


    /**
     *支付宝支付
     * @param type 类型
     * @param plid 商品ID
     * @return
     */
    /*
    @PostMapping(value = "alipayAPP")
    @ApiOperation("支付宝支付 APP支付 返回签名")
    @RequestMapping(value = "/alipayAPP", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "交易类型 1:充值,2:开通会员", required = true),
            @ApiImplicitParam(name = "plid", value = "开通会员商品主键"),
            @ApiImplicitParam(name = "money", value = "充值金额"),
    })
    private String alipayAPP(HttpServletRequest request,int type,@RequestParam(required = false)Integer plid,@RequestParam(required = false)Double money)throws Exception {
        int uid = Integer.valueOf((String)request.getAttribute("userId"));
        String orderNum = RandomUtil.getLongUuid() + DateUtil.current("yyMMdd");
        String sign = null;
        Wallet wallet = walletDao.queryByUid(uid);
        WalletOrder walletOrder = new WalletOrder();
        walletOrder.setUid(uid);
        walletOrder.setWid(wallet.getWid());
        walletOrder.setOrderNumber(orderNum);
        walletOrder.setType(type);
        walletOrder.setStatus(3);
        walletOrder.setCreateTime(new Date());
        walletOrder.setPaymentType(2);
        if(type == 1){
            walletOrder.setOrderMoney(money);
            walletOrder.setOrderName("钱包充值");
            walletOrder.setDescription("钱包充值");
            sign = alipay.buildOrderParamMap(orderNum,money,"钱包充值");
        }else if(type == 2){
            ProductList productList =  productListDao.getOne(plid);

            walletOrder.setPlid(productList.getPlid());
            walletOrder.setOrderMoney(productList.getBalance());
            walletOrder.setOrderName(productList.getProductName());
            walletOrder.setDescription(productList.getContent());
            sign = alipay.buildOrderParamMap(orderNum,productList.getBalance(),productList.getContent());
        }
        walletOrderDao.save(walletOrder);
        return sign;
    }
*/

    /**
     *支付宝支付
     * @param type 类型
     * @param plid 商品ID
     * @return
     */

    @PostMapping(value = "alipayAPP")
    @ApiOperation("支付宝支付 APP支付 返回签名")
    @RequestMapping(value = "/alipayAPP", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "交易类型 1:充值,2:开通会员", required = true),
            @ApiImplicitParam(name = "plid", value = "开通会员商品主键"),
            @ApiImplicitParam(name = "money", value = "充值金额"),
    })
    private String alipayAPP(HttpServletRequest request,int type,@RequestParam(required = false)Integer plid,@RequestParam(required = false)Double money)throws Exception {
        int uid = Integer.valueOf((String)request.getAttribute("userId"));
        String orderNum = RandomUtil.getLongUuid() + DateUtil.current("yyMMdd");
        String sign = null;
        Wallet wallet = walletDao.queryByUid(uid);
        WalletOrder walletOrder = new WalletOrder();
        walletOrder.setUid(uid);
        walletOrder.setWid(wallet.getWid());
        walletOrder.setOrderNumber(orderNum);
        walletOrder.setType(type);
        walletOrder.setStatus(3);
        walletOrder.setCreateTime(new Date());
        walletOrder.setPaymentType(2);
        if(type == 1){
            walletOrder.setOrderMoney(money);
            walletOrder.setOrderName("钱包充值");
            walletOrder.setDescription("钱包充值金额"+money);
            sign = alipay.buildOrderParamMap(orderNum,money,"钱包充值",Constants.APP_NAME+"-钱包充值");
        }else if(type == 2){
            ProductList productList =  productListDao.getOne(plid);

            walletOrder.setPlid(productList.getPlid());
            walletOrder.setOrderMoney(productList.getBalance());
            walletOrder.setOrderName(productList.getProductName());
            walletOrder.setDescription(productList.getContent());
            sign = alipay.buildOrderParamMap(orderNum,productList.getBalance(),productList.getContent(), Constants.APP_NAME+"-开通会员");
        }
        walletOrderDao.save(walletOrder);
        System.out.println("sign:"+sign);

        return sign;
    }


}
