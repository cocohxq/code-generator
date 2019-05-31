package ${javaPackage};

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 抽象的请求响应
 */
public class Response<T> implements Serializable {


    public static final String CODE_SUCCESS = "0";
    public static final String CODE_DEFAULT_FAILURE = "1";

    public static final String CODE_NO_SESSION = "-1";
    public static final String CODE_NO_AUTH = "-2";

    public static final String MSG_SUCCESS = "操作成功";
    public static final String MSG_DEFAULT_FAILURE = "操作失败";
    
    private static final long serialVersionUID = 8183507142540398148L;

    private String code;

    private String message;

    private T data;

    private Boolean isSuccess = true;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    protected void setSuccess(Boolean success) {
        isSuccess = success;
    }

    /**
     * 获取列表数据
     *
     * @return
     */
    public T getData() {
        return data;
    }

    /**
     * 设置数据
     *
     * @param data
     */
    public void setData(T data) {
        this.data = data;
    }

    public Boolean getIsSuccess() {
        if (isSuccess) {
            return true;
        }
        return message != null && CODE_SUCCESS.equals(code);
    }

    public static <T> Response<T> initializedResult() {
        Response<T> response = new Response<>();
        response.setCode(CODE_SUCCESS);
        return response;
    }

    public static <T> Response<T> writeSuccess() {
        return writeSuccess(null, null);
    }

    public static <T> Response<T> writeSuccess(String messageText) {
        return writeSuccess(messageText, null);
    }

    public static <T> Response<T> writeSuccess(T data) {
        return writeSuccess(null, data);
    }

    public static <T> Response<T> writeSuccess(String messageText, T data) {
        Response<T> response = new Response<>();
        response.setSuccess(true);
        response.setCode(CODE_SUCCESS);
        response.setMessage(StringUtils.isEmpty(messageText) ? MSG_SUCCESS : messageText);
        response.setData(data);
        return response;
    }

    public static <T> Response<T> writeSuccess(String messageCode, String messageText, T data) {
        Response<T> response = new Response<>();
        response.setSuccess(true);
        response.setCode(messageCode);
        response.setMessage(StringUtils.isEmpty(messageText) ? MSG_SUCCESS : messageText);
        response.setData(data);
        return response;
    }

    public static <T> Response<T> writeError(String messageText) {
        return writeError(null, messageText);
    }

    public static <T> Response<T> writeError(String messageCode, String messageText) {
        Response<T> response = Response.initializedResult();
        response.setSuccess(false);
        response.setCode(StringUtils.isEmpty(messageCode) || CODE_SUCCESS.equals(messageCode) ? CODE_DEFAULT_FAILURE : messageCode);
        response.setMessage(StringUtils.isEmpty(messageText) ? MSG_DEFAULT_FAILURE : messageText);
        return response;
    }

    public static <T> Response<T> writeError(String messageCode, String messageText, T data) {
        Response<T> response = Response.initializedResult();
        response.setSuccess(false);
        response.setCode(StringUtils.isEmpty(messageCode) || CODE_SUCCESS.equals(messageCode) ? CODE_DEFAULT_FAILURE : messageCode);
        response.setMessage(StringUtils.isEmpty(messageText) ? MSG_DEFAULT_FAILURE : messageText);
        response.setData(data);
        return response;
    }

    public static class Builder<T> {

        private String code;

        private String message;

        private T data;

        private boolean success = true;

        public Builder setCode(String code) {
            this.code = code;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public Builder setData(T data) {
            this.data = data;
            return this;
        }

        public <T> Response<T> build() {
            Response response = Response.initializedResult();
            response.setSuccess(success);
            response.setData(data);
            response.setCode(code);
            response.setMessage(message);
            return response;
        }
    }

}