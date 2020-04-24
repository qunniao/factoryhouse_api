package com.baoge.data.Controller;

import com.baoge.Util.BeanUtil;
import com.baoge.Util.Enums.ResultEnum;
import com.baoge.Util.PO.Result;
import com.baoge.Util.PageUtil;
import com.baoge.Util.ResultUtil;
import com.baoge.Util.SCException;
import com.baoge.data.Dao.ProductListDao;
import com.baoge.data.PO.ProductList;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Api(value = "/productList", tags = "会员商品列表")
@RestController
@RequestMapping(value = "/productList")
public class ProductListController {

    @Autowired
    private ProductListDao productListDao;

    @ApiOperation(value = "分页查询会员商品列表", notes = "参考ProductList对象")
    @RequestMapping(value = "/pageProductList", method = RequestMethod.POST)
    @ApiImplicitParams({
    })
    @GetMapping("/pageProductList")
    public Result<Page<ProductList>> pageProductList(PageUtil page, @ApiIgnore("ProductList")ProductList productList){

        Pageable pageable = PageRequest.of(page.getPageNum() - 1,page.getPageSize(),Sort.Direction.ASC,"order");
        Page<ProductList> gymShopPage = productListDao.findAll(new Specification<ProductList>() {
            @Override
            public Predicate toPredicate(Root<ProductList> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                return queryFilterExcute(productList,root,criteriaQuery,criteriaBuilder);
            }
        },pageable);
        return ResultUtil.success(gymShopPage);
    }



    @ApiOperation(value = "更新会员商品列表信息", notes = "参数参考ProductList对象")
    @PutMapping("/updateProductList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "plid", value = "ProductList主键", required = true)
    })
    @RequestMapping(value = "/updateProductList", method = RequestMethod.POST)
    @Transactional
    public Result<ProductList> updateProductList(ProductList productList)throws Exception {
        ProductList entity = productListDao.queryByPlid(productList.getPlid());
        if(StringUtils.isEmpty(entity)||entity.getPlid() == 0){
            throw new SCException(ResultEnum.NULL_TABLE);
        }
        BeanUtil.copyProperties(productList, entity, Arrays.asList("plid"));
        productListDao.save(entity);

        return ResultUtil.success(true);
    }


    @ApiOperation(value = "添加会员商品列表", notes = "参数参考ProductList对象")
    @PostMapping(value = "/addProductList")
    @RequestMapping(value = "/addProductList", method = RequestMethod.POST)
    Result<ProductList> addProductList(ProductList productList) throws SCException {
        productListDao.save(productList);
        return ResultUtil.success(productList);
    }


    @ApiOperation(value = "删除会员商品")
    @ApiImplicitParam(name = "plid", value = "ProductList主键", required = true)
    @DeleteMapping("/deleteProductList")
    @RequestMapping(value = "/deleteProductList", method = RequestMethod.POST)
    public Result<ProductList> deleteProductList(@ApiIgnore("ProductList")ProductList productList)throws SCException {
        productListDao.delete(productList);
        return ResultUtil.success(true);
    }



    public Predicate queryFilterExcute(ProductList productList,Root<ProductList> root,CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder){
        List<Predicate> list = new ArrayList<Predicate>();

        Predicate[] p = new Predicate[list.size()];

        return criteriaBuilder.and(list.toArray(p));
    }

}
