package joe.core.resolver.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 *  参数校验注解，需要参数校验的参数前需加此注解
 * Date Date : 2018年09月04日 13:46
 */
@Retention(RUNTIME)
@Target({METHOD, FIELD, CONSTRUCTOR, PARAMETER})
public @interface Var {
}