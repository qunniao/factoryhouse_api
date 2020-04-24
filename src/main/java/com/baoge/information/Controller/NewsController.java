package com.baoge.information.Controller;

import com.baoge.Util.*;
import com.baoge.Util.Enums.ResultEnum;
import com.baoge.Util.PO.Result;
import com.baoge.information.Dao.NewsDao;
import com.baoge.information.PO.NewsPO;
import com.baoge.personnel.PO.UserPO;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(value = "/newsController", tags = "新闻api")
@RestController
@RequestMapping(value = "/newsController")
public class NewsController {

    @Autowired
    private NewsDao newsDao;

    @ApiOperation(value = "分页查询新闻信息", notes = "分页查询新闻信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "新闻类型 int类型 1:xx有话说 2:监测与分析 3:项目招商 4:园区动态 5:指数分析", required = true),
            @ApiImplicitParam(name = "title", value = "标题 (模糊查询)"),
            @ApiImplicitParam(name = "isEnd", value = "查询启用禁用(非管理页面不要使用)")
    })
    @GetMapping("/pageNews")
    @RequestMapping(value = "/pageNews", method = RequestMethod.POST)
    public Result<Page<NewsPO>> pageNews(PageUtil page,@ApiIgnore("NewsPO") NewsPO newsPO){
        Pageable pageable = PageRequest.of(page.getPageNum() - 1,page.getPageSize(),Sort.Direction.DESC,"nid");
        Page<NewsPO> gymShopPage = newsDao.findAll(new Specification<NewsPO>() {
            @Override
            public Predicate toPredicate(Root<NewsPO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                //GymShopPO gymShopPO, Root<GymShopPO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder
                return queryFilterExcute(newsPO,root,criteriaQuery,criteriaBuilder);
            }
        },pageable);
        return ResultUtil.success(gymShopPage);
    }

    @ApiOperation("根据主键查询新闻信息")
    @GetMapping("/queryNewsByNid")
    @ApiImplicitParam(name = "nid", value = "新闻主键")
    @RequestMapping(value = "/queryNewsByNid", method = RequestMethod.POST)
    public Result<NewsPO> queryNewsByNid(int nid) {
        NewsPO newsPO = newsDao.queryByNid(nid);
        return ResultUtil.success(newsPO);
    }

    @ApiOperation(value = "添加新闻信息", notes = "参数参考NewsPO对象")
    @PostMapping("/addNews")
    @RequestMapping(value = "/addNews", method = RequestMethod.POST)
    @Transactional
    Result<NewsPO> addNews(NewsPO newsPO, HttpServletRequest request,MultipartFile titlePictureFile) throws Exception {
        int aid = Integer.valueOf((String)request.getAttribute("userId"));
        String userName = (String)request.getAttribute("userName");
        FastDFSClient fastDFSClient = new FastDFSClient();
        String url = fastDFSClient.uploadUtil(titlePictureFile);
        newsPO.setCreateName(userName);
        newsPO.setCreateAid(aid);
        newsPO.setTitlePicture(url);
        newsPO.setCreateTime(new Date());
        newsPO.setIsEnd(1);
        newsDao.save(newsPO);
        return ResultUtil.success(newsPO);
    }


    @ApiOperation(value = "更新新闻的启用禁用", notes = "参数参考NewsPO对象")
    @PutMapping("/updateNewsByIsEnd")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nid", value = "NewsPO主键", required = true),
            @ApiImplicitParam(name = "isEnd", value = "是否启用/禁用", required = true)
    })
    @RequestMapping(value = "/updateNewsByIsEnd", method = RequestMethod.POST)
    @Transactional
    public Result<NewsPO> updateNewsByIsEnd(int nid,int isEnd) throws Exception {
        NewsPO news = newsDao.getOne(nid);
        if(StringUtils.isEmpty(news)||news.getNid() == 0){
            throw new SCException(ResultEnum.NULL_TABLE);
        }
        news.setIsEnd(isEnd);
        newsDao.save(news);

        return ResultUtil.success(true);
    }



    @ApiOperation(value = "删除新闻信息", notes = "NewsPO主键")
    @DeleteMapping("/deleteNews")
    @ApiImplicitParam(name = "nid", value = "NewsPO主键", required = true)
    @RequestMapping(value = "/deleteNews", method = RequestMethod.POST)
    public Result<NewsPO> deleteNews(@ApiIgnore("NewsPO")NewsPO newsPO) {
        newsDao.delete(newsPO);
        return ResultUtil.success(true);
    }

    public Predicate queryFilterExcute(NewsPO newsPO,Root<NewsPO> root,CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder){
        List<Predicate> list = new ArrayList<Predicate>();

        if(!GeneralUtils.notEmpty(newsPO.getIsEnd())){
            list.add(criteriaBuilder.equal(root.get("isEnd").as(int.class), 1));
        }else if (newsPO.getIsEnd() != 0){
            list.add(criteriaBuilder.equal(root.get("isEnd").as(int.class), newsPO.getIsEnd()));
        }

        if(GeneralUtils.notEmpty(newsPO.getType()) && newsPO.getType()!=0){
            list.add(criteriaBuilder.equal(root.get("type").as(int.class), newsPO.getType()));
        }

        if (GeneralUtils.notEmpty(newsPO.getTitle())) {
            list.add(criteriaBuilder.like(root.get("title").as(String.class), "%"+newsPO.getTitle()+"%"));
        }

        Predicate[] p = new Predicate[list.size()];

        return criteriaBuilder.and(list.toArray(p));
    }
}
