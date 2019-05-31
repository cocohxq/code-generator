package com.wumart.scm.framework.core.arch.service.contract;

import java.io.Serializable;

/**
 * 分页信息
 */
public class Pagination implements Serializable{

    private int curPage;
    private int pageSize;
    private int totalPage;
    private long totalNum;

    public Pagination() {
    }

    public Pagination(int pageSize) {
        this.curPage = 1;
        this.pageSize = pageSize;
        this.totalNum = 0;
        this.totalPage = 0;
    }

    public Pagination(int curPage, int pageSize, int totalPage, long totalNum) {
        this.curPage = curPage;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.totalNum = totalNum;
    }

    /**
     * 获取当前页序号
     * @return
     */
    public int getCurPage() {
        return curPage;
    }

    /**
     * 设置当前页号
     * @param curPage
     */
    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    /**
     * 获取每页大小
     * @return
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页大小
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 获取总页数
     * @return
     */
    public int getTotalPage() {
        return totalPage;
    }

    /**
     * 设置总页数
     * @param totalPage
     */
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    /**
     * 获取总记录数
     * @return
     */
    public long getTotalNum() {
        return totalNum;
    }

    /**
     * 设置总记录数
     * @param totalNum
     */
    public void setTotalNum(long totalNum) {
        this.totalNum = totalNum;
    }
}