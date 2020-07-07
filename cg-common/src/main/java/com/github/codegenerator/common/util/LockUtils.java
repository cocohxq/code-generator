package com.github.codegenerator.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class LockUtils {

    public final static int EXPIRE_TIME = 3 * 60 * 1000;
    //没有使用ReentrantReadWriteLock是因为这个不是基于线程的，一个操作是很多次请求进来，是一个阶段性的操作
    private final static Map<String, LockInfo> dbLockMap = new ConcurrentHashMap<>();
    private static Logger logger = LoggerFactory.getLogger(LockUtils.class);

    static {
        //每5秒检查下是否过期
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                List<String> expireKeys = dbLockMap.values().stream().filter(l -> System.currentTimeMillis() - l.startTime >= l.expireTime).map(l -> l.key).collect(Collectors.toList());
                Optional.ofNullable(expireKeys).ifPresent(keys -> keys.stream().forEach(key -> dbLockMap.remove(key)));
            }
        }, 60 * 1000, 5000);
    }

    /**
     * 尝试锁定，根据key找到锁，尝试加锁，能加锁成功就可以继续执行
     *
     * @param key
     * @return
     */
    public static boolean tryWriteLock(String key, String user) {
        try {
            if (!dbLockMap.containsKey(key)) {
                dbLockMap.putIfAbsent(key, new LockInfo(key, EXPIRE_TIME));
            } else {
                if (dbLockMap.get(key).readLockUserSet.contains(user)) {
                    dbLockMap.get(key).setStartTime(System.currentTimeMillis());
                }
            }
            return dbLockMap.get(key).tryWriteLock(user);
        } catch (Exception e) {
            logger.error("锁定失败", e);
        }
        return false;
    }

    public static boolean tryReadLock(String key, String user) {
        try {
            if (!dbLockMap.containsKey(key)) {
                dbLockMap.putIfAbsent(key, new LockInfo(key, EXPIRE_TIME));
            } else {
                if (dbLockMap.get(key).readLockUserSet.contains(user)) {
                    dbLockMap.get(key).setStartTime(System.currentTimeMillis());
                }
            }
            return dbLockMap.get(key).tryReadLock(user);
        } catch (Exception e) {
            logger.error("锁定失败", e);
        }
        return false;
    }


    public static void unReadLock(String key, String user) {
        try {
            LockInfo lockInfo = dbLockMap.get(key);
            if (null != lockInfo) {
                lockInfo.unReadLock(user);
            }
        } catch (Exception e) {
            logger.error("解锁失败", e);
        }
    }

    public static void unWriteLock(String key, String user) {
        try {
            LockInfo lockInfo = dbLockMap.get(key);
            if (null != lockInfo) {
                lockInfo.unWriteLock(user);
            }
        } catch (Exception e) {
            logger.error("解锁失败", e);
        }
    }

    public static void unAllLock(String key, String user) {
        try {
            LockInfo lockInfo = dbLockMap.get(key);
            if (null != lockInfo) {
                lockInfo.unWriteLock(user);
                lockInfo.unReadLock(user);
            }
        } catch (Exception e) {
            logger.error("解锁失败", e);
        }
    }

    private static class LockInfo {
        //读锁的使用者
        public Set<String> readLockUserSet = new HashSet<>();

        //写锁的使用者
        public Set<String> writeLockUserSet = new HashSet<>();

        public String key;

        public long startTime;

        public long expireTime;

        public LockInfo(String key, long expireTime) {
            this.key = key;
            this.expireTime = expireTime;
            this.startTime = System.currentTimeMillis();
        }

        public boolean tryWriteLock(String user) {
            if (canUserWrite(user)) {
                //重入时直接成功
                if (writeLockUserSet.contains(user)) {
                    return true;
                }
                synchronized (this) {
                    if (canUserWrite(user)) {
                        writeLockUserSet.add(user);
                        return true;
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }

        public boolean tryReadLock(String user) {
            if (canUserRead(user)) {
                //重入时直接成功
                if (readLockUserSet.contains(user)) {
                    return true;
                }
                synchronized (this) {
                    if (canUserRead(user)) {
                        readLockUserSet.add(user);
                        return true;
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }


        public void unWriteLock(String user) {
            if (writeLockUserSet.contains(user)) {
                synchronized (this) {
                    writeLockUserSet.remove(user);
                    tryRemove();
                }
            }
        }

        public void unReadLock(String user) {
            if (readLockUserSet.contains(user)) {
                synchronized (this) {
                    readLockUserSet.remove(user);
                    tryRemove();
                }
            }
        }

        private void tryRemove() {
            if (readLockUserSet.isEmpty() && writeLockUserSet.isEmpty()) {
                dbLockMap.remove(key);
            }
        }

        //没有读(除自己) 且  没有写（除自己）
        private boolean canUserWrite(String user) {
            return (readLockUserSet.isEmpty() || (readLockUserSet.size() == 1 && readLockUserSet.contains(user))) && (writeLockUserSet.isEmpty() || (writeLockUserSet.size() == 1 && writeLockUserSet.contains(user)));
        }

        //没有写(除自己）
        private boolean canUserRead(String user) {
            return writeLockUserSet.isEmpty() || (writeLockUserSet.size() == 1 && writeLockUserSet.contains(user));
        }

        public Set<String> getReadLockUserSet() {
            return readLockUserSet;
        }

        public void setReadLockUserSet(Set<String> readLockUserSet) {
            this.readLockUserSet = readLockUserSet;
        }

        public Set<String> getWriteLockUserSet() {
            return writeLockUserSet;
        }

        public void setWriteLockUserSet(Set<String> writeLockUserSet) {
            this.writeLockUserSet = writeLockUserSet;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }
    }


}
