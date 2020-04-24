package com.baoge.personnel.Controller;

import com.baoge.Pay.Dao.WalletDao;
import com.baoge.Pay.PO.Wallet;
import com.baoge.Util.*;
import com.baoge.Util.Enums.ResultEnum;
import com.baoge.Util.PO.JiguangPO;
import com.baoge.Util.PO.Result;
import com.baoge.login.Dao.CodelogDao;
import com.baoge.login.PO.Codelog;
import com.baoge.personnel.Dao.MenberDao;
import com.baoge.personnel.Dao.PersonalDao;
import com.baoge.personnel.PO.MemberEntity;
import com.baoge.personnel.PO.UserPO;
import com.baoge.personnel.Component.MenberComponent;
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
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Api(value = "/personal", tags = "人员api")
@RestController
@RequestMapping(value = "/personal")
public class PersonalController {

    @Autowired
    private PersonalDao personalDao;

    @Autowired
    private CodelogDao codelogDao;

    @Autowired
    private WalletDao walletDao;

    @Autowired
    private MenberDao memberDao;

    @Autowired
    private MenberComponent menberComponent;


    @ApiOperation(value = "分页查询人员信息", notes = "分页查询人员信息")
    @RequestMapping(value = "/pagePersonal", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "用户类型(1:个人,2:中介,3:园区)"),
            @ApiImplicitParam(name = "mainArea", value = "区域信息"),
    })
    @GetMapping("/pagePersonal")
    public Result<Page<UserPO>> pagePersonal(PageUtil page, @ApiIgnore("UserPO")UserPO userPO){

        Pageable pageable = PageRequest.of(page.getPageNum() - 1,page.getPageSize(),Sort.Direction.ASC,"uid");
        Page<UserPO> gymShopPage = personalDao.findAll(new Specification<UserPO>() {
            @Override
            public Predicate toPredicate(Root<UserPO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                return queryFilterExcute(userPO,root,criteriaQuery,criteriaBuilder);
            }
        },pageable);
        for (UserPO user:gymShopPage.getContent()){
            boolean b = menberComponent.decideMenber(user.getUid());
            user.setEnd(b);
        }
        return ResultUtil.success(gymShopPage);
    }

    @ApiOperation("根据人员主键查询用户")
    @GetMapping("/queryUserByUid")
    @RequestMapping(value = "/queryUserByUid", method = RequestMethod.POST)
    @ApiImplicitParam(name = "uid", value = "用户", required = true)
    public Result<UserPO> queryUserByUid(@RequestParam("uid") int uid) {

        UserPO userPO = personalDao.queryByUid(uid);
        userPO.setUserPassword(null);
        userPO.setEnd(menberComponent.decideMenber(uid));
        return  ResultUtil.success(userPO);
    }

    @ApiOperation("根据手机号查询用户")
    @RequestMapping(value = "/queryByUserPhone", method = RequestMethod.POST)
    @GetMapping("/queryByUserPhone")
    @ApiImplicitParam(name = "userPhone", value = "手机号", required = true)
    public Result<UserPO> queryUserByUserPhone(@RequestParam("userPhone") String userPhone) {

        UserPO userPO = personalDao.queryByUserPhone(userPhone);
        userPO.setEnd(menberComponent.decideMenber(userPO.getUid()));
        userPO.setUserPassword(null);

        return ResultUtil.success(userPO);
    }

    @ApiOperation("添加用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true),
            @ApiImplicitParam(name = "userPhone", value = "电话", required = true),
            @ApiImplicitParam(name = "userSex", value = "性别0男1女", required = true),
            @ApiImplicitParam(name = "userPassword", value = "密码", required = true),
            @ApiImplicitParam(name = "type", value = "用户类型(1:个人,2:中介,3:园区)", required = true)
    })
    @PostMapping(value = "/addUser")
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public Result<Boolean> addUser(HttpServletRequest request,@ApiIgnore("UserPO")UserPO userPO) throws Exception{

        UserPO oldUser = personalDao.queryByUserPhone(userPO.getUserPhone());

        if (oldUser != null) {
            throw new SCException(ResultEnum.USER_PRESENCE);
        }

        userPO.setCreateTime(DateUtil.getDateToTimestamp(new Date()));

        userPO.setUserPassword(Base64Util.Base64UtilEncoder(userPO.getUserPassword()));

        String nameAndPassword = "KF_" + Long.toString(new Date().getTime());

        userPO.setJiguangUsername(nameAndPassword);
        userPO.setJiguangPassword(nameAndPassword);

        JiguangPO jiguangPO = new JiguangPO();
        jiguangPO.setUsreName(nameAndPassword);
        jiguangPO.setUserPassWord(nameAndPassword);

        JgUtil.testCreateGroup(jiguangPO);


        personalDao.save(userPO);

        Wallet wallet = new Wallet();
        wallet.setBalance(0);
        wallet.setUid(userPO.getUid());
        wallet.setStatus(0);
        walletDao.save(wallet);

        return ResultUtil.success(true);
    }

    @ApiOperation("根据用户编号更改用户密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户编号", required = true),
            @ApiImplicitParam(name = "userPassword", value = "密码", required = true)
    })
    @PutMapping(value = "/updateUserPassWord")
    @RequestMapping(value = "/updateUserPassWord", method = RequestMethod.POST)
    public Result<Boolean> updateUserPassWord(@ApiIgnore("UserPO")UserPO userPO) {

        UserPO userPO1 = personalDao.queryByUid(userPO.getUid());
        userPO1.setUserPassword(Base64Util.Base64UtilEncoder(userPO.getUserPassword()));

        personalDao.save(userPO1);

        return ResultUtil.success(true);
    }


    @ApiOperation("更改用户名")
    @PutMapping(value = "/updateUserName")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户编号", required = true),
            @ApiImplicitParam(name = "userName", value = "用户名", required = true)
    })
    @RequestMapping(value = "/updateUserName", method = RequestMethod.POST)
    public Result<Boolean> updateUserName(@ApiIgnore("UserPO")UserPO userPO) {

        UserPO userPO1 = personalDao.queryByUid(userPO.getUid());
        userPO1.setUserName(userPO.getUserName());

        personalDao.save(userPO1);

        return ResultUtil.success(true);
    }

    @ApiOperation("更改用户状态")
    @PutMapping(value = "/updateUserStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户编号", required = true),
            @ApiImplicitParam(name = "status", value = "用户状态", required = true)
    })
    @RequestMapping(value = "/updateUserStatus", method = RequestMethod.POST)
    public Result<Boolean> updateUserStatus(@ApiIgnore("UserPO")UserPO userPO) {

        UserPO userPO1 = personalDao.queryByUid(userPO.getUid());
        userPO1.setStatus(userPO.getStatus());

        personalDao.save(userPO1);

        return ResultUtil.success(true);
    }

    @ApiOperation("根据用户ID更新用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户编号", required = true),
            @ApiImplicitParam(name = "userName", value = "昵称", required = false),
            @ApiImplicitParam(name = "userSex", value = "性别", required = false),
            @ApiImplicitParam(name = "mainArea", value = "负责区域", required = false),
            @ApiImplicitParam(name = "introduction", value = "个人说明", required = false),
    })
    @PutMapping(value = "/updateUser")
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public Result<Boolean> updateUser(@ApiIgnore("UserPO")UserPO userPO) {
        UserPO userPO1 = personalDao.queryByUid(userPO.getUid());
        BeanUtil.copyProperties(userPO, userPO1, Arrays.asList("createTime","uid","status","jiguangUsername","jiguangPassword","realNameReview","userPassword"));
        if(GeneralUtils.notEmpty(userPO.getUserPassword())){
            userPO1.setUserPassword(Base64Util.Base64UtilEncoder(userPO.getUserPassword()));
        }
        personalDao.save(userPO1);

        return ResultUtil.success(true);

    }

    @ApiOperation("根据用户ID更新用户头像")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户编号", required = true),
            @ApiImplicitParam(name = "file", value = "头像图片", required = true),
    })
    @PutMapping(value = "/updateProfilePicture")
    @RequestMapping(value = "/updateProfilePicture", method = RequestMethod.POST)
    public Result<Boolean> updateProfilePicture(@ApiIgnore("UserPO")UserPO userPO,@RequestParam(value = "file")MultipartFile file)throws Exception  {
        if(file==null){
            throw new SCException(ResultEnum.NULL_TABLE);
        }

        UserPO entity = personalDao.queryByUid(userPO.getUid());

        FastDFSClient fastDFSClient = new FastDFSClient();
        String url = fastDFSClient.uploadUtil(file);
        entity.setAvatarUrl(url);

        personalDao.save(entity);

        return ResultUtil.success(true);

    }


    @ApiOperation("根据用户编号更改用户手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userPhone", value = "准备更换的用户手机号", required = true),
            @ApiImplicitParam(name = "uid", value = "用户编号", required = true),
            @ApiImplicitParam(name = "code", value = "验证码", required = true)
    })
    @PutMapping(value = "/updateUserPhone")
    @RequestMapping(value = "/updateUserPhone", method = RequestMethod.POST)
    public Result<Boolean> updateUserPhone(String userPhone,int uid,String code) {

        Codelog codelog = codelogDao.queryTop1ByCodePhoneAndTypeOrderByCodeIdDesc(userPhone,(short)4);

        if (codelog == null) {
            throw new SCException(ResultEnum.NULL_CODE);
        }

        if (!(codelog.getCode()).equals(code)) {
            throw new SCException(ResultEnum.CODE_ERRROR);
        }

        Timestamp creatTime = codelog.getCreatTime();

        long effectiveDate = 5L * 60L * 1000L;// 设置5分钟有效期
        Date afterDate = new Date(creatTime.getTime() + effectiveDate);

        if (!(codelog.getErrlog()).equals("OK")) {
            throw new SCException(ResultEnum.CODE_NOT_OK);
        }

        boolean flag = DateUtil.isEffectiveDate(DateUtil.getDateToTimestamp(new Date()), creatTime, afterDate);

        if (!flag) {
            throw new SCException(ResultEnum.CODE_OVERDUE);
        }

        int count = personalDao.countByUserPhone(userPhone);
        if(count>0){
            throw new SCException(ResultEnum.USER_PRESENCE);
        }
        UserPO user = personalDao.queryByUid(uid);
        user.setUserPhone(userPhone);
        personalDao.save(user);

        return ResultUtil.success(true);
    }

    @ApiOperation("通过手机号和验证码修改密码")
    @PutMapping(value = "/updatePassWordByCode")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userPhone", value = "用户手机号", required = true),
            @ApiImplicitParam(name = "code", value = "验证码", required = true),
            @ApiImplicitParam(name = "passWord", value = "密码", required = true)
    })
    @RequestMapping(value = "/updatePassWordByCode", method = RequestMethod.POST)
    public Result<Boolean> updatePassWordByCode(@RequestParam("userPhone") String userPhone, @RequestParam("code") String code, @RequestParam("passWord") String password) throws SCException{

        Codelog codelog = codelogDao.queryTop1ByCodePhoneOrderByCodeIdDesc(userPhone);

        if (!(codelog.getCode()).equals(code)) {
            throw new SCException(ResultEnum.CODE_ERRROR);
        }

        Timestamp creatTime = codelog.getCreatTime();

        long effectiveDate = 5L * 60L * 1000L;// 设置5分钟有效期
        Date afterDate = new Date(creatTime.getTime() + effectiveDate);

        if (codelog == null) {
            throw new SCException(ResultEnum.NULL_CODE);
        }

        if (!(codelog.getErrlog()).equals("OK")) {
            throw new SCException(ResultEnum.CODE_NOT_OK);
        }

        boolean flag = DateUtil.isEffectiveDate(DateUtil.getDateToTimestamp(new Date()), creatTime, afterDate);

        if (!flag) {
            throw new SCException(ResultEnum.CODE_OVERDUE);
        }

        UserPO userPO = personalDao.queryByUserPhone(userPhone);
        userPO.setUserPassword(Base64Util.Base64UtilEncoder(password));

        personalDao.save(userPO);

        return ResultUtil.success(true);
    }

    @ApiOperation(value = "删除用户", notes = "UserPO主键")
    @ApiImplicitParam(name = "uid", value = "UserPO主键", required = true)
    @DeleteMapping("/deleteUser")
    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public Result<UserPO> deleteUser(@ApiIgnore("UserPO")UserPO userPO) {
        personalDao.delete(userPO);
        return ResultUtil.success(true);
    }
    public Predicate queryFilterExcute(UserPO userPO,Root<UserPO> root,CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder){
        List<Predicate> list = new ArrayList<Predicate>();

        if (GeneralUtils.notEmpty(userPO.getUserPhone())) {
            list.add(criteriaBuilder.equal(root.get("userPhone").as(String.class), userPO.getUserPhone()));
        }

        if (GeneralUtils.notEmpty(userPO.getType())&&userPO.getType()!=0) {
            list.add(criteriaBuilder.equal(root.get("type").as(String.class), userPO.getType()));
        }

        if(GeneralUtils.notEmpty(userPO.getMainArea())){
            list.add(criteriaBuilder.like(root.get("mainArea").as(String.class), "%"+userPO.getMainArea()+"%"));
        }

        Predicate[] p = new Predicate[list.size()];

        return criteriaBuilder.and(list.toArray(p));
    }

    @ApiOperation("根据人员主键查询会员信息")
    @GetMapping("/queryVipByUid")
    @RequestMapping(value = "/queryVipByUid", method = RequestMethod.POST)
    @ApiImplicitParam(name = "uid", value = "用户", required = true)
    public Result<MemberEntity> queryVipByUid(@RequestParam("uid") int uid) {

        MemberEntity modal = memberDao.queryByUid(uid);
        return  ResultUtil.success(modal);
    }

}
