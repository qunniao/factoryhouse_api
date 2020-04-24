package com.baoge.personnel.Controller;

import com.baoge.Util.*;
import com.baoge.Util.Enums.ResultEnum;
import com.baoge.Util.PO.Result;
import com.baoge.personnel.Dao.PersonalDao;
import com.baoge.personnel.Dao.RealNameSystemDao;
import com.baoge.personnel.PO.RealNameSystem;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Api(value = "/realNameSystem", tags = "实名制审核api")
@RestController
@RequestMapping(value = "/realNameSystem")
public class RealNameSystemController {

    @Autowired
    private RealNameSystemDao realNameSystemDao;

    @Autowired
    private PersonalDao personalDao;

    @RequestMapping(value = "/pageRealNameSystem", method = RequestMethod.POST)
    @ApiOperation(value = "分页查询实名制审批", notes = "参数参考RealNameSystem对象")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user.userPhone", value = "用户电话")
    })
    @GetMapping("/pageRealNameSystem")
    public Result<Page<RealNameSystem>> pageRealNameSystem(PageUtil page, @ApiIgnore("RealNameSystem")RealNameSystem realNameSystem) {
        Pageable pageable = PageRequest.of(page.getPageNum() - 1,page.getPageSize(),Sort.Direction.ASC, "rnid");
        Page<RealNameSystem> gymShopPage= realNameSystemDao.findAll(new Specification<RealNameSystem>(){
            @Override
            public Predicate toPredicate(Root<RealNameSystem> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder){
                return queryFilterExcute(realNameSystem,root,criteriaQuery,criteriaBuilder);
            };
        },pageable);
        return ResultUtil.success(gymShopPage);
    }

    @ApiOperation("根据用户查询认证信息")
    @GetMapping("/queryRealNameSystemByUid")
    @ApiImplicitParam(name = "uid", value = "User(用户表)主键", dataType = "int", required = true)
    @RequestMapping(value = "/queryRealNameSystemByUid", method = RequestMethod.POST)
    public Result<List<RealNameSystem>> queryRealNameSystemByUid(int uid) {
        return ResultUtil.success(realNameSystemDao.findAllByUid(uid));
    }


    @ApiOperation(value = "添加实名制", notes = "参数参考RealNameSystem对象")
    @PostMapping(value = "/addRealNameSystem")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "关联的用户ID", required = true),
            @ApiImplicitParam(name = "type", value = "审批类型 1:个人认证 2:企业认证 ", required = true),
            @ApiImplicitParam(name = "realName", value = "实名制姓名", required = true),
            @ApiImplicitParam(name = "identityCard", value = "身份证号码", required = true),
            @ApiImplicitParam(name = "identityCardImgFile", value = "上传身份证图片 ", required = true),
            @ApiImplicitParam(name = "businessName", value = "企业名称 ", required = false),
            @ApiImplicitParam(name = "enterpriseCertificationFile", value = "企业资质认证图片", required = false),
    })
    @RequestMapping(value = "/addRealNameSystem", method = RequestMethod.POST)
    Result<RealNameSystem> addRealNameSystem(@ApiIgnore("RealNameSystem")RealNameSystem realNameSystem,
                                             @RequestParam(value = "identityCardImgFile")MultipartFile identityCardImgFile,
                                             @RequestParam(value = "enterpriseCertificationFile",required = false)MultipartFile enterpriseCertificationFile)throws Exception{

        UserPO user = personalDao.queryByUid(realNameSystem.getUid());
        if(user == null){
            throw new SCException(ResultEnum.NULL_USER);
        }
        if(user.getRealNameReview() == 3){
            throw new SCException(ResultEnum.REPEAT_DATA);
        }
        realNameSystemDao.updateByUidForIsEnd(realNameSystem.getUid());
        FastDFSClient fastDFSClient = new FastDFSClient();
        String identityCardImgUrl = fastDFSClient.uploadUtil(identityCardImgFile);
        if(enterpriseCertificationFile!=null){
            String enterpriseCertificationUrl = fastDFSClient.uploadUtil(enterpriseCertificationFile);
            realNameSystem.setEnterpriseCertification(enterpriseCertificationUrl);
        }
        realNameSystem.setIdentityCardImg(identityCardImgUrl);
        realNameSystem.setIsEnd(1);
        realNameSystemDao.save(realNameSystem);
        user.setRealNameReview(3);
        personalDao.save(user);
        return ResultUtil.success(realNameSystem);
    }

    @ApiOperation(value = "更改审批状态", notes = "参数参考RealNameSystem对象")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rnid", value = "ID"),
            @ApiImplicitParam(name = "isEnd", value = " 1:未认证 2:认证 "),
    })
    @RequestMapping(value = "/updateRealNameSystem", method = RequestMethod.POST)
    @PutMapping("/updateRealNameSystem")
    @Transactional
    public Result<RealNameSystem> updateRealNameSystem(RealNameSystem realNameSystem)throws Exception {
        RealNameSystem entity = realNameSystemDao.getOne(realNameSystem.getRnid());
        UserPO user = personalDao.queryByUid(entity.getUid());
        if(StringUtils.isEmpty(entity)||realNameSystem.getRnid() == 0){
            throw new SCException(ResultEnum.NULL_TABLE);
        }
        if(realNameSystem.getIsEnd()==2){
            entity.setIsEnd(2);
            user.setRealNameReview(entity.getType());
        }else if (realNameSystem.getIsEnd() == 1){
            entity.setIsEnd(3);
            user.setRealNameReview(0);
        }
        personalDao.save(user);
        realNameSystemDao.save(entity);

        return ResultUtil.success(true);
    }


    public Predicate queryFilterExcute(RealNameSystem realNameSystem,Root<RealNameSystem> root,CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder){
        List<Predicate> list = new ArrayList<Predicate>();

        if (GeneralUtils.notEmpty(realNameSystem.getUser())&&GeneralUtils.notEmpty(realNameSystem.getUser().getUserPhone())) {
            Join<Task,UserPO> join = root.join("user", JoinType.LEFT);
            list.add(criteriaBuilder.like(join.get("userPhone").as(String.class), "%"+realNameSystem.getUser().getUserPhone()+"%"));
        }

        Predicate[] p = new Predicate[list.size()];

        return criteriaBuilder.and(list.toArray(p));
    }
}
