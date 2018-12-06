package com.github.codegenerator.common.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class LogUtils {


    private static Map<Class, Logger> loggerMap = new ConcurrentHashMap<>();

    public static void error(String message,Throwable e,Class clz){
        if(null == loggerMap.get(clz)){
            loggerMap.put(clz,LoggerFactory.getLogger(clz));
        }
        if(null != e){
            loggerMap.get(clz).error(message,e);
        }else{
            loggerMap.get(clz).error(message);
        }
    }

    public static void warn(String message,Class clz){
        if(null == loggerMap.get(clz)){
            loggerMap.put(clz,LoggerFactory.getLogger(clz));
        }
        loggerMap.get(clz).warn(message);
    }


}
