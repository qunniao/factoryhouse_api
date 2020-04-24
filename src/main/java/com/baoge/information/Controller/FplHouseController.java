package com.baoge.information.Controller;

import com.baoge.Util.*;
import com.baoge.Util.Enums.ResultEnum;
import com.baoge.Util.PO.Result;
import com.baoge.data.Dao.CityDao;
import com.baoge.data.Dao.PictureStorageDao;
import com.baoge.data.PO.CityPO;
import com.baoge.data.PO.PictureStoragePO;
import com.baoge.information.Dao.FplHouseDao;
import com.baoge.information.PO.FplHousePO;
import com.baoge.personnel.Component.MenberComponent;
import com.baoge.personnel.PO.UserPO;
import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.distance.DistanceUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javafx.concurrent.Task;
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

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Api(value = "/fplHouse", tags = "出租出售厂房,厂库,土地api")
@RestController
@RequestMapping(value = "/fplHouse")
public class FplHouseController {

    @Autowired
    private FplHouseDao fplHouseDao;

    @Autowired
    private PictureStorageDao pictureStorageDao;

    @Autowired
    private MenberComponent menberComponent;

    @Autowired
    private CityDao cityDao;


    @ApiOperation(value = "分页查询出租出售的 厂房/仓库/土地的数据", notes = "参数参考FplHouse对象,支持productId,oneType,towType,provinceName,cityName,regionName条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "房源编号"),
            @ApiImplicitParam(name = "uid", value = "用户ID"),
            @ApiImplicitParam(name = "types", value = "类别 (0初始,1厂房 2 仓库 3土地)"),
            @ApiImplicitParam(name = "status", value = "二类(0:初始,1:出租,2:出售)"),
            @ApiImplicitParam(name = "upArea", value = "查询面积上限"),
            @ApiImplicitParam(name = "downArea", value = "查询面积下限"),
            @ApiImplicitParam(name = "upRent", value = "查询价格上限"),
            @ApiImplicitParam(name = "downRent", value = "查询价格下限"),
            @ApiImplicitParam(name = "rentUnit", value = "查询价格单位"),
            @ApiImplicitParam(name = "title", value = "标题 (模糊查询)"),
            @ApiImplicitParam(name = "region", value = "查询地区"),
            @ApiImplicitParam(name = "buildingStructure", value = "建筑结构"),
            @ApiImplicitParam(name = "isEnd", value = "启用/禁用 (此条件仅供管理页面使用 默认为查询启用数据)"),
            @ApiImplicitParam(name = "userPhone", value = "查询创建用户手机")
    })
    @GetMapping("/pageFplHouse")
    @RequestMapping(value = "/pageFplHouse", method = RequestMethod.POST)
    public Result<Page<FplHousePO>> pageFplHouse(PageUtil page, @ApiIgnore("FplHousePO")FplHousePO fplHousePO,
                                                 @RequestParam(required = false)Integer upArea, @RequestParam(required = false)Integer downArea,
                                                 @RequestParam(required = false)Integer upRent,@RequestParam(required = false)Integer downRent,
                                                 @RequestParam(required = false)String userPhone) {

        Pageable pageable = new PageRequest(page.getPageNum() - 1, page.getPageSize(), Sort.Direction.ASC, "fid");

        Page<FplHousePO> gymShopPage = fplHouseDao.findAll(new Specification<FplHousePO>() {
            @Override
            public Predicate toPredicate(Root<FplHousePO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                //GymShopPO gymShopPO, Root<GymShopPO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder
                return queryFilterExcute(fplHousePO,root,criteriaQuery,criteriaBuilder,upArea,downArea,upRent,downRent,userPhone);
            }
        },pageable);

        return ResultUtil.success(gymShopPage);
    }

    @ApiOperation("根据编号查询出租出售的厂房/仓库/土地的数据")
    @GetMapping("/queryFplHousePOByProductId")
    @ApiImplicitParam(name = "productId", value = "编号id", dataType = "String")
    @RequestMapping(value = "/queryFplHousePOByProductId", method = RequestMethod.POST)
    public Result<FplHousePO> queryFplHousePOByProductId(String productId) {
        return ResultUtil.success(fplHouseDao.queryByProductId(productId));
    }


    @ApiOperation("查询区域内相关信息")
    @GetMapping("/queryFplHouseRegionalInformation")
    @ApiImplicitParam(name = "city", value = "区域 (传入2级或3级区域)", dataType = "String")
    @RequestMapping(value = "/queryFplHouseRegionalInformation", method = RequestMethod.POST)
    public Result<Object> queryFplHouseRegionalInformation(String city) throws Exception {
        CityPO cityEntity = cityDao.queryTop1ByName(city);
        if(cityEntity == null){
            throw new SCException(ResultEnum.NULL_REGION);
        }

        if(cityEntity.getLevel() == 2 ){
            List entity = new ArrayList();
            List<CityPO> bean = cityDao.queryByPid(cityEntity.getId());
            for(CityPO cityPO : bean){
                int count = fplHouseDao.countByRegionLike("%" + cityPO.getName() + "%");
                if(count>0){
                    Map map2 = new HashMap<String,Object>();
                    map2.put("longitude",cityPO.getLng());
                    map2.put("latitude",cityPO.getLat());
                    map2.put("count",count);
                    map2.put("title",cityPO.getName());
                    entity.add(map2);
                }
            }
            return ResultUtil.success(entity);
        }else if (cityEntity.getLevel() == 3){
            List<FplHousePO> entity = fplHouseDao.findAllByRegionLike("%" + cityEntity.getName() + "%");
            return ResultUtil.success(entity);
        }else {
            return ResultUtil.success(false);
        }
    }

    @ApiOperation("根据用户查询出租出售的信息")
    @GetMapping("/queryFplHouseByUid")
    @ApiImplicitParam(name = "uid", value = "User(用户表)主键", dataType = "int", required = true)
    @RequestMapping(value = "/queryFplHouseByUid", method = RequestMethod.POST)
    public Result<List<FplHousePO>> queryFplHouseByUid(int uid) {
        return ResultUtil.success(fplHouseDao.findAllByUid(uid));
    }

    @ApiOperation("根据用户与类型查询出租出售的信息")
    @GetMapping("/queryFplHouseByUidAndTypes")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "User(用户表)主键", required = true),
            @ApiImplicitParam(name = "types", value = "类别 (0初始,1厂房 2 仓库 3土地)", required = true)
    })
    @RequestMapping(value = "/queryFplHouseByUidAndTypes", method = RequestMethod.POST)
    public Result<List<FplHousePO>> queryFplHouseByUidAndTypes(int uid,int types) {
        return ResultUtil.success(fplHouseDao.findAllByUidAndTypes(uid,types));
    }

    @ApiOperation("根据当前坐标查询出租出售的厂房/仓库/土地的数据")
    @GetMapping("/queryFplHousePOByLatitudeAndLongitude")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "latitude", value = "当前经度", required = true),
            @ApiImplicitParam(name = "longitude", value = "当前纬度", required = true)
    })
    @RequestMapping(value = "/queryFplHousePOByLatitudeAndLongitude", method = RequestMethod.POST)
    public Result<List<FplHousePO>> queryFplHousePOByLatitudeAndLongitude(Double latitude,Double longitude)throws Exception {
        if(latitude == null || longitude==null){
            throw new SCException(ResultEnum.NULL_POSITIONING);
        }
        List<FplHousePO> entity = fplHouseDao.findAllByIsEnd(1);
        calculatedDistance(latitude,longitude,entity);

        return ResultUtil.success(entity);
    }

    @ApiOperation(value = "添加出租出售的:厂房/仓库/土地", notes = "添加时可为空参数其他参数可查看FplHousePO对象(除主键和编号外)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "region", value = "区域信息", required = true),
            @ApiImplicitParam(name = "types", value = "类别 (0初始,1厂房 2 仓库 3土地)", required = true),
            @ApiImplicitParam(name = "status", value = "二类(0:初始,1:出租,2:出售)", required = true),
            @ApiImplicitParam(name = "subleaseType", value = "是否分租 0:分租 1不分租", required = true),
            @ApiImplicitParam(name = "rent", value = "租金/售价(根据类型不同显示不同)", required = true),
            @ApiImplicitParam(name = "area", value = "面积", required = true),
            @ApiImplicitParam(name = "leasePeriod", value = "租期", required = true),
            @ApiImplicitParam(name = "title", value = "标题", required = true),
            @ApiImplicitParam(name = "address", value = "地址", required = true),
            @ApiImplicitParam(name = "latitude", value = "经度", required = true),
            @ApiImplicitParam(name = "longitude", value = "纬度", required = true),
            @ApiImplicitParam(name = "infrastructure", value = "基础设施", required = true),
            @ApiImplicitParam(name = "buildingStructure", value = "建筑结构", required = true),
            @ApiImplicitParam(name = "peripheralPackage", value = "周边配套", required = true),
            @ApiImplicitParam(name = "suitableBusiness", value = "适合企业", required = true),
            @ApiImplicitParam(name = "detailedDescription", value = "详情描述", required = true),
            @ApiImplicitParam(name = "contact", value = "联系人", required = true),
            @ApiImplicitParam(name = "contactPhone", value = "联系人电话", required = true),
            @ApiImplicitParam(name = "files", value = "图片集合 (List)", required = true)
    })
    @PostMapping("/addFplHouse")
    @RequestMapping(value = "/addFplHouse", method = RequestMethod.POST)
    @Transactional
    public Result<Boolean> addFplHouse(HttpServletRequest request, FplHousePO fplHouse,@RequestParam(value = "files")List<MultipartFile> files) throws Exception {
        Boolean isEnd = menberComponent.decideMenber(fplHouse.getUid());

        int count = fplHouseDao.countByUid(fplHouse.getUid());
        int releasesNumber =isEnd?Integer.parseInt(ReadProperties.getProperties_1("config/config.properties", "memberReleasesNumber"))
                : Integer.parseInt(ReadProperties.getProperties_1("config/config.properties", "releasesNumber"));
        if(count>=releasesNumber&&isEnd == false){
            throw new SCException(ResultEnum.EXCEED_UPPERLIMIT);
        }
        FastDFSClient fastDFSClient = new FastDFSClient();
        List<String> urls = fastDFSClient.uploadUtil(files);
        fplHouse.setTitlePicture(urls.get(0));
        fplHouse.setProductId(RandomUtil.getShortUuid() + DateUtil.current("yyMMdd"));
        fplHouse.setCreateTime(new Date());
        fplHouse.setIsEnd(1);
        fplHouseDao.save(fplHouse);
        urls.remove(0);
        if(urls!=null&&urls.size()>0){
            List<PictureStoragePO> PictureStoragePOs = new ArrayList<PictureStoragePO>() ;
            for(String url:urls){
                PictureStoragePO pictureStoragePO = new PictureStoragePO();
                pictureStoragePO.setCreateTime(new Date());
                pictureStoragePO.setPrimaryid(fplHouse.getFid());
                pictureStoragePO.setType(1);
                pictureStoragePO.setUrl(url);
                pictureStoragePO.setCreateUserId(fplHouse.getUid());
                PictureStoragePOs.add(pictureStoragePO);
            };
            pictureStorageDao.saveAll(PictureStoragePOs);
        }
        return ResultUtil.success(true);
    }

    @ApiOperation(value = "更新出租出售的,厂房/仓库/土地的数据", notes = "参数参考FplHouse对象")
    @ApiImplicitParam(name = "fid", value = "FplHouse主键", required = true)
    @RequestMapping(value = "/updateFplHouse", method = RequestMethod.POST)
    @PutMapping("/updateFplHouse")
    @Transactional
    public Result<FplHousePO> updateFplHouse(FplHousePO fplHouse)throws Exception {
        FplHousePO fplHousePO = fplHouseDao.getOne(fplHouse.getFid());
        if(StringUtils.isEmpty(fplHousePO)||fplHousePO.getFid() == 0){
            throw new SCException(ResultEnum.NULL_TABLE);
        }
        BeanUtil.copyProperties(fplHouse, fplHousePO, Arrays.asList("pictureStoragePOS","createTime","uid","productId","isEnd"));
        fplHouseDao.save(fplHousePO);

        return ResultUtil.success(true);
    }

    @ApiOperation(value = "更新出租出售的,厂房/仓库/土地的启用禁用(上架下架功能)", notes = "参数参考FplHouse对象")
    @PutMapping("/updateFplHouseByIsEnd")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fid", value = "FplHouse主键", required = true),
            @ApiImplicitParam(name = "isEnd", value = "是否启用/禁用", required = true)
    })
    @RequestMapping(value = "/updateFplHouseByIsEnd", method = RequestMethod.POST)
    @Transactional
    public Result<FplHousePO> updateFplHouseByIsEnd(int fid,int isEnd)throws Exception {
        FplHousePO fplHousePO = fplHouseDao.getOne(fid);
        if(StringUtils.isEmpty(fplHousePO)||fplHousePO.getFid() == 0){
            throw new SCException(ResultEnum.NULL_TABLE);
        }
        fplHousePO.setIsEnd(isEnd);
        fplHouseDao.save(fplHousePO);

        return ResultUtil.success(true);
    }

    @ApiOperation(value = "删除出租出售的厂房/仓库/土地的数据", notes = "FplHouse主键")
    @ApiImplicitParam(name = "fid", value = "FplHouse主键", required = true)
    @RequestMapping(value = "/deleteFplHouse", method = RequestMethod.POST)
    @DeleteMapping("/deleteFplHouse")
    @Transactional
    public Result<FplHousePO> deleteFplHouse(FplHousePO fplHouse) {
        List<PictureStoragePO> pictureStoragePO = pictureStorageDao.findAllByPrimaryid(fplHouse.getFid());
        pictureStorageDao.deleteAll(pictureStoragePO);
        fplHouseDao.delete(fplHouse);

        return ResultUtil.success(true);
    }


    private Predicate queryFilterExcute(FplHousePO fplHousePO, Root<FplHousePO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder,Integer upArea,Integer downArea,Integer upRent,Integer downRent,String userPhone) {

        List<Predicate> list = new ArrayList<Predicate>();

        if(!GeneralUtils.notEmpty(fplHousePO.getIsEnd())){
            list.add(criteriaBuilder.equal(root.get("isEnd").as(int.class), 1));
        }else if (fplHousePO.getIsEnd() != 0){
            list.add(criteriaBuilder.equal(root.get("isEnd").as(int.class), fplHousePO.getIsEnd()));
        }

        if (GeneralUtils.notEmpty(fplHousePO.getUid()) && fplHousePO.getUid() != 0) {
            list.add(criteriaBuilder.equal(root.get("uid").as(int.class), fplHousePO.getUid()));
        }

        if (GeneralUtils.notEmpty(fplHousePO.getProductId())) {
            list.add(criteriaBuilder.like(root.get("productId").as(String.class), "%"+fplHousePO.getProductId()+"%"));
        }

        if (GeneralUtils.notEmpty(fplHousePO.getTypes()) && fplHousePO.getTypes() != 0){
            list.add(criteriaBuilder.equal(root.get("types").as(int.class), fplHousePO.getTypes()));
        }

        if (GeneralUtils.notEmpty(fplHousePO.getStatus()) && fplHousePO.getStatus() != 0){
            list.add(criteriaBuilder.equal(root.get("status").as(int.class), fplHousePO.getStatus()));
        }

        if(GeneralUtils.notEmpty(upArea)&& GeneralUtils.notEmpty(downArea)){
            list.add(criteriaBuilder.between(root.get("area").as(int.class), upArea, downArea));
        }

        if(GeneralUtils.notEmpty(upRent)&& GeneralUtils.notEmpty(downRent)){
            list.add(criteriaBuilder.between(root.get("rent").as(int.class), upRent, downRent));
        }

        if (GeneralUtils.notEmpty(fplHousePO.getRentUnit())){
            list.add(criteriaBuilder.equal(root.get("rentUnit").as(String.class), fplHousePO.getRentUnit()));
        }

        if(GeneralUtils.notEmpty(fplHousePO.getRegion())){
            list.add(criteriaBuilder.like(root.get("region").as(String.class), "%"+fplHousePO.getRegion()+"%"));
        }
        if(GeneralUtils.notEmpty(fplHousePO.getBuildingStructure())&&fplHousePO.getBuildingStructure()!=0){
            list.add(criteriaBuilder.equal(root.get("buildingStructure").as(int.class), fplHousePO.getBuildingStructure()));
        }

        //测试标题模糊查询
        if (GeneralUtils.notEmpty(fplHousePO.getTitle())) {
            list.add(criteriaBuilder.like(root.get("title").as(String.class), "%"+fplHousePO.getTitle()+"%"));
        }

        if (GeneralUtils.notEmpty(userPhone)) {
            Join<Task,UserPO> join = root.join("user", JoinType.LEFT);
            list.add(criteriaBuilder.like(join.get("userPhone").as(String.class), "%"+userPhone+"%"));
        }

        //测试标题模糊查询
       /* if (GeneralUtils.notEmpty(fplHousePO.getTitle())) {
            list.add(criteriaBuilder.like(root.get("productId").as(String.class), "%"+fplHousePO.getTitle()+"%"));
        }*/


//        if (GeneralUtils.notEmpty(gymShopPO.getGymSubjectPO())) {
//            Join<GymShopPO, GymSubjectPO> join = root.join("gymSubjectPOList", JoinType.INNER);
//            list.add(criteriaBuilder.equal(join.get("sid"), gymShopPO.getGymSubjectPO().getSid()));
//            //--------------------华丽分割线--------------------------
////        list.add(criteriaBuilder.equal(root.<GymSubjectPO>get("gymSubjectPO").get("sid"),1));
////        criteriaQuery.groupBy(root.get("gid"));
//            //List<GymSubjectPO>
//
////            List<GymSubjectPO> subjectPOS = gymShopPO.getGymSubjectPOList();
////            Path<Object> path = join.get("bid");
////
////            CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
////            for (GymSubjectPO subjectPO : subjectPOS) {
////                in.value(subjectPO.getSid());
////            }
////
////            list.add(criteriaBuilder.and(in));
//        }

//        if (GeneralUtils.notEmpty(gymShopPO.getBuildingPO())) {
//            Join<GymShopPO, GymBuildingPO> join = root.join("gymBuildingPOList", JoinType.INNER);
//            list.add(criteriaBuilder.equal(join.get("bid"), gymShopPO.getBuildingPO().getBid()));
//
////            List<GymBuildingPO> gymBuildingPOS = gymShopPO.getGymBuildingPOList();
////            Path<Object> path = join.get("bid");
////
////            CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
////            for (GymBuildingPO gymBuilding:gymBuildingPOS){
////                in.value(gymBuilding.getBid());
////            }
////
////            list.add(criteriaBuilder.and(in));
//
//        }

        /*if (gymShopPO.getLongitude() != 0 && gymShopPO.getLatitude() != 0) {
            double lon = gymShopPO.getLongitude(), lat = gymShopPO.getLatitude(); // 千米
            int radius = 5;
            SpatialContext geo = SpatialContext.GEO;
            Rectangle rectangle = geo.getDistCalc().calcBoxByDistFromPt(geo.makePoint(lon, lat), radius * DistanceUtils.KM_TO_DEG, geo, null);
            System.out.println(rectangle.getMinX() + "-" + rectangle.getMaxX());// 经度范围
            System.out.println(rectangle.getMinY() + "-" + rectangle.getMaxY());// 纬度范围
            list.add(criteriaBuilder.between(root.get("longitude"), lon, lat));
            list.add(criteriaBuilder.between(root.get("latitude"), lon, lat));
        }*/

        Predicate[] p = new Predicate[list.size()];

        return criteriaBuilder.and(list.toArray(p));
    }

    private List<FplHousePO> calculatedDistance(Double lat,Double lon,List<FplHousePO> bean){


        for (FplHousePO entity:bean){
            double lat2 = entity.getLatitude(), lon2 = entity.getLongitude();
            SpatialContext geo = SpatialContext.GEO;
            double distance = geo.calcDistance(geo.makePoint(lon, lat), geo.makePoint(lon2, lat2))
                    * DistanceUtils.DEG_TO_KM;
            entity.setDistance(distance);
        }
        Collections.sort(bean);
        return bean;
    }
}
