package com.baoge.information.Controller;


import com.baoge.Util.*;
import com.baoge.Util.Enums.ResultEnum;
import com.baoge.Util.PO.Result;
import com.baoge.Util.PO.Validation;
import com.baoge.information.Dao.BuyingPurchaseDao;
import com.baoge.information.PO.BuyingPurchasePO;
import com.baoge.personnel.PO.UserPO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Api(value = "/buyingPurchase", tags = "求租求购的厂房,厂库,土地api")
@RestController
@RequestMapping(value = "/buyingPurchase")
public class BuyingPurchaseController {

    @Autowired
    private BuyingPurchaseDao buyingPurchaseDao;

    @ApiOperation(value = "分页查询求租求购的 厂房/仓库/土地的数据", notes = "参数参考FplHouse对象,支持productId,oneType,towType,provinceName,cityName,regionName条件查询")
    @GetMapping("/pageBuyingPurchase")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "房源编号"),
            @ApiImplicitParam(name = "uid", value = "用户ID"),
            @ApiImplicitParam(name = "type", value = "类别 (0初始,1厂房 2 厂库 3土地)"),
            @ApiImplicitParam(name = "status", value = "二类(0:初始,1:求租,2:求购)"),
            @ApiImplicitParam(name = "upArea", value = "查询面积上限"),
            @ApiImplicitParam(name = "downArea", value = "查询面积下限"),
            @ApiImplicitParam(name = "region", value = "查询地区"),
            @ApiImplicitParam(name = "isEnd", value = "查询启用禁用(非管理页面不要使用)"),
            @ApiImplicitParam(name = "userPhone", value = "查询创建用户手机"),
    })
    @RequestMapping(value = "/pageBuyingPurchase", method = RequestMethod.POST)
    public Result<Page<BuyingPurchasePO>> pageBuyingPurchase(PageUtil page, @ApiIgnore("BuyingPurchasePO") BuyingPurchasePO buyingPurchasePO, @RequestParam(required = false)Integer upArea, @RequestParam(required = false)Integer downArea,@RequestParam(required = false)String userPhone,HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page.getPageNum() - 1,page.getPageSize(),Sort.Direction.ASC, "bpid");
        Page<BuyingPurchasePO> gymShopPage= buyingPurchaseDao.findAll(new Specification<BuyingPurchasePO>(){
            @Override
            public Predicate toPredicate(Root<BuyingPurchasePO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder){
                return queryFilterExcute(buyingPurchasePO,root,criteriaQuery,criteriaBuilder,upArea,downArea,userPhone);
            };
        },pageable);

        return ResultUtil.success(gymShopPage);
    }

    @ApiOperation(value = "根据类型,类别,查询求租求购", notes = "参数参考BuyingPurchasePOe对象")
    @GetMapping("/queryByTypesAndStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "types", value = "类别", required = true),
            @ApiImplicitParam(name = "status", value = "二类", required = true)
    })
    @RequestMapping(value = "/queryByTypesAndStatus", method = RequestMethod.POST)
    Result<List<BuyingPurchasePO>> queryByTypesAndStatus(@ApiIgnore("BuyingPurchasePO")BuyingPurchasePO buyingPurchasePO) {

        List<BuyingPurchasePO> buyingPurchasePOs = buyingPurchaseDao.findAll(new Specification<BuyingPurchasePO>(){
            @Override
            public Predicate toPredicate(Root<BuyingPurchasePO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder){
                return queryFilterExcute(buyingPurchasePO,root,criteriaQuery,criteriaBuilder,null,null,null);
            };
        });
        return ResultUtil.success(buyingPurchasePOs);
    }

    @ApiOperation(value = "根据用户查询求租求购信息", notes = "参数参考BuyingPurchasePOe对象")
    @GetMapping("/queryBuyingPurchaseByUid")
    @ApiImplicitParam(name = "uid", value = "User(用户表)主键", required = true)
    @RequestMapping(value = "/queryBuyingPurchaseByUid", method = RequestMethod.POST)
    Result<List<BuyingPurchasePO>> queryBuyingPurchaseByUid(int uid) {

        List<BuyingPurchasePO> buyingPurchasePOs = buyingPurchaseDao.findAllByUid(uid);
        return ResultUtil.success(buyingPurchasePOs);
    }

    @ApiOperation("根据主键查询求租求购信息")
    @GetMapping("/queryBuyingPurchaseByBpid")
    @ApiImplicitParam(name = "bpid", value = "求租求购主键")
    @RequestMapping(value = "/queryBuyingPurchaseByBpid", method = RequestMethod.POST)
    public Result<BuyingPurchasePO> queryBuyingPurchaseByBpid(int bpid) {
        BuyingPurchasePO buyingPurchasePO = buyingPurchaseDao.queryByBpid(bpid);
        return ResultUtil.success(buyingPurchasePO);
    }

    @ApiOperation(value = "添加求租求购", notes = "参数参考BuyingPurchasePOe对象")
    @PostMapping(value = "/addBuyingPurchase")
    @RequestMapping(value = "/addBuyingPurchase", method = RequestMethod.POST)
    Result<BuyingPurchasePO> addBuyingPurchase(BuyingPurchasePO buyingPurchasePO, HttpServletRequest request) throws SCException{

        if (!RegexUtil.checkByRegular(Validation.MOBILE, buyingPurchasePO.getContactPhone())) {
            throw new SCException(ResultEnum.PHONE_NUMBER_ERRROR);
        }
        buyingPurchasePO.setProductId(RandomUtil.getShortUuid() + DateUtil.current("yyMMdd"));
        buyingPurchasePO.setCreateTime(new Date());
        buyingPurchasePO.setIsEnd(1);
        buyingPurchaseDao.save(buyingPurchasePO);

        return ResultUtil.success(buyingPurchasePO);
    }

    @ApiOperation(value = "根据主键更新求租求购信息", notes = "参数参考BuyingPurchasePOe对象")
    @PutMapping(value = "/updateBuyingPurchase")
    @ApiImplicitParam(name = "bpid", value = "BuyingPurchasePO主键", required = true)
    @RequestMapping(value = "/updateBuyingPurchase", method = RequestMethod.POST)
    Result<Boolean> updateBuyingPurchase(BuyingPurchasePO buyingPurchasePO) {

        BuyingPurchasePO buyingPurchasePO1 = buyingPurchaseDao.getOne(buyingPurchasePO.getBpid());

        BeanUtil.copyProperties(buyingPurchasePO, buyingPurchasePO1, Arrays.asList("bpid", "uid", "productId","createTime","isEnd"));
        return ResultUtil.success(true);
    }

    @ApiOperation(value = "更新求租求购的,厂房/仓库/土地的启用禁用(上架下架功能)", notes = "参数参考BuyingPurchasePO对象")
    @PutMapping(value = "/updateBuyingPurchaseByIsEnd")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bpid", value = "BuyingPurchasePO主键", required = true),
            @ApiImplicitParam(name = "isEnd", value = "是否启用/禁用", required = true)
    })
    @RequestMapping(value = "/updateBuyingPurchaseByIsEnd", method = RequestMethod.POST)
    public Result<BuyingPurchasePO> updateBuyingPurchaseByIsEnd(int bpid,int isEnd)throws Exception {
        BuyingPurchasePO buyingPurchase = buyingPurchaseDao.getOne(bpid);
        if(StringUtils.isEmpty(buyingPurchase)||buyingPurchase.getBpid() == 0){
            throw new SCException(ResultEnum.NULL_TABLE);
        }
        buyingPurchase.setIsEnd(isEnd);
        buyingPurchaseDao.save(buyingPurchase);

        return ResultUtil.success(true);
    }

    @ApiOperation(value = "删除求租求购信息", notes = "参数参考BuyingPurchasePOe对象")
    @ApiImplicitParam(name = "bpid", value = "BuyingPurchasePO主键", required = true)
    @DeleteMapping("/deleteBuyingPurchase")
    @RequestMapping(value = "/deleteBuyingPurchase", method = RequestMethod.POST)
    Result<Boolean> deleteBuyingPurchase(@RequestParam("bpid") int bpid) {

        BuyingPurchasePO buyingPurchasePO1 = buyingPurchaseDao.getOne(bpid);
        buyingPurchaseDao.delete(buyingPurchasePO1);

        return ResultUtil.success(true);
    }
    public Predicate queryFilterExcute(BuyingPurchasePO buyingPurchasePO,Root<BuyingPurchasePO> root,CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder,Integer upArea,Integer downArea,String userPhone){
        List<Predicate> list = new ArrayList<Predicate>();



        if(!GeneralUtils.notEmpty(buyingPurchasePO.getIsEnd())){
            list.add(criteriaBuilder.equal(root.get("isEnd").as(int.class), 1));
        }else if (buyingPurchasePO.getIsEnd() != 0){
            list.add(criteriaBuilder.equal(root.get("isEnd").as(int.class), buyingPurchasePO.getIsEnd()));
        }

        if (GeneralUtils.notEmpty(buyingPurchasePO.getUid()) && buyingPurchasePO.getUid() != 0) {
            list.add(criteriaBuilder.equal(root.get("uid").as(int.class), buyingPurchasePO.getUid()));
        }

        if (GeneralUtils.notEmpty(buyingPurchasePO.getProductId())) {
            list.add(criteriaBuilder.like(root.get("productId").as(String.class), "%"+buyingPurchasePO.getProductId()+"%"));
        }

        if (GeneralUtils.notEmpty(buyingPurchasePO.getType()) && buyingPurchasePO.getType() != 0){
            list.add(criteriaBuilder.equal(root.get("type").as(int.class), buyingPurchasePO.getType()));
        }

        if (GeneralUtils.notEmpty(buyingPurchasePO.getStatus()) && buyingPurchasePO.getStatus() != 0){
            list.add(criteriaBuilder.equal(root.get("status").as(int.class), buyingPurchasePO.getStatus()));
        }

        if(GeneralUtils.notEmpty(upArea)&& GeneralUtils.notEmpty(downArea)){
            list.add(criteriaBuilder.between(root.get("area").as(int.class), upArea, downArea));
        }

        if(GeneralUtils.notEmpty(buyingPurchasePO.getRegion())){
            list.add(criteriaBuilder.like(root.get("region").as(String.class), "%"+buyingPurchasePO.getRegion()+"%"));
        }

        if (GeneralUtils.notEmpty(userPhone)) {
            Join<Task,UserPO> join = root.join("user", JoinType.LEFT);
            list.add(criteriaBuilder.like(join.get("userPhone").as(String.class), "%"+userPhone+"%"));
        }


        Predicate[] p = new Predicate[list.size()];

        return criteriaBuilder.and(list.toArray(p));
    }
}
