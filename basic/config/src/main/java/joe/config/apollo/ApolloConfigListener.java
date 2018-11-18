package joe.config.apollo;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * @Project : joe
 * @Package : joe.config.apollo
 * @Description : apollo 配置修改监听
 * @date : 2018年10月15日 15:11
 */
public interface ApolloConfigListener {
    Logger LOGGER = LoggerFactory.getLogger(ApolloConfigListener.class);

    default void configurationPropertiesListener(ConfigChangeEvent changeEvent, String apolloPrefix) {
        Field[] declaredFields = this.getClass().getDeclaredFields();
        Arrays.stream(declaredFields).forEach(field -> {
            String property = apolloPrefix + "." + field.getName();
            if (changeEvent.isChanged(property)) {
                field.setAccessible(true);
                String newValue = changeEvent.getChange(property).getNewValue();
                try {
                    if (StringUtils.equalsIgnoreCase(field.getType().getName(), "boolean")) {
                        boolean newBool = BooleanUtils.toBoolean(newValue);
                        field.set(this, newBool);
                    } else {
                        field.set(this, newValue);
                    }
                    LOGGER.info("{} 字段 {} 值修改为 {}", this.getClass().getName(), field.getName(), newValue);
                } catch (IllegalAccessException e) {
                    LOGGER.error("反射赋值报错，字段：{}，新值：{}", field.getName(), newValue, e);
                }
            }
        });
    }

}
