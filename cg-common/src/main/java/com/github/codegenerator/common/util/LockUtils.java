package com.github.codegenerator.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class LockUtils {

    private static Logger logger = LoggerFactory.getLogger(LockUtils.class);

    private final static Map<String,ReentrantLock> dbLockMap = new ConcurrentHashMap<>();

    private final static int TIMEOUT = 3*60*1000;


    /**
     * 尝试锁定，根据key找到锁，尝试加锁，能加锁成功就可以继续执行
     * @param key
     * @return
     */
    public static boolean tryLock(String key){
        try {
            if(!dbLockMap.containsKey(key)){
                dbLockMap.putIfAbsent(key, new ReentrantLock());
            }
            return dbLockMap.get(key).tryLock(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.error("锁定失败",e);
        }
        return false;
    }


    public static void unLock(String key){
        try {
            if(dbLockMap.containsKey(key)){
                dbLockMap.remove(key);
            }
        } catch (Exception e) {
            logger.error("解锁失败",e);
        }
    }
}
