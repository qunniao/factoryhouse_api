package com.baoge.information.Controller;


import com.baoge.Util.Enums.ResultEnum;
import com.baoge.Util.PO.Result;
import com.baoge.Util.ResultUtil;
import com.baoge.Util.SCException;
import com.baoge.information.Dao.EncyclopediaAnswerDao;
import com.baoge.information.Dao.EncyclopediaDao;
import com.baoge.information.PO.EncyclopediaAnswerPO;
import com.baoge.information.PO.EncyclopediaPO;
import com.baoge.personnel.Dao.PersonalDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(value = "/encyclopediaAnswer", tags = "百科答案api")
@RestController
@RequestMapping(value = "/encyclopediaAnswer")
public class EncyclopediaAnswerController {

    @Autowired
    private EncyclopediaAnswerDao encyclopediaAnswerDao;
    @Autowired
    private EncyclopediaDao encyclopediaDao;

    @ApiOperation(value = "根据地产百科主键查询百科答案", notes = "BuyingPurchasePO主键")
    @GetMapping("/queryByElid")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "elid", value = "百科主键  int", required = true)
    })
    @RequestMapping(value = "/queryByElid", method = RequestMethod.POST)
    Result<EncyclopediaAnswerPO> queryByElid(@ApiIgnore("EncyclopediaAnswerPO")EncyclopediaAnswerPO encyclopediaAnswerPO){
        List<EncyclopediaAnswerPO> encyclopediaAnswerEntity = encyclopediaAnswerDao.queryByElid(encyclopediaAnswerPO.getElid());
        return ResultUtil.success(encyclopediaAnswerEntity);
    }

    @ApiOperation("根据主键查询地产百科答案信息")
    @GetMapping("/queryEncyclopediaAnswerByEaid")
    @ApiImplicitParam(name = "eaid", value = "百科答案主键")
    @RequestMapping(value = "/queryEncyclopediaAnswerByEaid", method = RequestMethod.POST)
    public Result<EncyclopediaAnswerPO> queryEncyclopediaAnswerByEaid(int eaid) {
        EncyclopediaAnswerPO encyclopediaAnswer = encyclopediaAnswerDao.queryByEaid(eaid);
        return ResultUtil.success(encyclopediaAnswer);
    }


    @ApiOperation(value = "添加百科答案", notes = "参数参考EncyclopediaAnswerPO对象")
    @PostMapping(value = "/addEncyclopediaAnswer")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "elid", value = "百科标题表ID int", required = true),
            @ApiImplicitParam(name = "content", value = "回答内容 string ", required = true)
    })
    @RequestMapping(value = "/addEncyclopediaAnswer", method = RequestMethod.POST)
    Result<EncyclopediaAnswerPO> addEncyclopediaAnswer(EncyclopediaAnswerPO encyclopediaAnswerPO, HttpServletRequest request){
        int uid = Integer.valueOf((String)request.getAttribute("userId"));
        String userName =  (String)request.getSession().getAttribute("userName");
        encyclopediaAnswerPO.setCreateId(uid);
        encyclopediaAnswerPO.setCreateName(userName);
        encyclopediaAnswerPO.setCreateTime(new Date());
        encyclopediaAnswerPO.setIsEnd(1);
        encyclopediaAnswerDao.save(encyclopediaAnswerPO);

        return ResultUtil.success(encyclopediaAnswerPO);
    }

    @ApiOperation(value = "根据主键采纳答案", notes = "参数参考EncyclopediaAnswerPO对象")
    @PutMapping(value = "/updateEncyclopediaAnswer")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "eaid", value = "EncyclopediaAnswerPO主键", required = true)
    })
    @RequestMapping(value = "/updateEncyclopediaAnswer", method = RequestMethod.POST)
    Result<Boolean> updateEncyclopediaAnswer(@ApiIgnore("EncyclopediaAnswerPO")EncyclopediaAnswerPO encyclopediaAnswer) throws SCException{
        EncyclopediaAnswerPO entity = encyclopediaAnswerDao.getOne(encyclopediaAnswer.getEaid());
        int count = encyclopediaAnswerDao.countByElidAndAndIsEnd(entity.getElid(),true);
        if(count>0){
            throw new SCException(ResultEnum.EXISTED_ACCEPTION);
        }
        entity.setIsEnd(2);
        encyclopediaAnswerDao.save(entity);
        EncyclopediaPO encyclopediaPO = encyclopediaDao.getOne(entity.getElid());
        encyclopediaPO.setIsEnd(2);
        encyclopediaDao.save(encyclopediaPO);
        return ResultUtil.success(true);
    }

    @ApiOperation(value = "删除百科答案", notes = "参数参考EncyclopediaAnswerPO对象")
    @DeleteMapping(value = "/deleteEncyclopediaAnswer")
    @ApiImplicitParam(name = "eaid", value = "EncyclopediaAnswerPO主键", required = true)
    @RequestMapping(value = "/deleteEncyclopediaAnswer", method = RequestMethod.POST)
    Result<Boolean> deleteEncyclopediaAnswer(@ApiIgnore("EncyclopediaAnswerPO")EncyclopediaAnswerPO encyclopediaAnswer) {
        EncyclopediaAnswerPO entity = encyclopediaAnswerDao.getOne(encyclopediaAnswer.getEaid());
        if(entity.getIsEnd() == 2 ){
            EncyclopediaPO encyclopedia = encyclopediaDao.getOne(entity.getElid());
            encyclopedia.setIsEnd(1);
            encyclopediaDao.save(encyclopedia);
        }
        encyclopediaAnswerDao.delete(encyclopediaAnswer);
        return ResultUtil.success(true);
    }


}
