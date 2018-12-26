package ${javaPackage};

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果接口
 */
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = -1L;

    private int curPage;
    private int pageSize;
    private int totalPages;
    private int totalRecords;
    private List<T> data;


    public PageResult(
            int curPage,
            int pageSize,
            int totalRecords,
            List<T> data) {
        this.curPage = curPage;
        this.pageSize = pageSize;
        this.totalRecords = totalRecords;
        this.data = data;
        this.totalPages = this.calculateTotalPages(this.totalRecords, this.pageSize);
    }

    public int getCurPage() {
        return this.curPage;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getTotalPages() {
        return this.totalPages;
    }

    public int getTotalRecords() {
        return this.totalRecords;
    }

    public List<T> getData() {
        return this.data;
    }

    private int calculateTotalPages(int totalRecords, int pageSize) {
        if (pageSize == 0) return 0;

        if (totalRecords % pageSize == 0) {
            return (totalRecords / pageSize);
        } else {
            return (totalRecords / pageSize) + 1;
        }
    }
}