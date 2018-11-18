package joe.core.utils;


import com.google.common.collect.Sets;
import joe.core.communicate.Response;
import joe.core.communicate.StringResponse;
import joe.core.exception.CommonRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Set;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * @Project: basic
 * @Package joe.core.exception
 * @note: 异常工具类
 * @date Date : 2018年09月04日 10:02
 */
public class ExceptionUtil {
    public static String getSimpleStackTrace(Throwable throwable) {
        String wholeStackTraceString = ExceptionUtils.getStackTrace(throwable);
        String[] lines = StringUtils.split(wholeStackTraceString, "\n");
        Set<String> traceLines = Sets.newLinkedHashSetWithExpectedSize(10);
        for (String line : lines) {
            if (line.startsWith("com.")) {
                traceLines.add(line);
            } else if (line.startsWith("\tat com.")) {
                traceLines.add(line);
            } else if (line.startsWith("Caused")) {
                traceLines.add(line);
            } else if (!line.startsWith("\t")) {
                traceLines.add(line);
            }
        }
        return StringUtils.join(traceLines, "\n");
    }

    public static String getMessage(Throwable throwable) {
        String msg;
        if (throwable instanceof CommonRuntimeException) {
            msg = ((CommonRuntimeException) throwable).getErrorMsg();
        } else {
            msg = throwable.getMessage();
        }
        return msg;
    }

    public static String getErrorCode(Throwable throwable) {
        String errorCode;
        if (throwable instanceof CommonRuntimeException) {
            errorCode = ((CommonRuntimeException) throwable).getErrorCode();
        } else {
            errorCode = Response.ERROR_CODE;
        }
        return errorCode;
    }

    public static Response<StringResponse> createResponse(Throwable throwable) {
        return createResponse(getErrorCode(throwable), getMessage(throwable));
    }

    public static Response<StringResponse> createResponse(String errorCode, String... errorMsg) {
        return Response.error(errorCode, errorMsg);
    }

    public static CommonRuntimeException createException(Throwable throwable) {
        return new CommonRuntimeException(getErrorCode(throwable), getMessage(throwable));
    }
}
