package com.github.codegenerator.common.util;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataUtil {


    public static boolean updateData(String key, String data, boolean insertIfAbsent) {
        try {
            FileUtils.update(FileUtils.concatPath(ContextContainer.CONFIG_PATH,key),data,insertIfAbsent);
            return true;
        } catch (Exception e) {
            ContextContainer.getContext().error("保存config数据异常:"+e.getMessage());
            return false;
        }
    }

    public static boolean saveData(String key, String data) {
        try {
            FileUtils.write(FileUtils.concatPath(ContextContainer.CONFIG_PATH,key),data);
            return true;
        } catch (Exception e) {
            ContextContainer.getContext().error("保存config数据异常："+e.getMessage());
            return false;
        }
    }

    public static boolean deleteData(String key) {
        try {
            FileUtils.delete(FileUtils.concatPath(ContextContainer.CONFIG_PATH,key));
            return true;
        } catch (Exception e) {
            ContextContainer.getContext().error("删除config数据异常："+e.getMessage());
            return false;
        }
    }


    public static <T> T getData(String key, Class<T> clazz) {
        try {
            String info = FileUtils.read(FileUtils.concatPath(ContextContainer.CONFIG_PATH,key));
            if(null == info){
                return null;
            }
            return JSONObject.parseObject(info,clazz);
        } catch (Exception e) {
            ContextContainer.getContext().error("获取config数据异常:"+e.getMessage());
            return null;
        }
    }

    public static List<String> getDataNameList() {
        try {
            File configDir = new File(FileUtils.concatPath(ContextContainer.CONFIG_PATH));
            if(!configDir.exists()){
                return new ArrayList<>();
            }
            return Arrays.asList(configDir.listFiles()).stream().filter(f -> !f.isHidden() && f.isFile()).map(f ->f.getName()).collect(Collectors.toList());
        } catch (Exception e) {
            ContextContainer.getContext().error("获取config数据列表异常:"+e.getMessage());
            return null;
        }
    }


}
