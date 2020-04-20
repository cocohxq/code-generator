package ${javaPackage};

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果接口
 */
public class PagedResult<T> implements Serializable {
    private static final long serialVersionUID = -1L;

    private int curPage;
    private int pageSize;
    private int totalPage;
    private long totalNum;
    private List<T> data;


    public PagedResult(
            int curPage,
            int pageSize,
            long totalNum,
            List<T> data) {
        this.curPage = curPage;
        this.pageSize = pageSize;
        this.totalNum = totalNum;
        this.data = data;
        this.totalPage = this.calculateTotalPages(this.totalNum, this.pageSize);
    }

    public int getCurPage() {
        return this.curPage;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(long totalNum) {
        this.totalNum = totalNum;
    }

    public List<T> getData() {
        return this.data;
    }

    private int calculateTotalPages(long totalNum, int pageSize) {
        if (pageSize == 0) return 0;

        if (totalNum % pageSize == 0) {
            return (int)(totalNum / pageSize);
        } else {
            return (int)(totalNum / pageSize) + 1;
        }
    }
}