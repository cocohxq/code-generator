package com.github.codegenerator.common.util;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockUtils {

    private ReadWriteLock lock = new ReentrantReadWriteLock();
}
