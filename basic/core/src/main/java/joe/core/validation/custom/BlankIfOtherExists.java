package joe.core.validation.custom;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


/**
 * 自定义参数校验注解
 *
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * Date : 2018年09月06日 17:12
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
