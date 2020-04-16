package joe.core.security;

import com.fasterxml.jackson.databind.JsonNode;
import joe.core.exception.CommonRuntimeException;
import joe.core.utils.UserContext;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;


/**
 * 安全校验，对 key 做校验
 *
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * Date : 2018年09月05日 11:36
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 500)
public class SecurityCheckAspect {

    private final static Logger LOGGER = LoggerFactory.getLogger(SecurityCheckAspect.class);

    @Value("${security.key:}")
    private String secret;

    /**
     * 切面定义
     */
    @Pointcut("execution(* joe..controllers..*.*(..)) && within(@(@org.springframework.stereotype.Controller *) *)")
    public void signAspect() {
        // empty
    }

    /**
     * 前置通知，即校验逻辑
     */
    @Before("signAspect()")
    public void before() {
//            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//            HttpServletRequest request = requestAttributes.getRequest();
        JsonNode jsonNode = UserContext.currentUserContext().jsonBody();
        JsonNode keyNode = jsonNode.get("key");
//        String key = UserContext.currentUserContext().getKey();
        if (Objects.isNull(keyNode) || !StringUtils.equals(keyNode.asText(), secret)) {
            LOGGER.error("签名校验错误，请联系管理员");
            throw new CommonRuntimeException("403", "签名校验错误，请联系管理员");
        }
    }
}
