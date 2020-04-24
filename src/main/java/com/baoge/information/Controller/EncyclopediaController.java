package com.baoge.information.Controller;

import com.baoge.Util.GeneralUtils;
import com.baoge.Util.PO.Result;
import com.baoge.Util.PageUtil;
import com.baoge.Util.ResultUtil;
import com.baoge.Util.SCException;
import com.baoge.information.Dao.EncyclopediaAnswerDao;
import com.baoge.information.Dao.EncyclopediaDao;
import com.baoge.information.PO.EncyclopediaAnswerPO;
import com.baoge.information.PO.EncyclopediaPO;
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
@Api(value = "/encyclopedia", tags = "地产百科api")
@RestController
@RequestMapping(value = "/encyclopedia")
public class EncyclopediaController {

    @Autowired
    private EncyclopediaDao encyclopediaDao;

    @Autowired
    private EncyclopediaAnswerDao encyclopediaAnswerDao;


    @ApiOperation(value = "分页查询地产百科", notes = "分页查询新闻信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "百科类型 int类型 1:企业经营 2:投资融资 3:政策法规 4:厂房装修 5:地产交易", required = false),
            @ApiImplicitParam(name = "title", value = "百科标题 模糊查询", required = false),
    })
    @GetMapping("/pageEncyclopedia")
    @RequestMapping(value = "/pageEncyclopedia", method = RequestMethod.POST)
    public Result<Page<EncyclopediaPO>> pageEncyclopedia(PageUtil page,@ApiIgnore("EncyclopediaPO") EncyclopediaPO encyclopedia){
        Pageable pageable = PageRequest.of(page.getPageNum() - 1,page.getPageSize(),Sort.Direction.ASC,"createTime");
        Page<EncyclopediaPO> gymShopPage = encyclopediaDao.findAll(new Specification<EncyclopediaPO>() {
            @Override
            public Predicate toPredicate(Root<EncyclopediaPO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                //GymShopPO gymShopPO, Root<GymShopPO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder
                return queryFilterExcute(encyclopedia,root,criteriaQuery,criteriaBuilder);
            }
        },pageable);
        return ResultUtil.success(gymShopPage);
    }

    @ApiOperation("根据主键查询地产百科信息")
    @GetMapping("/queryEncyclopediaByElid")
    @ApiImplicitParam(name = "elid", value = "百科主键")
    @RequestMapping(value = "/queryEncyclopediaByElid", method = RequestMethod.POST)
    public Result<EncyclopediaPO> queryEncyclopediaByElid(int elid) {
        EncyclopediaPO encyclopedia = encyclopediaDao.queryByElid(elid);
        return ResultUtil.success(encyclopedia);
    }

    @ApiOperation(value = "添加百科信息", notes = "参数参考EncyclopediaPO对象")
    @PostMapping(value = "/addEncyclopedia")
    @RequestMapping(value = "/addEncyclopedia", method = RequestMethod.POST)
    Result<EncyclopediaPO> addEncyclopedia(EncyclopediaPO encyclopediaPO, HttpServletRequest request) throws SCException {
        encyclopediaPO.setCreateTime(new Date());
        encyclopediaPO.setIsEnd(1);
        encyclopediaDao.save(encyclopediaPO);
        return ResultUtil.success(encyclopediaPO);
    }

    @ApiOperation(value = "删除百科", notes = "EncyclopediaPO主键")
    @ApiImplicitParam(name = "elid", value = "EncyclopediaPO主键", required = true)
    @DeleteMapping("/deleteEncyclopedia")
    @RequestMapping(value = "/deleteEncyclopedia", method = RequestMethod.POST)
    public Result<EncyclopediaPO> deleteEncyclopedia(@ApiIgnore("EncyclopediaPO")EncyclopediaPO encyclopediaPO) {
        List<EncyclopediaAnswerPO> enetiy = encyclopediaAnswerDao.queryByElid(encyclopediaPO.getElid());
        encyclopediaAnswerDao.deleteAll(enetiy);
        encyclopediaDao.delete(encyclopediaPO);
        return ResultUtil.success(true);
    }


    public Predicate queryFilterExcute(EncyclopediaPO encyclopedia,Root<EncyclopediaPO> root,CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder){
        List<Predicate> list = new ArrayList<Predicate>();

        if(GeneralUtils.notEmpty(encyclopedia.getTitle())){
            list.add(criteriaBuilder.like(root.get("title").as(String.class), "%"+encyclopedia.getTitle()+"%"));
        }
        if(GeneralUtils.notEmpty(encyclopedia.getType()) && encyclopedia.getType()!=0){
            list.add(criteriaBuilder.equal(root.get("type").as(int.class), encyclopedia.getType()));
        }

        Predicate[] p = new Predicate[list.size()];

        return criteriaBuilder.and(list.toArray(p));
    }
}
