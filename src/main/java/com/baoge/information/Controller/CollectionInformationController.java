package com.baoge.information.Controller;

import com.baoge.Util.GeneralUtils;
import com.baoge.Util.PO.Result;
import com.baoge.Util.PageUtil;
import com.baoge.Util.ResultUtil;
import com.baoge.Util.SCException;
import com.baoge.information.Dao.CollectionInformationDao;
import com.baoge.information.PO.CollectionInformation;
import com.baoge.information.PO.NewsPO;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(value = "/collectionInformation", tags = "用户收藏信息api")
@RestController
@RequestMapping(value = "/collectionInformation")
public class CollectionInformationController {


    @Autowired
    private CollectionInformationDao collectionInformationDao;

    @ApiOperation(value = "分页查询用户收藏信息", notes = "分页查询用户收藏信息")
    @ApiImplicitParams({
            //@ApiImplicitParam(name = "type", value = "", required = true),
    })
    @GetMapping("/pageCollectionInformation")
    @RequestMapping(value = "/pageCollectionInformation", method = RequestMethod.POST)
    public Result<Page<CollectionInformation>> pageCollectionInformation(PageUtil page, @ApiIgnore("CollectionInformation") CollectionInformation collectionInformation){
        Pageable pageable = PageRequest.of(page.getPageNum() - 1,page.getPageSize(),Sort.Direction.ASC,"ciid");
        Page<CollectionInformation> gymShopPage = collectionInformationDao.findAll(new Specification<CollectionInformation>() {
            @Override
            public Predicate toPredicate(Root<CollectionInformation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                //GymShopPO gymShopPO, Root<GymShopPO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder
                return queryFilterExcute(collectionInformation,root,criteriaQuery,criteriaBuilder);
            }
        },pageable);
        return ResultUtil.success(gymShopPage);
    }

    @ApiOperation("根据用户查询收藏信息")
    @GetMapping("/queryCollectionInformationByUid")
    @ApiImplicitParam(name = "uid", value = "User(用户表)主键", dataType = "int", required = true)
    @RequestMapping(value = "/queryCollectionInformationByUid", method = RequestMethod.POST)
    public Result<CollectionInformation> queryCollectionInformationByUid(int uid) {
        return ResultUtil.success(collectionInformationDao.findAllByUid(uid));
    }

    @ApiOperation("根据用户id和产品id查询是否已收藏该产品")
    @GetMapping("/queryCollectionInformationByUidPrimaryid")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "User(用户表)主键", dataType = "int", required = true),
            @ApiImplicitParam(name = "primaryid", value = "关联表ID", dataType = "int",required = true)
    })
    @RequestMapping(value = "/queryCollectionInformationByUidPrimaryid", method = RequestMethod.POST)
    public Result<CollectionInformation> queryCollectionInformationByUidPrimaryid(@ApiIgnore("CollectionInformation")CollectionInformation collectionInformation) {
        return ResultUtil.success(collectionInformationDao.findAllByUidAndPrimaryid(collectionInformation.getUid(),collectionInformation.getPrimaryid()));
    }

    @ApiOperation(value = "添加用户收藏信息", notes = "参数参考CollectionInformation对象")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "userId", required = true),
            @ApiImplicitParam(name = "primaryid", value = "关联表ID", required = true)
    })
    @PostMapping("/addCollectionInformation")
    @RequestMapping(value = "/addCollectionInformation", method = RequestMethod.POST)
    Result<CollectionInformation> addCollectionInformation(@ApiIgnore("CollectionInformation")CollectionInformation collectionInformation, HttpServletRequest request) throws SCException {
        List<CollectionInformation> list = collectionInformationDao.findAllByUidAndPrimaryid(collectionInformation.getUid(),collectionInformation.getPrimaryid());
        if(null == list || list.size()==0){
            collectionInformation.setCreateTime(new Date());
            collectionInformation.setType(1);
            collectionInformationDao.save(collectionInformation);
            return ResultUtil.success(collectionInformation);
        }else{
            return ResultUtil.error(-1,"已经收藏过了");
        }

    }

    @ApiOperation(value = "删除收藏信息", notes = "CollectionInformation主键")
    @DeleteMapping("/deleteCollectionInformation")
    @ApiImplicitParam(name = "ciid", value = "CollectionInformation主键", required = true)
    @RequestMapping(value = "/deleteCollectionInformation", method = RequestMethod.POST)
    public Result<CollectionInformation> deleteCollectionInformation(@ApiIgnore("CollectionInformation")CollectionInformation collectionInformation) {
        collectionInformationDao.delete(collectionInformation);
        return ResultUtil.success(true);
    }


    public Predicate queryFilterExcute(CollectionInformation collectionInformation, Root<CollectionInformation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder){
        List<Predicate> list = new ArrayList<Predicate>();

        Predicate[] p = new Predicate[list.size()];

        return criteriaBuilder.and(list.toArray(p));
    }

}
