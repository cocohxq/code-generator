package ${javaPackage};

import java.io.Serializable;

/**
 *  分页信息
 */
public class Page implements Serializable {
    private static final long serialVersionUID = -1L;


    private int curPage = 1;// 当前页
    private int pageSize = 10;// 每页的记录条数


    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStartIndex() {
        int startIndex = (this.curPage - 1) * this.pageSize;
        return startIndex < 0 ? 0 : startIndex;
    }

}
