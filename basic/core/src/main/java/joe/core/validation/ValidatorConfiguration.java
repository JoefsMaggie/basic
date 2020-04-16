package joe.core.validation;

import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

/**
 * 参数校验器的配置类
 *
 * @author : Joe
 * @version V1.0
 * Date : 2018年09月03日 18:09
 */
@Configuration
public class ValidatorConfiguration {

    @Value("${validator.failFast:false}")
    private boolean failFast;

    @Bean(name = "JoeValidator")
    public ValidatorFactory validator() {
        return Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(failFast)
                .buildValidatorFactory();
    }
}
