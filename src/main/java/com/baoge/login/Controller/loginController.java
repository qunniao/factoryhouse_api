package com.baoge.login.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baoge.Pay.Dao.WalletDao;
import com.baoge.Pay.PO.Wallet;
import com.baoge.Util.*;
import com.baoge.Util.Enums.ResultEnum;
import com.baoge.Util.PO.JiguangPO;
import com.baoge.Util.PO.Result;
import com.baoge.Util.PO.Validation;
import com.baoge.login.Dao.CodelogDao;
import com.baoge.login.PO.Codelog;
import com.baoge.personnel.Dao.AdministratorAccountDao;
import com.baoge.personnel.Dao.PersonalDao;
import com.baoge.personnel.PO.AdministratorAccount;
import com.baoge.personnel.PO.UserPO;
import com.baoge.personnel.Component.MenberComponent;
import com.baoge.personnel.VO.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

@Api(value = "/login", tags = "登录验证等api")
@RestController
@RequestMapping(value = "/login")
public class loginController {

    @Autowired
    private CodelogDao codelogDao;

    @Autowired
    private PersonalDao personalDao;

    @Autowired
    private MenberComponent menberComponent;

    @Autowired
    private WalletDao walletDao;

    @Autowired
    private AdministratorAccountDao administratorAccountDao;

