package com.baoge.data.Controller;

import com.baoge.Util.PO.Result;
import com.baoge.Util.ResultUtil;
import com.baoge.data.Dao.ModuleDao;
import com.baoge.data.PO.ModulePO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ApiIgnore
@RestController
@RequestMapping(value = "/modules")
public class ModuleController {

    @Autowired
    private ModuleDao moduleDao;

    @ApiIgnore
    @ApiOperation("根据用户和角色获得页面列表数据")
    @RequestMapping(value = "/findAllModule", method = RequestMethod.POST)
    public Result<List<ModulePO>> findAllModule(HttpServletRequest request) {
        return ResultUtil.success(moduleDao.findAllByOrderByLevelAscOrderAsc());
    }
}