package joe.core.validation;

import joe.core.exception.ValidationException;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * 校验器
 *
 * @author chenwenlong@foresee.com.cn
 * @version 1.0
 * Date 2017年04月18日
 */
public class Validators {

//    private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    private static ValidatorFactory factory;

    @Resource(name = "JoeValidator")
    public void setFactory(ValidatorFactory factory) {
        Validators.factory = factory;
    }

    /**
     * @param object 校验对象
     */
    public static void validate(Object object) {
        if (object == null) {
            return;
        }
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> errors = validator.validate(object);
        if (errors.isEmpty()) {
            return;
        }
        throw new ValidationException(errors);
    }

    /**
     * 判断是否通过校验
     *
     * @param object 校验对象
     */
    public static boolean validateNoException(Object object) {
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> errors = validator.validate(object);
        return errors.isEmpty();

    }

    public static void validateParameters(Object object, Method method, Object... values) {
        if (object == null) {
            return;
        }
        Set<ConstraintViolation<Object>> errors = factory.getValidator()
                .forExecutables()
                .validateParameters(object, method, values);
        if (errors.isEmpty()) {
            return;
        }
        throw new ValidationException(errors, method);
    }
}
