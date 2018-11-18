package joe.elasticsearch.helper.factory.agg;

import joe.elasticsearch.common.AggType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * @Project : joe
 * @Package : joe.elasticsearch.helper.factory.agg
 * @Description : 用于标识是哪类聚合类型
 * @date : 2018年09月30日 15:25
 */
@Target({TYPE})
@Retention(RUNTIME)
public @interface AggFactoryType {
    AggType value();
}
