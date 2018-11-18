package joe.core.validation;

import joe.core.filter.RepeatableRequestFilter;
import joe.core.interceptor.RestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * @Project: basic
 * @Package joe.core.validation
 * @note: 参数校验需要的 bean 的配置类
 * @date Date : 2018年09月04日 11:35
 */
@Configuration
public class ValidatorAspectConfiguration {

    @Bean
    public Validators validators() {
        return new Validators();
    }

    @Bean
    public RestInterceptor restInterceptor() {
        return new RestInterceptor();
    }

    @Bean
    public ControllerArgsAspect authControllerAspect() {
        return new ControllerArgsAspect();
    }

    @Bean
    public RepeatableRequestFilter repeatableRequestFilter() {
        return new RepeatableRequestFilter();
    }
}
