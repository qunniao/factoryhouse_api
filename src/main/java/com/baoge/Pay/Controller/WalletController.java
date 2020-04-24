package com.baoge.Pay.Controller;

import com.baoge.Pay.Dao.WalletDao;
import com.baoge.Pay.PO.Wallet;
import com.baoge.Util.Base64Util;
import com.baoge.Util.Enums.ResultEnum;
import com.baoge.Util.PO.Result;
import com.baoge.Util.ResultUtil;
import com.baoge.Util.SCException;
import com.baoge.personnel.PO.UserPO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "/wallet", tags = "钱包api")
@RestController
@RequestMapping(value = "/wallet")
public class WalletController {

    @Autowired
    private WalletDao walletDao;

    @ApiOperation(value = "根据用户查询钱包信息", notes = "参数参考Wallet对象")
    @GetMapping("/queryWalletByUid")
    @ApiImplicitParam(name = "uid", value = "User(用户表)主键", required = true)
    @RequestMapping(value = "/queryWalletByUid", method = RequestMethod.POST)
    Result<Wallet> queryWalletByUid(int uid) {
        Wallet wallet = walletDao.queryByUid(uid);
        wallet.setPayPassWord("");
        return ResultUtil.success(wallet);
    }


    @ApiOperation("判断用户是否为初始化钱包")
    @PostMapping("/judgmentWallet")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户主键", required = true),
    })
    @RequestMapping(value = "/judgmentWallet", method = RequestMethod.POST)
    public Result<Boolean> judgmentWallet(int uid) {
        Boolean isEnd = false;
        Wallet wallet = walletDao.queryByUid(uid);
        if(wallet.getStatus() == 0){
            return ResultUtil.success(true);
        }else{
            return ResultUtil.success(false);
        }

    }

    @ApiOperation("判断用户钱包密码")
    @PostMapping("/judgmentPayPassWord")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户主键", required = true),
    })
    @RequestMapping(value = "/judgmentPayPassWord", method = RequestMethod.POST)
    public Result<Boolean> judgmentPayPassWord(int uid,String payPassWord) {
        Wallet wallet = walletDao.queryByUid(uid);
        String EncoderPayPassWord = Base64Util.Base64UtilEncoder(payPassWord);
        if (!EncoderPayPassWord.equals(wallet.getPayPassWord())) {
            throw new SCException(ResultEnum.PASSWORD_ERRROR);
        }
        return ResultUtil.success(true);
    }


    @ApiOperation("根据用户编号更改用户钱包密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户编号", required = true),
            @ApiImplicitParam(name = "payPassWord", value = "钱包密码", required = true)
    })
    @PutMapping(value = "/updateWalletPassWord")
    @RequestMapping(value = "/updateWalletPassWord", method = RequestMethod.POST)
    public Result<Boolean> updateWalletPassWord(@ApiIgnore("Wallet")Wallet wallet) {

        Wallet entity = walletDao.queryByUid(wallet.getUid());
        entity.setPayPassWord(Base64Util.Base64UtilEncoder(wallet.getPayPassWord()));

        walletDao.save(entity);

        return ResultUtil.success(true);
    }

}
