package joe.tags;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableApolloConfig
@SpringBootApplication(scanBasePackages = "joe")
public class TagsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TagsApplication.class, args);
    }
}
