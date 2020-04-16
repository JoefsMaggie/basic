package joe.core.resolver;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import joe.core.utils.AppUtils;
import joe.core.utils.RequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * RequestMapping 方法参数注入
 *
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * Date : 2018年09月04日 13:46
 */
public class DefaultArgumentResolver implements HandlerMethodArgumentResolver {

    private Map<MethodParameter, ArgumentResolver> argumentResolverCache = Maps.newConcurrentMap();

    /**
     * 判断是否需要解析注入
     *
     * @param parameter 方法参数
     * @return 需要与否
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        HttpServletRequest request = RequestUtils.getRequest();

        String contentType = request.getHeader("Content-Type");
        if (StringUtils.isBlank(contentType)) {
            return false;
        }
        if (!contentType.startsWith("application/json")) {
            return false;
        }

        if (!"POST".equals(request.getMethod())) {
            return false;
        }

        /*boolean containsSpringAnnotation = containsSpringAnnotation(parameter);
        if (containsSpringAnnotation) {
            return false;
        }*/

        for (ArgumentResolver argumentResolver : getArgumentResolvers()) {
            if (argumentResolver.support(parameter, request)) {
                argumentResolverCache.put(parameter, argumentResolver);
                return true;
            }
        }
        return false;
    }

    /**
     * 获取参数解析器
     *
     * @return 参数解析器列表
     */
    private List<ArgumentResolver> getArgumentResolvers() {
        Collection<ArgumentResolver> values = AppUtils.getContext().getBeansOfType(ArgumentResolver.class).values();
        List<ArgumentResolver> argumentResolvers = Lists.newArrayList(values);
        argumentResolvers.sort(AnnotationAwareOrderComparator.INSTANCE);
        return argumentResolvers;
    }

    /**
     * 判断是否需要对参数做解析
     *
     * @param parameter 参数
     * @return 是否包含spring里面的注解
     */
    private boolean containsSpringAnnotation(MethodParameter parameter) {
        Annotation[] parameterAnnotations = parameter.getParameterAnnotations();
        String springPkgName = ResponseBody.class.getPackage().getName();
        return Stream.of(parameterAnnotations)
                .anyMatch(parameterAnnotation ->
                        parameterAnnotation.annotationType().getName().startsWith(springPkgName));
    }

    /**
     * 解析注入参数
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

        return argumentResolverCache.get(parameter)
                .resolveArgument(parameter, mavContainer, webRequest, binderFactory);
    }

}