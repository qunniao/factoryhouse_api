package com.baoge.information.Controller;

import com.baoge.Util.PO.Result;
import com.baoge.Util.ResultUtil;
import com.baoge.information.Dao.ParkStoreHouseTypeDao;
import com.baoge.information.PO.ParkStoreHouseType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "/parkStoreHouseType", tags = "园区户型")
@RestController
@RequestMapping(value = "/parkStoreHouseType")
public class ParkStoreHouseTypeController {

    @Autowired
    private ParkStoreHouseTypeDao parkStoreHouseTypeDao;


    @ApiOperation("根据主键查询园区户型信息")
    @GetMapping("/queryParkStoreHouseTypeByPid")
    @ApiImplicitParam(name = "pid", value = "园区主键")
    @RequestMapping(value = "/queryParkStoreHouseTypeByPid", method = RequestMethod.POST)
    public Result<ParkStoreHouseType> queryParkStoreHouseTypeByPid(int pid) {
        ParkStoreHouseType parkStoreHouse = parkStoreHouseTypeDao.queryByPid(pid);
        return ResultUtil.success(parkStoreHouse);
    }


    @ApiOperation(value = "添加园区户型信息", notes = "参数参考ParkStoreHouseType对象")
    @PostMapping(value = "/addParkStoreHouseType")
    @RequestMapping(value = "/addParkStoreHouseType", method = RequestMethod.POST)
    Result<ParkStoreHouseType> addParkStoreHouseType(List<ParkStoreHouseType> parkStoreHouseType, HttpServletRequest request) throws Exception{
        parkStoreHouseTypeDao.saveAll(parkStoreHouseType);
        return ResultUtil.success(parkStoreHouseType);
    }


    @ApiOperation(value = "删除园区户型信息", notes = "ParkStoreHouseType主键")
    @ApiImplicitParam(name = "ptid", value = "ParkStoreHouseType主键", required = true)
    @DeleteMapping("/deleteParkStoreHouseType")
    @RequestMapping(value = "/deleteParkStoreHouseType", method = RequestMethod.POST)
    public Result<ParkStoreHouseType> deleteParkStoreHouseType(@ApiIgnore("ParkStoreHouseType")ParkStoreHouseType parkStoreHouseType) {
        parkStoreHouseTypeDao.delete(parkStoreHouseType);
        return ResultUtil.success(true);
    }


}
