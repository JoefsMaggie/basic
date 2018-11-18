package joe.core.config;

import joe.core.interceptor.RestInterceptor;
import joe.core.resolver.DefaultArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 * @note: 参数解析器
 * Date Date : 2018年09月04日 13:46
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private RequestMappingHandlerAdapter adapter;

    private final RestInterceptor restInterceptor;

    @Autowired
    public WebMvcConfig(RestInterceptor restInterceptor) {
        this.restInterceptor = restInterceptor;
    }

    @PostConstruct
    public void prioritizeCustomArgumentMethodHandlers() {
        if (restInterceptor == null) {
            return;
        }
        List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>(Objects.requireNonNull(adapter.getArgumentResolvers()));

        argumentResolvers.add(0, new DefaultArgumentResolver());

        adapter.setArgumentResolvers(argumentResolvers);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON_UTF8);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        super.addInterceptors(registry);
        if (restInterceptor == null) {
            return;
        }
        registry.addInterceptor(restInterceptor)
                .addPathPatterns("/controllers/*/**");
    }

    /**
     * 发现如果继承了WebMvcConfigurationSupport，则在yml中配置的相关内容会失效。 需要重新指定静态资源
     *
     * @param registry 静态资源注册器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//        super.addResourceHandlers(registry);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        if (restInterceptor == null) {
            return;
        }
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converters.add(0, converter);
    }
}
