package com.baoge.data.Controller;

import com.baoge.Util.Enums.ResultEnum;
import com.baoge.Util.FastDFSClient;
import com.baoge.Util.PO.Result;
import com.baoge.Util.ResultUtil;
import com.baoge.Util.SCException;
import com.baoge.data.Dao.PictureStorageDao;
import com.baoge.data.PO.PictureStoragePO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Api(value = "/picture", tags = "图片API")

@RestController
@RequestMapping(value = "/picture")
public class PictureController {

    @Autowired
    private PictureStorageDao pictureStorageDao;


    @ApiIgnore
    @ApiOperation(value = "图片上传功能 (富文本编写使用)")
    @PostMapping("/uploadPicture")
    @ApiImplicitParam(name = "files", value = "上传文件集合", required = true)
    @RequestMapping(value = "/uploadPicture", method = RequestMethod.POST)
   public Result<List<String>> uploadPicture(@RequestParam(value = "files")List<MultipartFile> files) throws Exception {

       if(files.size()<1){
           throw new SCException(ResultEnum.NULL_UPLOAD);
       }
       FastDFSClient fastDFSClient = new FastDFSClient();
       List<String> urls = fastDFSClient.uploadUtil(files);
       return  ResultUtil.success(urls);
   }


    @ApiOperation(value = "图片上传返回URL")
    @PostMapping("/uploadPic")
    @ApiImplicitParam(name = "files", value = "上传文件集合", required = true)
    @RequestMapping(value = "/uploadPic", method = RequestMethod.POST)
    public Result<List<String>> uploadPic(@RequestParam(value = "files")MultipartFile files) throws Exception {
        if(files==null){
            throw new SCException(ResultEnum.NULL_UPLOAD);
        }
        FastDFSClient fastDFSClient = new FastDFSClient();
        String url = fastDFSClient.uploadUtil(files);
        return  ResultUtil.success(url);
    }


    @ApiIgnore
    @ApiOperation(value = "图片上传功能 (上传系统文件)")
    @PostMapping("/uploadSystemPicture")
    @ApiImplicitParam(name = "files", value = "上传文件集合", required = true)
    @RequestMapping(value = "/uploadSystemPicture", method = RequestMethod.POST)
    public Result<List<PictureStoragePO>> uploadSystemPicture(@RequestParam(value = "files")List<MultipartFile> files) throws Exception {

        if(files.size()<1){
            throw new SCException(ResultEnum.NULL_UPLOAD);
        }
        FastDFSClient fastDFSClient = new FastDFSClient();
        List<String> urls = fastDFSClient.uploadUtil(files);
        List<PictureStoragePO> systemList = new ArrayList<>();
        for(String url: urls){
            PictureStoragePO pictureStorage = new PictureStoragePO();
            pictureStorage.setType(4);
            pictureStorage.setCreateTime(new Date());
            pictureStorage.setCreateUserId(0);
            pictureStorage.setUrl(url);
            pictureStorage.setPrimaryid(0);
            systemList.add(pictureStorage);
        }
        pictureStorageDao.saveAll(systemList);
        return  ResultUtil.success(systemList);
    }




    @ApiOperation(value = "查看系统轮播图片")
    @PostMapping("/selectSystemPicture")
    @RequestMapping(value = "/selectSystemPicture", method = RequestMethod.POST)
    public Result<List<PictureStoragePO>> selectSystemPicture() throws Exception {

        List<PictureStoragePO> entity = pictureStorageDao.findAllByType(4);

        return  ResultUtil.success(entity);
    }

    @ApiIgnore
    @ApiOperation(value = "删除系统轮播图片")
    @PostMapping("/delSystemPicture")
    @RequestMapping(value = "/delSystemPicture", method = RequestMethod.POST)
    public Result<Boolean> delSystemPicture(PictureStoragePO entity) throws Exception {

       pictureStorageDao.delete(entity);

        return  ResultUtil.success(true);
    }

}
