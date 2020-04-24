package com.baoge.personnel.Controller;

import com.baoge.Util.*;
import com.baoge.Util.Enums.ResultEnum;
import com.baoge.Util.PO.Result;
import com.baoge.personnel.Dao.AdministratorAccountDao;
import com.baoge.personnel.PO.AdministratorAccount;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Api(value = "/administrator", tags = "管理员api")
@RestController
@RequestMapping(value = "/administrator")
public class AdministratorController {

    @Autowired
    private AdministratorAccountDao administratorAccountDao;

    @ApiOperation(value = "分页查询管理人员账号")
    @ApiImplicitParams({
    })
    @GetMapping("/pageAdministratorAccount")
    @RequestMapping(value = "/pageAdministratorAccount", method = RequestMethod.POST)
    public Result<Page<AdministratorAccount>> pageAdministratorAccount(PageUtil page, @ApiIgnore("AdministratorAccount") AdministratorAccount administratorAccount){
        Pageable pageable = PageRequest.of(page.getPageNum() - 1,page.getPageSize(),Sort.Direction.ASC,"createTime");
        Page<AdministratorAccount> gymShopPage = administratorAccountDao.findAll(new Specification<AdministratorAccount>() {
            @Override
            public Predicate toPredicate(Root<AdministratorAccount> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                //GymShopPO gymShopPO, Root<GymShopPO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder
                return queryFilterExcute(administratorAccount,root,criteriaQuery,criteriaBuilder);
            }
        },pageable);
        return ResultUtil.success(gymShopPage);
    }


    @ApiOperation("添加管理员账号")
    @PostMapping("/addAdministratorAccount")
    @RequestMapping(value = "/addAdministratorAccount", method = RequestMethod.POST)
    public Result<AdministratorAccount> addAdministratorAccount(AdministratorAccount administratorAccount, HttpServletRequest request) throws SCException {
        int aid = Integer.valueOf((String)request.getAttribute("userId"));
        AdministratorAccount admin = administratorAccountDao.queryByAccountNumber(administratorAccount.getAccountNumber());

        if (admin != null) {
            throw new SCException(ResultEnum.EXISTED_USER);
        }

        String EncoderPassWord = Base64Util.Base64UtilEncoder(administratorAccount.getUserPassword());
        administratorAccount.setUserPassword(EncoderPassWord);
        administratorAccount.setCreateTime(new Date());
        administratorAccount.setCreateAid(aid);
        administratorAccount.setIsEnd(1);

        administratorAccountDao.save(administratorAccount);

        return ResultUtil.success(administratorAccount);
    }






    @ApiOperation("根据用户ID更新管理员信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "aid", value = "用户编号", required = true),
            @ApiImplicitParam(name = "userPhone", value = "手机号", required = false),
            @ApiImplicitParam(name = "userName", value = "昵称", required = false),
            @ApiImplicitParam(name = "userPassword", value = "密码", required = false),
    })
    @PutMapping(value = "/updateAdministratorAccount")
    @RequestMapping(value = "/updateAdministratorAccount", method = RequestMethod.POST)
    public Result<Boolean> updateAdministratorAccount(@ApiIgnore("AdministratorAccount")AdministratorAccount administratorAccount) {
        AdministratorAccount entity = administratorAccountDao.getOne(administratorAccount.getAid());
        BeanUtil.copyProperties(administratorAccount, entity, Arrays.asList("createTime","aid","isEnd","userPassword","accountNumber","createAid"));
        if(GeneralUtils.notEmpty(administratorAccount.getUserPassword())){
            entity.setUserPassword(Base64Util.Base64UtilEncoder(administratorAccount.getUserPassword()));
        }
        administratorAccountDao.save(entity);

        return ResultUtil.success(true);

    }

    @ApiOperation(value = "删除管理员")
    @ApiImplicitParam(name = "aid", value = "AdministratorAccount主键", required = true)
    @DeleteMapping("/deleteAdministratorAccount")
    @RequestMapping(value = "/deleteAdministratorAccount", method = RequestMethod.POST)
    public Result<AdministratorAccount> deleteAdministratorAccount(
            HttpServletRequest request,
            @ApiIgnore("AdministratorAccount")AdministratorAccount administratorAccount)throws SCException {
        int aid = Integer.valueOf((String)request.getAttribute("userId"));
        if(aid == administratorAccount.getAid()){
            throw new SCException(ResultEnum.NOT_DELETE);
        }
        administratorAccountDao.delete(administratorAccount);
        return ResultUtil.success(true);
    }



    @ApiOperation(value = "根据主键更新管理员启用禁用", notes = "参数参考AdministratorAccount对象")
    @RequestMapping(value = "/updateAdministratorAccountByIsEnd", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "aid", value = "AdministratorAccount主键", required = true),
            @ApiImplicitParam(name = "isEnd", value = "是否启用/禁用", required = true)
    })
    @PutMapping(value = "/updateAdministratorAccountByIsEnd")
    Result<Boolean> updateAdministratorAccountByIsEnd(int aid,int isEnd,HttpServletRequest request) {
        int id = Integer.valueOf((String)request.getAttribute("userId"));
        if(id == aid){
            throw new SCException(ResultEnum.NOT_UPDATE);
        }
        AdministratorAccount entity = administratorAccountDao.getOne(aid);

        if(StringUtils.isEmpty(entity)||entity.getAid() == 0){
            throw new SCException(ResultEnum.NULL_TABLE);
        }
        entity.setIsEnd(isEnd);
        administratorAccountDao.save(entity);
        return ResultUtil.success(true);
    }


    public Predicate queryFilterExcute(AdministratorAccount administratorAccount, Root<AdministratorAccount> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder){
        List<Predicate> list = new ArrayList<Predicate>();

        Predicate[] p = new Predicate[list.size()];

        return criteriaBuilder.and(list.toArray(p));
    }

}
