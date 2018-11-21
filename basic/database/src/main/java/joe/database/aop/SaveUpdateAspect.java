package joe.database.aop;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;

/**
 * mybatis 更新修改切面
 *
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * Date: 2018/11/21
 */
@Slf4j
@Aspect
@Component
public class SaveUpdateAspect {

    @Pointcut("execution(* joe..*.model.mapper.*.save*(..))")
    public void savePointcut() {
        // empty
    }

    @Before("savePointcut()")
    public void save(JoinPoint point) {
        Arrays.stream(point.getArgs()).forEach(arg -> {
            final val date = new Date();
            Arrays.stream(arg.getClass().getDeclaredMethods())
                  .filter(method -> StringUtils.startsWith(method.getName(), "set"))
                  .filter(method -> StringUtils.containsIgnoreCase(method.getName(), "createTime")
                          || StringUtils.containsIgnoreCase(method.getName(), "updateTime")
                          || StringUtils.containsIgnoreCase(method.getName(), "createDate")
                          || StringUtils.containsIgnoreCase(method.getName(), "updateDate"))
                  .forEach(method -> {
                      try {
                          method.invoke(arg, date);
                      } catch (IllegalAccessException | InvocationTargetException e) {
                          log.error("反射注入时间报错", e);
                      }
                  });
        });
    }

    @Pointcut("execution(* joe..*.model.mapper.*.update*(..))")
    public void updatePointcut() {
        // empty
    }

    @Before("updatePointcut()")
    public void update(JoinPoint point) {
        Arrays.stream(point.getArgs()).forEach(arg -> {
            final val date = new Date();
            Arrays.stream(arg.getClass().getDeclaredMethods())
                  .filter(method -> StringUtils.startsWith(method.getName(), "set"))
                  .filter(method -> StringUtils.containsIgnoreCase(method.getName(), "updateTime")
                          || StringUtils.containsIgnoreCase(method.getName(), "updateDate"))
                  .forEach(method -> {
                      try {
                          method.invoke(arg, date);
                      } catch (IllegalAccessException | InvocationTargetException e) {
                          log.error("反射注入时间报错", e);
                      }
                  });
        });
    }
}
