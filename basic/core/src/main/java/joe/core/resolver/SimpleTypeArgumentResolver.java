package joe.core.resolver;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import joe.core.resolver.annotation.Var;
import joe.core.utils.Jackson;
import joe.core.utils.UserContext;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * @Project: basic
 * @Package joe.core.validation
 * @note: 参数解析器
 * @date Date : 2018年09月04日 13:46
 */
@Component
@Order(1)
public class SimpleTypeArgumentResolver implements ArgumentResolver {

    /**
     * 判断是否需要做参数注入
     *
     * @param parameter          方法参数
     * @param httpServletRequest 报文
     * @return boolean
     */
    @Override
    public boolean support(MethodParameter parameter, HttpServletRequest httpServletRequest) {
        Class<?> parameterType = parameter.getParameterType();
        return ClassUtils.isPrimitiveOrWrapper(parameterType)
                || (parameterType == String.class)
                || parameter.hasParameterAnnotation(Var.class)
                || parameter.hasParameterAnnotation(Valid.class);
    }

    /**
     * 参数注入实现逻辑
     *
     * @param parameter     方法参数
     * @param mavContainer  容器
     * @param webRequest    请求参数
     * @param binderFactory 绑定的工厂类
     * @return 注入后的参数
     * @throws Exception 异常
     */
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        JsonNode bodyNode = UserContext.currentUserContext().jsonBody();

        Class<?> parameterType = parameter.getParameterType();
        String parameterName = parameter.getParameterName();

        Type type = parameter.getGenericParameterType();
        JavaType javaType = Jackson.constructType(type);

        if (ClassUtils.isPrimitiveOrWrapper(parameterType)
                || Collection.class.isAssignableFrom(parameterType)
                || parameterType == String.class
                || Map.class.isAssignableFrom(parameterType)) {
            JsonNode fieldNode = bodyNode.get(parameterName);
            if (fieldNode == null) {
                return zeroValue(parameterType);
            }
            return Jackson.readerFor(javaType).readValue(fieldNode);
        }
        return Jackson.readerFor(javaType).readValue(bodyNode);
    }

    /**
     * 默认值注入
     *
     * @param parameterType 参数类型
     * @return 默认值
     */
    private Object zeroValue(Class<?> parameterType) {
        if (!parameterType.isPrimitive()) {
            return null;
        }
        if (parameterType == boolean.class) {
            return false;
        }
        return 0;
    }
}