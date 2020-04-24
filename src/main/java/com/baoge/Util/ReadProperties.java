package com.baoge.Util;


import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.*;
import java.util.Properties;


public class ReadProperties {
    public static void main(String[] args) throws Exception {
        String aa = ReadProperties.getProperties_1("config/config.properties", "serverId");
        System.out.println(aa);
    }

    public static String getProperties_1(String filePath, String keyWord) {
        if (filePath.equals("")) {
            filePath = "config/config.properties";
        }
        Properties prop = null;
        String value = null;
        try {
            // 通过Spring中的PropertiesLoaderUtils工具类进行获取

            prop = PropertiesLoaderUtils.loadAllProperties(filePath);
            // 根据关键字查询相应的值
            value = prop.getProperty(keyWord);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 修改或添加键值对 如果key存在，修改, 反之，添加。
     * @param filePath 文件路径，即文件所在包的路径，例如：java/util/config.properties
     * @param key 键
     * @param value 键对应的值
     */

    public static void writeData(String filePath, String key, String value) {
        //获取绝对路径
        filePath = ReadProperties.class.getResource("/" + filePath).toString();
        //截掉路径的”file:/“前缀
        filePath = filePath.substring(6);
        Properties prop = new Properties();
        try {
            File file = new File(filePath);
            if (!file.exists())
                file.createNewFile();
            InputStream fis = new FileInputStream(file);
            prop.load(fis);
            //一定要在修改值之前关闭fis
            fis.close();
            OutputStream fos = new FileOutputStream(filePath);
            prop.setProperty(key, value);
            //保存，并加入注释
            prop.store(fos, "Update '" + key + "' value");
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据KEY，读取文件对应的值
     * @param filePath 文件路径，即文件所在包的路径，例如：java/util/config.properties
     * @param key 键
     * @return key对应的值
     */
    public static String readData(String filePath, String key) {
        //获取绝对路径
        filePath = ReadProperties.class.getResource("/" + filePath).toString();
        //截掉路径的”file:“前缀
        filePath = filePath.substring(6);
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            props.load(in);
            in.close();
            String value = props.getProperty(key);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
