package joe.core.validation.custom;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * @Project: basic
 * @Package joe.core.validation.custom
 * @note: 自定义参数校验注解
 * @date Date : 2018年09月06日 17:12
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {
        StringBlankIfOtherExistsValidator.class,
        ListBlankIfOtherExistsValidator.class,
        MapBlankIfOtherExistsValidator.class
})
@Documented
public @interface BlankIfOtherExists {
    String message() default "不能为空";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] value();

    Class clazz();

    Logic logic() default Logic.OR;

    enum Logic {
        OR, AND
    }
}
