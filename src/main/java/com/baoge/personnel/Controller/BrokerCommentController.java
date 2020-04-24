package com.baoge.personnel.Controller;

import com.baoge.Util.Enums.ResultEnum;
import com.baoge.Util.GeneralUtils;
import com.baoge.Util.PO.Result;
import com.baoge.Util.PageUtil;
import com.baoge.Util.ResultUtil;
import com.baoge.Util.SCException;
import com.baoge.personnel.Dao.BrokerCommentDao;
import com.baoge.personnel.Dao.PersonalDao;
import com.baoge.personnel.PO.BrokerCommentPO;
import com.baoge.personnel.PO.UserPO;
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
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(value = "/brokerCommentController", tags = "经纪人评论信息API")
@RestController
@RequestMapping(value = "/brokerCommentController")
public class BrokerCommentController {

    @Autowired
    private BrokerCommentDao brokerCommentDao;

    @Autowired
    private PersonalDao personalDao;

    @ApiOperation(value = "分页查询经纪人评论信息", notes = "参数参考BrokerCommentPO对象")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户主键 int类型 必须传", required = true),
    })
    @GetMapping("/pageBrokerComment")
    @RequestMapping(value = "/pageBrokerComment", method = RequestMethod.POST)
    public Result<Page<BrokerCommentPO>> pageBrokerComment(PageUtil page, @ApiIgnore("BrokerCommentPO")BrokerCommentPO brokerComment) {
        Pageable pageable = PageRequest.of(page.getPageNum() - 1,page.getPageSize(),Sort.Direction.ASC, "bcid");
        Page<BrokerCommentPO> gymShopPage= brokerCommentDao.findAll(new Specification<BrokerCommentPO>(){
            @Override
            public Predicate toPredicate(Root<BrokerCommentPO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder){
                return queryFilterExcute(brokerComment,root,criteriaQuery,criteriaBuilder);
            };
        },pageable);
        return ResultUtil.success(gymShopPage);
    }
    @ApiOperation(value = "添加经纪人评论信息", notes = "参数参考BrokerCommentPO对象")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户主键 int", required = true),
    })
    @PostMapping(value = "/addBrokerComment")
    @RequestMapping(value = "/addBrokerComment", method = RequestMethod.POST)
    Result<BrokerCommentPO> addBrokerComment(BrokerCommentPO brokerCommentPO, HttpServletRequest request) throws SCException {
        UserPO user = personalDao.queryByUid(brokerCommentPO.getUid());
        if(user == null){
            throw new SCException(ResultEnum.NULL_TABLE);
        }
        brokerCommentPO.setCreateTime(new Date());
        brokerCommentDao.save(brokerCommentPO);

        return ResultUtil.success(brokerCommentPO);
    }

    @ApiOperation(value = "删除经纪人评论信息", notes = "参数参考BrokerCommentPO对象")
    @ApiImplicitParam(name = "bcid", value = "BrokerCommentPO对象主键 int类型", required = true)
    @DeleteMapping("/deleteBrokerComment")
    @RequestMapping(value = "/deleteBrokerComment", method = RequestMethod.POST)
    public Result<BrokerCommentPO> deleteBrokerComment(@ApiIgnore("BrokerCommentPO")BrokerCommentPO brokerCommentPO) {
        brokerCommentDao.delete(brokerCommentPO);
        return ResultUtil.success(true);
    }

    public Predicate queryFilterExcute(BrokerCommentPO brokerComment,Root<BrokerCommentPO> root,CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder){
        List<Predicate> list = new ArrayList<Predicate>();
        Predicate[] p = null;
        if(GeneralUtils.notEmpty(brokerComment.getUid())){
            list.add(criteriaBuilder.equal(root.get("uid").as(int.class), brokerComment.getUid()));
            p = new Predicate[list.size()];
        }

        return criteriaBuilder.and(list.toArray(p));
    }
}
