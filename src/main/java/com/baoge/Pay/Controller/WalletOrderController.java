package com.baoge.Pay.Controller;

import com.baoge.Pay.Dao.WalletOrderDao;
import com.baoge.Pay.PO.WalletOrder;
import com.baoge.Util.GeneralUtils;
import com.baoge.Util.PO.Result;
import com.baoge.Util.PageUtil;
import com.baoge.Util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Api(value = "/walletOrder", tags = "支付宝订单")
@RestController
@RequestMapping(value = "/walletOrder")
public class WalletOrderController {

    @Autowired
    private WalletOrderDao walletOrderDao;

    @ApiOperation(value = "根据人员分页查询人员消费信息", notes = "参考WalletOrder对象")
    @RequestMapping(value = "/pageWalletOrder", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "user表ID", required = true),
            @ApiImplicitParam(name = "type", value = "订单类型 1:充值,2:开通会员", required = false),
    })
    @GetMapping("/pageWalletOrder")
    public Result<Page<WalletOrder>> pageWalletOrder(PageUtil page, @ApiIgnore("WalletOrder")WalletOrder walletOrder){

        Pageable pageable = PageRequest.of(page.getPageNum() - 1,page.getPageSize(),Sort.Direction.ASC,"woid");
        Page<WalletOrder> gymShopPage = walletOrderDao.findAll(new Specification<WalletOrder>() {
            @Override
            public Predicate toPredicate(Root<WalletOrder> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                return queryFilterExcute(walletOrder,root,criteriaQuery,criteriaBuilder);
            }
        },pageable);
        return ResultUtil.success(gymShopPage);
    }





    @ApiOperation(value = "根据用户查询全部订单信息", notes = "参数参考WalletOrder对象")
    @GetMapping("/queryWalletOrderByUid")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "User(用户表)主键", required = true)
    })
    @RequestMapping(value = "/queryWalletOrderByUid", method = RequestMethod.POST)
    Result<List<WalletOrder>> queryWalletOrderByUid(int uid) {
        return ResultUtil.success(walletOrderDao.findAllByUid(uid));
    }


    @ApiOperation(value = "根据用户查询全部成功的订单信息", notes = "参数参考WalletOrder对象")
    @GetMapping("/querySuccessWalletOrderByUid")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "User(用户表)主键", required = true)
    })
    @RequestMapping(value = "/querySuccessWalletOrderByUid", method = RequestMethod.POST)
    Result<List<WalletOrder>> querySuccessWalletOrderByUid(int uid) {
        return ResultUtil.success(walletOrderDao.findAllByUidAndStatusInOrderByWoidDesc(uid,new int[]{1,2}));
    }

    public Predicate queryFilterExcute(WalletOrder walletOrder, Root<WalletOrder> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder){
        List<Predicate> list = new ArrayList<Predicate>();
        Predicate[] p = null;

        if (GeneralUtils.notEmpty(walletOrder.getType()) && walletOrder.getType() != 0){
            list.add(criteriaBuilder.equal(root.get("type").as(int.class), walletOrder.getType()));
        }

        if (GeneralUtils.notEmpty(walletOrder.getUid()) && walletOrder.getUid() != 0) {
            list.add(criteriaBuilder.equal(root.get("uid").as(int.class), walletOrder.getUid()));
            p = new Predicate[list.size()];
        }
        return criteriaBuilder.and(list.toArray(p));
    }


}
