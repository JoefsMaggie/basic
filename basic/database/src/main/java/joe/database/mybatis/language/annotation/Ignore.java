package joe.database.mybatis.language.annotation;

import java.lang.annotation.*;

/**
 * 是否忽略
 *
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * Date: 2018/11/20
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Ignore {
    IgnoreSqlType[] value() default IgnoreSqlType.ALL;
}
