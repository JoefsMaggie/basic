package joe.core.validation;

import joe.core.communicate.Response;
import joe.core.exception.CommonRuntimeException;
import joe.core.exception.ValidationException;
import joe.core.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * @Project: basic
 * @Package joe.core.validation
 * @note: 安全校验
 * @date Date : 2018年09月04日 10:46
 */
@Aspect
@Order()
public class ControllerArgsAspect {
    private final static Logger LOGGER = Loggers.getLogger();

    // Controller层切点
//    @Pointcut("execution(* joe..controllers..*.*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    @Pointcut("execution(* joe..controllers..*.*(..)) && within(@(@org.springframework.stereotype.Controller *) *)")
    public void controllerAspect() {
        // empty body
    }

    @Around("controllerAspect()")
    public Object interceptor(ProceedingJoinPoint p) {
        MethodSignature signature = null;
        try {
            LOGGER.info("Request Parameters:\n{}", UserContext.currentUserContext().jsonBody());
            MethodInvocationProceedingJoinPoint pjp = (MethodInvocationProceedingJoinPoint) p;

            Object[] args = pjp.getArgs();
            signature = (MethodSignature) pjp.getSignature();
            String methodName = signature.getMethod().getName();
            Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
            Object target = pjp.getTarget();
            if (Objects.isNull(target)) {
                throw new CommonRuntimeException("500", "未知错误，请联系管理员");
            }
            Method method = target.getClass().getMethod(methodName, parameterTypes);

            try {
                Validators.validateParameters(target, method, args);
            } catch (ValidationException e) {
                throw e;
            } catch (Exception e) {
                for (Object arg : args) {
                    Validators.validate(arg);
                }
            }

            StopWatch clock = new StopWatch();
            clock.start(); // 计时开始
            Object proceed = pjp.proceed();
            clock.stop(); // 计时结束
            LOGGER.info("\nrequest controller: {}, \nrequest controller method: {}, \nmethod processing time：{} ms",
                        target.getClass().getName(), method.getName(), clock.getTime());

            return proceed;
        } catch (Throwable throwable) {
            return handleException(throwable, signature);
        }
    }

    private Object handleException(Throwable throwable, MethodSignature signature) {
        String message = throwable.getMessage();
        if (StringUtils.isBlank(message)) {
            message = throwable.toString();
        }
        String traces = ExceptionUtil.getSimpleStackTrace(throwable);
        String rawBody = "";
        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = RequestUtils.getRequest();
            rawBody = RequestUtils.getRawBody(request);
        }
        if (throwable instanceof ValidationException) {
            List<ValidationException.ValidationDetail> details = ((ValidationException) throwable).getDetails();
            LOGGER.error("校验失败, Request:{}, {}\n{}", signature, Jackson.toJson(details), rawBody);
            return Response.error("400", message);
        } else {
            LOGGER.error("Handle Error, {}\nRequest:{}", traces, rawBody);
        }
        if (throwable instanceof CommonRuntimeException) {
            throw (CommonRuntimeException) throwable;
        }
        throw new RuntimeException(throwable);
    }

}


