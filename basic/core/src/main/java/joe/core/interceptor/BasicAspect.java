package joe.core.interceptor;

import joe.core.exception.CommonRuntimeException;
import joe.core.utils.Loggers;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * 基础切面拦截
 * Date: 2018/10/30
 */
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE + 100)
public class BasicAspect {
    private final static Logger LOGGER = Loggers.getLogger();

    // Controller层切点
//    @Pointcut("execution(* joe..controllers..*.*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    @Pointcut("execution(* joe..controllers..*.*(..)) && within(@(@org.springframework.stereotype.Controller *) *)")
    public void controllerAspect() {
        // empty body
    }

    @Around("controllerAspect()")
    public Object interceptor(ProceedingJoinPoint p) {
        MethodInvocationProceedingJoinPoint pjp = (MethodInvocationProceedingJoinPoint) p;
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String methodName = signature.getMethod().getName();
        Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
        Object target = pjp.getTarget();
        try {
            StopWatch clock = new StopWatch();
            clock.start(); // 计时开始
            Object proceed = pjp.proceed();
            clock.stop(); // 计时结束
            if (Objects.isNull(target)) {
                throw new CommonRuntimeException("500", "未知错误，请联系管理员");
            }
            Method method = target.getClass().getMethod(methodName, parameterTypes);
            LOGGER.info("\nrequest controller: {}, \nrequest controller method: {}, \nmethod processing time：{} ms",
                        target.getClass().getName(), method.getName(), clock.getTime());
            return proceed;
        } catch (Throwable throwable) {
            LOGGER.error("异常：", throwable);
            throw new CommonRuntimeException("500", "未知错误，请联系管理员");
        }
    }
}
