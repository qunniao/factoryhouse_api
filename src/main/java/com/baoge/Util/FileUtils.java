package com.baoge.Util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by King on 2019/6/15.
 */
public class FileUtils {
    // endpoint是访问OSS的域名。如果您已经在OSS的控制台上 创建了Bucket，请在控制台上查看域名。


    private static String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
    private static String endpoint1 = "http://jikang.oss-cn-hangzhou.aliyuncs.com";
    private static String accessKeyId = "LTAI8VoT591CDwLd";
    private static String accessKeySecret = "fK8hu4R3k1y24EFsrlLDpuRsIM8O6U";
    private static String bucketName = "jikang";

    /**
     * 上传文件
     *
     * @param request
     * @return map
     */
    public static Map<String, Object> upload(HttpServletRequest request) {
        Map<String, Object> mapReturn = new HashMap<String, Object>();
        // 生成OSSClient，您可以指定一些参数，详见“SDK手册 > Java-SDK > 初始化”，
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        String extendFileName = ".jpg";
        if (multipartResolver.isMultipart(request)) {
            try {
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                List<MultipartFile> images = multiRequest.getFiles("image");// 接收的图片
                System.out.println("images1"+images.size() );
                if (null != images && images.size()>0) {
                    ArrayList<String> strings = addFile(images, extendFileName);
                    System.out.println("strings2"+strings.size() );
                    if (strings.size() > 0) {
                        System.out.println("images3"+strings);
                        mapReturn.put("images", strings);
                    }
                }
            } catch (OSSException oe) {
                oe.printStackTrace();
            } catch (ClientException ce) {
                ce.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                ossClient.shutdown();
            }
        }
        return mapReturn;
    }


    /**
     * 富文本上传文件方法
     *
     * @param request
     * @return map
     */
    public static Map<String, Object> uploadfile(MultipartFile request) {
        Map<String, Object> mapReturn = new HashMap<String, Object>();
        // 生成OSSClient，您可以指定一些参数，详见“SDK手册 > Java-SDK > 初始化”，
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        String extendFileName = ".jpg";
        if (true) {
            try {
                List<MultipartFile> images = new ArrayList<>();// 接收的图片
                images.add(request);
                if (null != images && images.size()>0) {
                    ArrayList<String> strings = addFile(images, extendFileName);
                    if (strings.size() > 0) {
                        mapReturn.put("images", strings);
                    }
                }
            } catch (OSSException oe) {
                oe.printStackTrace();
            } catch (ClientException ce) {
                ce.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                ossClient.shutdown();
            }
        }
        return mapReturn;
    }





    public static ArrayList<String> addFile(List<MultipartFile> parameter, String extendFileName) {
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ArrayList<String> imagesinfos = new ArrayList<>();
        try {
            if (parameter != null) {
                ArrayList<String> imagesinfo = new ArrayList<>();
                for (MultipartFile multipartFile : parameter) {
                    String originalFilename = multipartFile.getOriginalFilename();
                    if (IsObjectNullUtils.is(originalFilename)) {
                        continue;
                    }
                    String[] split = originalFilename.split("\\.");
                    // 文件存储入OSS，Object的名称为uuid
                    String uuid = FileUtils.uuid();
                    System.out.println("uuid"+uuid);
                    File f = null;
                    f = File.createTempFile("tmp", null);
                    multipartFile.transferTo(f);
                    imagesinfo.add(endpoint1 + "/" + uuid + "." + split[1]);
                    ossClient.putObject(bucketName, uuid + "." + split[1], f);
                }
                return imagesinfo;
            }
        } catch (Exception e) {

        }
        return imagesinfos;
    }


    private static File multipartToFile(MultipartFile multfile) {
        CommonsMultipartFile cf = (CommonsMultipartFile) multfile;
        // 这个myfile是MultipartFile的
        DiskFileItem fi = (DiskFileItem) cf.getFileItem();
        File file = fi.getStoreLocation();
        // 手动创建临时文件
        if (file.length() < 20971520) {
            File tmpFile = new File(
                    System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + file.getName());
            try {
                multfile.transferTo(tmpFile);
            } catch (IllegalStateException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return new File(tmpFile.getAbsolutePath());
        }
        return file;
    }

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static boolean deleteFile(String filePath) {
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        System.out.println("删除文件,filePath="+filePath);
        ossClient.deleteObject(bucketName, filePath);
        ossClient.shutdown();
        return true;
    }
}
