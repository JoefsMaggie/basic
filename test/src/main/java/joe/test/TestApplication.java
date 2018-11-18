package joe.test;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import joe.core.validation.annotation.EnableAutoValidate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * 测试工程启动类
 * Date : 2018年10月15日 11:04
 */
@EnableAutoValidate
@EnableApolloConfig()
@SpringBootApplication(scanBasePackages = "joe")
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
