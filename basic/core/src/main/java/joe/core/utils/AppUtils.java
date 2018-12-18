package joe.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 *  ApplicationContext 工具类
 * Date Date : 2018年09月04日 13:46
 */
public class AppUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static <T> T aopWrap(T object) {
        if (object == null) {
            return null;
        }
        return context.getBean((Class<T>) object.getClass());
    }

    public static <T> T aopWrap(String beanName, T object) {
        if (object == null) {
            return null;
        }
        return context.getBean(beanName, (Class<T>) object.getClass());
    }
}