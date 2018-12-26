package com.github.codegenerator.common.util;

import java.util.List;

public class SortUtils {


    public static final int SORT_ASC = 1;
    public static final int SORT_DESC = -1;


    /**
     * 字符数组排序
     * @param arr
     * @param sortType
     */
    public static void sort(List<String> arr, int sortType) {
        if (null == arr) {
            return;
        }
        //冒泡排序
        for (int f = 0;f < arr.size() - 1; f++) {
            String front = arr.get(f);
            for (int b = f + 1; b < arr.size(); b++) {
                String back = arr.get(b);
                if(sort(back,front,sortType) == -1){//需要更换位置
                    arr.set(f,back);
                    arr.set(b,front);
                    front = back;
                }
            }
        }
    }

    /**
     * 同Collections.sort的使用习惯保持一致
     *
     * @param back
     * @param front
     * @param sortType 1:asc  -1:desc
     * @return 按sortType而言 0,1:无需换位置  -1：需要更换位置
     */
    public static int sort(String back, String front, int sortType) {
        if (null == back || null == front) {
            throw new RuntimeException("参数异常");
        }
        char[] b = back.toCharArray();
        char[] f = front.toCharArray();

        int loopCount = b.length > f.length ? f.length : b.length;
        //逐个比较
        for (int i = 0; i < loopCount - 1; i++) {
            if (b[i] > f[i]) {//后者比前者大时，asc返回1，desc返回-1
                return sortType;
            } else if (b[i] < f[i]) {
                return -sortType;
            }
        }
        return b.length >= f.length ? sortType : -sortType;
    }


}
