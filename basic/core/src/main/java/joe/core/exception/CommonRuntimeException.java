package joe.core.exception;

/**
 * @author : Joe
 * @version V1.0


 *  通用异常
 * Date Date : 2018年09月04日 9:19
 */
public class CommonRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 6358803642758332780L;

    private String errorCode;

    private String errorMsg;

    private Object[] params;

    public CommonRuntimeException(String errorCode, Object... params) {
        this(errorCode, "", params);
    }

    public CommonRuntimeException(String errorCode, String errorMsg, Object... params) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.params = params;
    }

    public CommonRuntimeException(String errorCode, Throwable t, Object... params) {
        this(errorCode, "", t, params);
    }

    public CommonRuntimeException(String errorCode, String errorMsg, Throwable t, Object... params) {
        super(errorCode + " : " + errorMsg, t);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.params = params;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public Object[] getParams() {
        return params;
    }
}