    @ApiOperation("通过手机号获得验证码")
    @GetMapping("/geCheckCode")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userPhone", value = "用户手机号", required = true),
            @ApiImplicitParam(name = "type", value = "验证类型", required = true)
    })
    @RequestMapping(value = "/geCheckCode", method = RequestMethod.POST)
    public Result<Boolean> geCheckCode(@RequestParam("userPhone") String userPhone, @RequestParam("type") Short type) throws SCException {
        String code = String.valueOf(RandomUtil.getRandom_Six());

        if (!RegexUtil.checkByRegular(Validation.MOBILE, userPhone)) {
            throw new SCException(ResultEnum.PHONE_NUMBER_ERRROR);
        }

        String response = SMSUtil.send_SMS_Verification_Code(code, userPhone);


        JSONObject jsonObject = JSON.parseObject(response);

        String errCode = jsonObject.get("Code").toString();
        if (!errCode.equals("OK")) {
            throw new SCException(ResultEnum.ALIBABA_SMS_ERROR);
        }

        Codelog codelog = new Codelog();

        codelog.setCodePhone(userPhone);
        codelog.setCode(code);
        codelog.setType(type);
        codelog.setCreatTime(DateUtil.getDateToTimestamp(new Date()));
        codelog.setErrlog(errCode);

        codelogDao.save(codelog);



        return  ResultUtil.success(true);
    }

    @ApiOperation("通过手机号和密码登录")
    @GetMapping("/geCheckCode")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userPhone", value = "用户手机号", required = true),
            @ApiImplicitParam(name = "passWord", value = "登陆密码", required = true)
    })
    @RequestMapping(value = "/loginByPassWord", method = RequestMethod.POST)
    public Result<UserVO> loginByPassWord(@RequestParam("userPhone") String userPhone, @RequestParam("passWord") String passWord, HttpServletResponse response, HttpServletRequest request) throws SCException {


        UserPO userPO = personalDao.queryByUserPhone(userPhone);

        UserVO userVO = new UserVO();
        if (userPO == null) {
            throw new SCException(ResultEnum.NULL_USER);
        }
        if(userPO.getStatus() == 1){
            throw new SCException(ResultEnum.REFUSE_USER);
        }
        userPO.setEnd(menberComponent.decideMenber(userPO.getUid()));
        String EncoderPassWord = Base64Util.Base64UtilEncoder(passWord);

        if (!EncoderPassWord.equals(userPO.getUserPassword())) {
            throw new SCException(ResultEnum.PASSWORD_ERRROR);
        }

        BeanUtil.copyProperties(userPO, userVO, Arrays.asList("createTime","userPassword"));
        String token = JwtUtil.sign(userPO.getUserName(),userPO.getUserPhone(),String.valueOf(userPO.getUid()),userPO.getUserPassword(),"user");
        userVO.setToken(token);

        Wallet wallet = walletDao.queryByUid(userVO.getUid());
        if(null == wallet){
            wallet = new Wallet();
            wallet.setBalance(0);
            wallet.setUid(userPO.getUid());
            wallet.setStatus(0);
            walletDao.save(wallet);
        }


        return ResultUtil.success(userVO);
    }
    @ApiOperation("通过手机号和验证码注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userPhone", value = "电话", required = true),
            @ApiImplicitParam(name = "passWord", value = "密码", required = true),
            @ApiImplicitParam(name = "code", value = "短信验证码", required = true),
            @ApiImplicitParam(name = "type", value = "用户类型(1:个人,2:中介,3:园区)", required = true)
    })
    @PostMapping("/registrationUser")
    @RequestMapping(value = "/registrationUser", method = RequestMethod.POST)
    public Result<UserVO> registrationUser(@RequestParam("userPhone") String userPhone,String  passWord,@RequestParam("code") String code,@RequestParam("type")int type,HttpServletRequest request)throws Exception{
        Codelog codelog = codelogDao.queryTop1ByCodePhoneOrderByCodeIdDesc(userPhone);

        Timestamp creatTime = codelog.getCreatTime();
        String codes = codelog.getCode();

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

        if (!codes.equals(code)) {
            throw new SCException(ResultEnum.CODE_ERRROR);
        }
        UserPO userPO = new UserPO();
        UserVO userVO = new UserVO();

        UserPO oldUser = personalDao.queryByUserPhone(userPhone);

        if (oldUser != null) {
            throw new SCException(ResultEnum.USER_PRESENCE);
        }
        userPO.setUserPhone(userPhone);
        userPO.setUserSex(0);
        userPO.setCreateTime(DateUtil.getDateToTimestamp(new Date()));

        userPO.setUserPassword(Base64Util.Base64UtilEncoder(passWord));

        String nameAndPassword = "KF_" + Long.toString(new Date().getTime());
        userPO.setType(type);
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
        BeanUtil.copyProperties(userPO, userVO, Arrays.asList("createTime","userPassword"));

        String token = JwtUtil.sign(userPO.getUserName(),userPO.getUserPhone(),String.valueOf(userPO.getUid()),userPO.getUserPassword(),"user");
        userVO.setToken(token);

        return ResultUtil.success(userVO);
    }

    @ApiOperation("通过手机号和验证码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userPhone", value = "电话", required = true),
            @ApiImplicitParam(name = "code", value = "短信验证码", required = true)
    })
    @GetMapping("/loginByCode")
    @RequestMapping(value = "/loginByCode", method = RequestMethod.POST)
    public Result<UserVO> loginByCode(@RequestParam("userPhone") String userPhone, @RequestParam("code") String code,HttpServletResponse response, HttpServletRequest request) throws Exception {

        Codelog codelog = codelogDao.queryTop1ByCodePhoneOrderByCodeIdDesc(userPhone);

        Timestamp creatTime = codelog.getCreatTime();
        String codes = codelog.getCode();

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

        if (!codes.equals(code)) {
            throw new SCException(ResultEnum.CODE_ERRROR);
        }
        UserPO userPO = personalDao.queryByUserPhone(userPhone);
        UserVO userVO = new UserVO();
        if (userPO == null) throw new SCException(ResultEnum.NULL_USER);
        if (userPO.getStatus() == 1) throw new SCException(ResultEnum.REFUSE_USER);
        userPO.setEnd(menberComponent.decideMenber(userPO.getUid()));
        BeanUtil.copyProperties(userPO, userVO, Arrays.asList("createTime","userPassword"));

        String token = JwtUtil.sign(userPO.getUserName(),userPO.getUserPhone(),String.valueOf(userPO.getUid()),userPO.getUserPassword(),"user");
        userVO.setToken(token);

        Wallet wallet = walletDao.queryByUid(userVO.getUid());
        if(null == wallet){
            wallet = new Wallet();
            wallet.setBalance(0);
            wallet.setUid(userPO.getUid());
            wallet.setStatus(0);
            walletDao.save(wallet);
        }
        return ResultUtil.success(userVO);
    }
    @ApiIgnore
    @ApiOperation("通过管理员账号和密码登录")
    @RequestMapping(value = "/adminLoginByPassWord", method = RequestMethod.POST)
    public Result<AdministratorAccount> adminLoginByPassWord(@RequestParam("accountNumber") String accountNumber, @RequestParam("userPassword") String userPassword, HttpServletResponse response, HttpServletRequest request) throws SCException {

        AdministratorAccount administratorAccount = administratorAccountDao.queryByAccountNumber(accountNumber);

        if (administratorAccount == null) {
            throw new SCException(ResultEnum.NULL_USER);
        }

        if (administratorAccount.getIsEnd() == 2) {
            throw new SCException(ResultEnum.REFUSE_USER);
        }

        String EncoderPassWord = Base64Util.Base64UtilEncoder(userPassword);

        if (!EncoderPassWord.equals(administratorAccount.getUserPassword())) {
            throw new SCException(ResultEnum.PASSWORD_ERRROR);
        }
        String token = JwtUtil.sign(administratorAccount.getUserName(),administratorAccount.getAccountNumber(),String.valueOf(administratorAccount.getAid()),administratorAccount.getUserPassword(),"admin");
        administratorAccount.setToken(token);

        return ResultUtil.success(administratorAccount);
    }

}
