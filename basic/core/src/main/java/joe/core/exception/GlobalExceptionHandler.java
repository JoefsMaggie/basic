package joe.core.exception;

import joe.core.communicate.Response;
import joe.core.communicate.StringResponse;
import joe.core.utils.ExceptionUtil;
import joe.core.utils.Loggers;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : Joe
 * @version V1.0
 * @Project: basic
 * @Package joe.core.exception
 * @note: 全局异常处理器
 * @date Date : 2018年09月04日 9:50
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private final static Logger LOGGER = Loggers.getLogger();

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Response<StringResponse> handleException(HttpServletResponse response,
                                                    Exception exception) throws IOException {
        String stackTrace = ExceptionUtil.getSimpleStackTrace(exception);
        LOGGER.error("Handle Error, {}", stackTrace);
        if (!(exception instanceof CommonRuntimeException)) {
            return ExceptionUtil.createResponse(Response.ERROR_CODE);
        }
//        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
//        response.getWriter().write(Jackson.toJson(Collections.singletonMap("1", "呀！！！系统出现一点小问题，请稍后再来！")));
//        response.getWriter().flush();
        return ExceptionUtil.createResponse(exception);
    }
}
