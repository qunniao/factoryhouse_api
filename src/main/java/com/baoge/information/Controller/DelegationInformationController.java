package com.baoge.information.Controller;

import com.baoge.Util.GeneralUtils;
import com.baoge.Util.PO.Result;
import com.baoge.Util.PageUtil;
import com.baoge.Util.ResultUtil;
import com.baoge.Util.SCException;
import com.baoge.information.Dao.DelegationInformationDao;
import com.baoge.information.PO.DelegationInformation;
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

@Api(value = "/delegationInformation", tags = "委托信息")
@RestController
@RequestMapping(value = "/delegationInformation")
public class DelegationInformationController {

    @Autowired
    private DelegationInformationDao delegationInformationDao;


    @ApiOperation(value = "分页查询地产百科", notes = "分页查询新闻信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contactPhone", value = "联系人电话")
    })
    @GetMapping("/pageDelegationInformation")
    @RequestMapping(value = "/pageDelegationInformation", method = RequestMethod.POST)
    public Result<Page<DelegationInformation>> pageDelegationInformation(PageUtil page, @ApiIgnore("DelegationInformation") DelegationInformation delegationInformation){
        Pageable pageable = PageRequest.of(page.getPageNum() - 1,page.getPageSize(),Sort.Direction.ASC,"createTime");
        Page<DelegationInformation> gymShopPage = delegationInformationDao.findAll(new Specification<DelegationInformation>() {
            @Override
            public Predicate toPredicate(Root<DelegationInformation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                //GymShopPO gymShopPO, Root<GymShopPO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder
                return queryFilterExcute(delegationInformation,root,criteriaQuery,criteriaBuilder);
            }
        },pageable);
        return ResultUtil.success(gymShopPage);
    }


    @ApiOperation(value = "添加委托", notes = "参数参考DelegationInformation对象")
    @PostMapping(value = "/addDelegationInformation")
    @RequestMapping(value = "/addDelegationInformation", method = RequestMethod.POST)
    Result<DelegationInformation> addDelegationInformation(DelegationInformation delegationInformation, HttpServletRequest request) throws Exception {
        delegationInformation.setCreateTime(new Date());
        delegationInformationDao.save(delegationInformation);
        return ResultUtil.success(true);
    }

    @ApiOperation(value = "删除委托信息", notes = "DelegationInformation主键")
    @DeleteMapping("/deleteDelegationInformation")
    @ApiImplicitParam(name = "did", value = "DelegationInformation主键", required = true)
    @RequestMapping(value = "/deleteDelegationInformation", method = RequestMethod.POST)
    public Result<DelegationInformation> deleteDelegationInformation(@ApiIgnore("DelegationInformation")DelegationInformation delegationInformation) {
        delegationInformationDao.delete(delegationInformation);
        return ResultUtil.success(true);
    }

    public Predicate queryFilterExcute(DelegationInformation delegationInformation,Root<DelegationInformation> root,CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder){
        List<Predicate> list = new ArrayList<Predicate>();

        if(GeneralUtils.notEmpty(delegationInformation.getContactPhone())){
            list.add(criteriaBuilder.like(root.get("contactPhone").as(String.class), "%"+delegationInformation.getContactPhone()+"%"));
        }

        Predicate[] p = new Predicate[list.size()];

        return criteriaBuilder.and(list.toArray(p));
    }

}
