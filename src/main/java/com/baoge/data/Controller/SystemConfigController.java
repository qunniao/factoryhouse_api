package com.baoge.data.Controller;

import com.baoge.Util.GeneralUtils;
import com.baoge.Util.PO.Result;
import com.baoge.Util.ResultUtil;
import com.baoge.data.Dao.SystemConfigDao;
import com.baoge.data.PO.SystemConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(value = "/systemConfig", tags = "系统信息API")
@RestController
@RequestMapping(value = "/systemConfig")
public class SystemConfigController {

    @Autowired
    private SystemConfigDao systemConfigDao;

    @ApiOperation(value = "读取系统信息")
    @GetMapping("/querySystemConfig")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "查询key值", required = false),
    })
    @RequestMapping(value = "/querySystemConfig", method = RequestMethod.POST)
    Result<List<SystemConfig>> querySystemConfig(@ApiIgnore("SystemConfig")SystemConfig systemConfig) {

        List<SystemConfig>  entity = systemConfigDao.findAllByOrderByOrderAsc();
        return ResultUtil.success(entity);
    }

    @ApiOperation(value = "更新系统信息")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/updateSystemConfig", method = RequestMethod.POST,produces="application/json")
    @PutMapping("/updateSystemConfig")
    public Result<Boolean> updateSystemConfig(@ApiIgnore("SystemConfig")@RequestBody List<SystemConfig> systemConfig)throws Exception {
        for (SystemConfig entity : systemConfig){
            systemConfigDao.updateSystemConfigKeyByValue(entity.getKey(),entity.getValue());
        }
        return ResultUtil.success(true);
    }
}


