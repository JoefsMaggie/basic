package joe.core.validation.custom;

import com.fasterxml.jackson.databind.JsonNode;
import joe.core.utils.Jackson;
import joe.core.utils.UserContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintViolationCreationContext;
import org.hibernate.validator.internal.engine.path.PathImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 参数不为空校验
 *
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * Date : 2018年09月07日 13:51
 */
public class BlankIfOtherExistsValidator<T> implements ConstraintValidator<BlankIfOtherExists, T> {
    private BlankIfOtherExists blankIfOtherExists;

    @Override
    public void initialize(BlankIfOtherExists blankIfOtherExists) {
        this.blankIfOtherExists = blankIfOtherExists;
    }

    @Override
    public boolean isValid(T value, ConstraintValidatorContext context) {
        ConstraintValidatorContextImpl validatorContext = (ConstraintValidatorContextImpl) context;
        List<ConstraintViolationCreationContext> constraintViolationCreationContexts = validatorContext.getConstraintViolationCreationContexts();
        ConstraintViolationCreationContext constraintViolationCreationContext = constraintViolationCreationContexts.get(0);
        PathImpl path = constraintViolationCreationContext.getPath();
        if (Objects.nonNull(path.getLeafNode().getValue())) {
            if (value instanceof List) {
                return !((List) value).isEmpty();
            } else if (value instanceof Map) {
                return !((Map) value).isEmpty();
            }
            return true;
        }
        String[] paths = path.toString().split("\\.");
        if (paths.length > 3) {
            JsonNode jsonNode = UserContext.currentUserContext().jsonBody();
            Map<String, Object> jsonToMap = Jackson.jsonToMap(jsonNode.toString());
            String arg = paths[1];
            int index = arg.indexOf("[");
            if (index > 0) {
                Set<String> keys = jsonToMap.keySet();
                String trueKey = keys.stream().findFirst().orElseThrow(() -> new IllegalArgumentException("参数异常"));
                List<Map<String, Object>> list = (List) jsonToMap.get(trueKey);
                return check(list.get(Integer.parseInt(arg.substring(index + 1, arg.indexOf("]")))), paths);
            } else {
                return check(jsonToMap, paths);
            }
        }
        return false;
    }

    private boolean check(Map<String, Object> jsonToMap, String[] paths) {
        return this.check(jsonToMap, paths, 2);
    }

    private boolean check(Map<String, Object> jsonToMap, String[] split, int currentIndex) {
        String key = split[currentIndex];
        int index = key.indexOf("[");
        int nextIndex = currentIndex + 1;
        if (split.length == nextIndex) {
            Predicate<String> stringPredicate = field -> {
                Object obj = jsonToMap.get(field);
                if (obj instanceof List) {
                    return CollectionUtils.isNotEmpty((List) obj);
                } else if (obj instanceof Map) {
                    return CollectionUtils.isNotEmpty(((Map) obj).entrySet());
                } else {
                    return Objects.nonNull(obj) && StringUtils.isNotBlank(obj.toString());
                }
            };
            switch (blankIfOtherExists.logic()) {
                case AND:
                    return Stream.of(blankIfOtherExists.value()).allMatch(stringPredicate);
                default:
                    return Stream.of(blankIfOtherExists.value()).anyMatch(stringPredicate);
            }
        }
        if (index > 0) {
            String trueKey = key.substring(0, index);
            List o = (List) jsonToMap.get(trueKey);
            Map<String, Object> map = (Map) o.get(Integer.valueOf(key.substring(index + 1, key.indexOf("]"))));
            return check(map, split, nextIndex);
        } else {
            return check((Map<String, Object>) jsonToMap.get(split[currentIndex]), split, nextIndex);
        }
    }
}
