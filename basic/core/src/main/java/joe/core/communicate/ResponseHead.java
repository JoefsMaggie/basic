package joe.core.communicate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 * @note: 返回头信息
 * Date Date : 2018年09月04日 15:59
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseHead implements Serializable {
    private static final long serialVersionUID = 4572910611616655490L;

    public static final String SUCCESS_CODE = "0";

    public static final String REDIRECT_CODE = "302";

    @JsonProperty("return_code")
    private String errorCode = SUCCESS_CODE;
    @JsonProperty("return_msg")
    private String message;
    private long timestamp = System.currentTimeMillis();

    public boolean isRedirect() {
        return REDIRECT_CODE.equals(errorCode);
    }

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(errorCode) || isRedirect();
    }

    public boolean isError() {
        return !isSuccess();
    }

    public ResponseHead() {

    }

    public ResponseHead(String errorCode, String message) {
        super();
        this.errorCode = errorCode;
        this.message = message;
    }

    public ResponseHead(String errorCode, String message, long timestamp) {
        super();
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public ResponseHead setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResponseHead setMessage(String message) {
        this.message = message;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ResponseHead setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
