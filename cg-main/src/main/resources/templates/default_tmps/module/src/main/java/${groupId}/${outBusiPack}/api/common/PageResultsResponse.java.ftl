package ${javaPackage};

import ${commonValueStack.getValue("PageResult.classPath")!""};
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 带可分页数据的响应结果
 */
public class PagedResultsResponse<T> extends Response<List<T>> implements Serializable {

    private static final long serialVersionUID = -3994082324815695837L;

    private Pagination page;

    /**
     * 获取分页信息
     *
     * @return
     */
    public Pagination getPage() {
        return page;
    }

    public void setPage(Pagination page) {
        this.page = page;
    }

    public static <T> PagedResultsResponse<T> initializePagedResultsResponse() {
        PagedResultsResponse<T> pagedResultsResponse = new PagedResultsResponse<>();
        pagedResultsResponse.setCode(ResponseConstants.CODE_SUCCESS);
        return pagedResultsResponse;
    }

    public static <T> PagedResultsResponse<T> writeSuccess(PagedResult<T> pagedResult) {
        PagedResultsResponse<T> pagedResultsResponse = new PagedResultsResponse<>();
        pagedResultsResponse.setCode(ResponseConstants.CODE_SUCCESS);
        pagedResultsResponse.setMessage(ResponseConstants.MSG_SUCCESS);
        if (pagedResult != null && CollectionUtils.isNotEmpty(pagedResult.getData())) {
            Pagination pagination = new Pagination();
            pagination.setCurPage(pagedResult.getCurPage());
            pagination.setPageSize(pagedResult.getPageSize());
            pagination.setTotalPage(pagedResult.getTotalPage());
            pagination.setTotalNum(pagedResult.getTotalNum());
            pagedResultsResponse.setPage(pagination);
            pagedResultsResponse.setData(pagedResult.getData());
        }
        return pagedResultsResponse;
    }

    public static <T> PagedResultsResponse<T> writeSuccess(List<T> data, Pagination page) {
        PagedResultsResponse<T> pagedResultsResponse = new PagedResultsResponse<>();
        pagedResultsResponse.setCode(ResponseConstants.CODE_SUCCESS);
        pagedResultsResponse.setMessage(ResponseConstants.MSG_SUCCESS);
        pagedResultsResponse.setPage(page);
        pagedResultsResponse.setData(data);
        return pagedResultsResponse;
    }

    public static <T> PagedResultsResponse<T> empty(BaseQuery query) {
        PagedResultsResponse<T> pagedResultsResponse = new PagedResultsResponse<>();
        pagedResultsResponse.setCode(ResponseConstants.CODE_SUCCESS);
        pagedResultsResponse.setMessage(ResponseConstants.MSG_SUCCESS);
        Pagination page = new Pagination();
        page.setCurPage(query.getCurPage());
        page.setPageSize(query.getPageSize());
        page.setTotalNum(0);
        page.setTotalPage(0);
        pagedResultsResponse.setPage(page);
        return pagedResultsResponse;
    }

    public static PagedResultsResponse writeError(String messageText) {
        return writeError(null, messageText);
    }

    public static PagedResultsResponse writeError(String messageCode, String messageText) {
        PagedResultsResponse pagedResultsResponse = new PagedResultsResponse<>();
        pagedResultsResponse.setSuccess(false);
        pagedResultsResponse.setCode(StringUtils.isEmpty(messageCode) ? ResponseConstants.CODE_DEFAULT_FAILURE : messageCode);
        pagedResultsResponse.setMessage(StringUtils.isEmpty(messageText) ? ResponseConstants.MSG_DEFAULT_FAILURE : messageText);
        return pagedResultsResponse;
    }

}
