package com.baoge.information.Controller;


import com.baoge.Util.*;
import com.baoge.Util.Enums.ResultEnum;
import com.baoge.Util.PO.Result;
import com.baoge.Util.PO.Validation;
import com.baoge.data.Dao.PictureStorageDao;
import com.baoge.data.PO.PictureStoragePO;
import com.baoge.information.Dao.ParkStoreDao;
import com.baoge.information.PO.ParkStoreHouseType;
import com.baoge.information.PO.ParkStorePO;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Api(value = "/parkStore", tags = "园区api")
@RestController
@RequestMapping(value = "/parkStore")
public class ParkStoreController {

    @Autowired
    private PictureStorageDao pictureStorageDao;

    @Autowired
    private ParkStoreDao parkStoreDao;

    @ApiOperation(value = "分页查询园区的数据", notes = "分页查询园区数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parkAddress", value = "查询地区", required = false),
            @ApiImplicitParam(name = "title", value = "标题名称(模糊查询)", required = false),
            @ApiImplicitParam(name = "status", value = "查询启用禁用 (非管理接口不要使用 默认查询开启状态园区) ", required = false),
    })
    @GetMapping("/pageParkStore")
    @RequestMapping(value = "/pageParkStore", method = RequestMethod.POST)
    public Result<Page<ParkStorePO>> pageParkStore(PageUtil page,@ApiIgnore("ParkStorePO") ParkStorePO parkStorePO){
        Pageable pageable = PageRequest.of(page.getPageNum() - 1,page.getPageSize(),Sort.Direction.ASC,"pid");
        Page<ParkStorePO> gymShopPage = parkStoreDao.findAll(new Specification<ParkStorePO>() {
            @Override
            public Predicate toPredicate(Root<ParkStorePO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                //GymShopPO gymShopPO, Root<GymShopPO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder
               return queryFilterExcute(parkStorePO,root,criteriaQuery,criteriaBuilder);
            }
        },pageable);
        return ResultUtil.success(gymShopPage);
    }

    @ApiOperation("根据主键查询园区信息")
    @GetMapping("/queryParkStoreByPid")
    @ApiImplicitParam(name = "pid", value = "园区主键")
    @RequestMapping(value = "/queryParkStoreByPid", method = RequestMethod.POST)
    public Result<ParkStorePO> queryParkStoreByPid(int pid) {
        ParkStorePO parkStorePO = parkStoreDao.queryByPid(pid);
        return ResultUtil.success(parkStorePO);
    }

    @ApiOperation(value = "添加园区信息", notes = "参数参考ParkStorePO对象")
    @PostMapping(value = "/addParkStore")
    @RequestMapping(value = "/addParkStore", method = RequestMethod.POST)
    Result<ParkStorePO> addParkStore(ParkStorePO parkStorePO,
                                     @RequestParam(value = "pictureStorage")List<MultipartFile> pictureStorage,
                                     HttpServletRequest request) throws Exception{

        parkStorePO.setStatus(1);
        parkStorePO.setCreateTime(new Date());
        parkStoreDao.save(parkStorePO);

        FastDFSClient fastDFSClient = new FastDFSClient();
        List<String> urls = fastDFSClient.uploadUtil(pictureStorage);

        if(urls!=null&&urls.size()>0){
            List<PictureStoragePO> PictureStoragePOs = new ArrayList<PictureStoragePO>() ;
            for(String url:urls) {
                PictureStoragePO pictureStoragePO = new PictureStoragePO();
                pictureStoragePO.setCreateTime(new Date());
                pictureStoragePO.setType(2);
                pictureStoragePO.setPrimaryid(parkStorePO.getPid());
                pictureStoragePO.setCreateUserId(parkStorePO.getUid());
                pictureStoragePO.setUrl(url);
                PictureStoragePOs.add(pictureStoragePO);
            }
            pictureStorageDao.saveAll(PictureStoragePOs);
        }
        return ResultUtil.success(parkStorePO);
    }






    @ApiOperation(value = "根据主键更新园区信息", notes = "参数参考BuyingPurchasePOe对象")
    @RequestMapping(value = "/updateParkStore", method = RequestMethod.POST)
    @ApiImplicitParam(name = "pid", value = "ParkStorePO主键", required = true)
    @PutMapping(value = "/updateParkStore")
    Result<Boolean> updateParkStore(ParkStorePO parkStorePO) {

        ParkStorePO parkStorePOO = parkStoreDao.getOne(parkStorePO.getPid());

        BeanUtil.copyProperties(parkStorePO, parkStorePOO, Arrays.asList("pid", "createTime","status"));

        parkStoreDao.save(parkStorePOO);
        return ResultUtil.success(true);
    }


    @ApiOperation(value = "更新园区启用禁用(上架下架功能)", notes = "参数参考ParkStorePO对象")
    @PutMapping("/updateParkStoreByStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "ParkStorePO主键", required = true),
            @ApiImplicitParam(name = "status", value = "1启用2禁用", required = true)
    })
    @RequestMapping(value = "/updateParkStoreByStatus", method = RequestMethod.POST)
    @Transactional
    public Result<ParkStorePO> updateParkStoreByStatus(int pid,int status)throws Exception {
        ParkStorePO parkStorePO = parkStoreDao.getOne(pid);
        if(StringUtils.isEmpty(parkStorePO)||parkStorePO.getPid() == 0){
            throw new SCException(ResultEnum.NULL_TABLE);
        }
        parkStorePO.setStatus(status);
        parkStoreDao.save(parkStorePO);

        return ResultUtil.success(true);
    }

    @ApiOperation(value = "删除园区信息", notes = "ParkStorePO主键")
    @ApiImplicitParam(name = "pid", value = "ParkStorePO主键", required = true)
    @DeleteMapping("/deleteParkStore")
    @RequestMapping(value = "/deleteParkStore", method = RequestMethod.POST)
    public Result<ParkStorePO> deleteParkStore(@ApiIgnore("ParkStorePO")ParkStorePO parkStorePO) {
        parkStoreDao.delete(parkStorePO);
        return ResultUtil.success(true);
    }

    public Predicate queryFilterExcute(ParkStorePO parkStorePO,Root<ParkStorePO> root,CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder){
        List<Predicate> list = new ArrayList<Predicate>();

        if(!GeneralUtils.notEmpty(parkStorePO.getStatus())){
            list.add(criteriaBuilder.equal(root.get("status").as(int.class), 1));
        }else if (parkStorePO.getStatus() != 0){
            list.add(criteriaBuilder.equal(root.get("status").as(int.class), parkStorePO.getStatus()));
        }

        if(GeneralUtils.notEmpty(parkStorePO.getParkAddress())){
            list.add(criteriaBuilder.like(root.get("parkAddress").as(String.class), "%"+parkStorePO.getParkAddress()+"%"));
        }

        if(GeneralUtils.notEmpty(parkStorePO.getTitle())){
            list.add(criteriaBuilder.like(root.get("title").as(String.class), "%"+parkStorePO.getTitle()+"%"));
        }

        Predicate[] p = new Predicate[list.size()];

        return criteriaBuilder.and(list.toArray(p));
    }
}
