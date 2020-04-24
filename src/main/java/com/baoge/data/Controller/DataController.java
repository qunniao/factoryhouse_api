package com.baoge.data.Controller;

import com.baoge.Util.PO.Result;
import com.baoge.Util.ResultUtil;
import com.baoge.data.Dao.CityDao;
import com.baoge.data.PO.CityPO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(value = "/data", tags = "全国地址API")
@RestController
@RequestMapping(value = "/data")
public class DataController {

    @Autowired
    private CityDao cityDao;

    @ApiOperation("查询全部中国地址信息")
    @GetMapping("/queryAllCity")
    @RequestMapping(value = "/queryAllCity", method = RequestMethod.POST)
    public Result<List<CityPO>> queryAllCity() {

        List<CityPO> cityDaoAll = cityDao.findAll();
        return ResultUtil.success(cityDaoAll);
    }

    @ApiOperation("根据等级查询中国地址信息")
    @GetMapping("/queryCityByLevel")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "level", value = "等级 1省,2市,3区", required = true)
    })
    @RequestMapping(value = "/queryCityByLevel", method = RequestMethod.POST)
        public Result<List<CityPO>> queryCityByLevel(@RequestParam("level")int level) {

        return ResultUtil.success(cityDao.queryByLevel(level));
    }

    @ApiOperation("根据分级名称模糊查询中国地址信息")
    @GetMapping("/queryCityByLikeMergerName")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mergerName", value = "全称", required = true),
            @ApiImplicitParam(name = "level", value = "等级 1省,2市,3区", required = true)
    })
    @RequestMapping(value = "/queryCityByLikeMergerName", method = RequestMethod.POST)
    public Result<List<CityPO>> queryCityByLikeMergerName(@RequestParam("mergerName") String mergerName, @RequestParam("level") int level) {

        return ResultUtil.success(cityDao.queryByMergerNameLikeAndLevel("%" + mergerName + "%", level));
    }

    @ApiOperation("根据pid查询地址信息")
    @GetMapping("/queryCityByPid")
    @ApiImplicitParam(name = "pid", value = "父级id", required = true)
    @RequestMapping(value = "/queryCityByPid", method = RequestMethod.POST)
    public Result<List<CityPO>> queryCityByPid(@RequestParam("pid") Integer pid) {

        return ResultUtil.success(cityDao.queryByPid(pid));
    }
}

