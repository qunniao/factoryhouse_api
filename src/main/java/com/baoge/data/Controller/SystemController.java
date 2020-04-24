package com.baoge.data.Controller;

import com.baoge.Util.FileUtils;
import com.baoge.Util.R;
import com.baoge.Util.ResultStant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 0 on 2019/6/15.
 */
@Controller
@RequestMapping(value = "/system")
public class SystemController {


    /**
     * 上传图片
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/oss/fileUpload", method = RequestMethod.POST)
    @ResponseBody
    public R fileUpload(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> result = new HashMap<>();
        result.put("code", ResultStant.RESULT_CODE_SUCCESS);
        result.put("msg", "上传成功！");
        result.put("data", FileUtils.upload(request));
        return R.ok(FileUtils.upload(request));
    }

    /**
     * 删除图片
     * @return
     */
    @RequestMapping(value = "/oss/deleteFileUpload", method = RequestMethod.POST)
    @ResponseBody
    public Map deleteFileUpload(@RequestParam(value = "image", required = false)String image){
        Map<String,Object> result = new HashMap<>();
        result.put("code", ResultStant.RESULT_CODE_SUCCESS);

        if ( FileUtils.deleteFile(image)){
            result.put("msg", "删除成功！");
            result.put("code", ResultStant.RESULT_CODE_SUCCESS);
        }else {
            result.put("msg", "删除失败！");
            result.put("code", ResultStant.RESULT_CODE_ERROR);
        }

        result.put("data", "");
        return result;
    }

}
