package joe.core.validation.annotation;

import joe.core.validation.ValidatorAspectConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 * @note: 是否启动参数校验，以弃用，以默认开启
 * Date Date : 2018年09月04日 11:36
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ValidatorAspectConfiguration.class)
public @interface EnableAutoValidate {

}
