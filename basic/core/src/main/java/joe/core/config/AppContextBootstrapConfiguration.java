package joe.core.config;

import joe.core.utils.AppUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppContextBootstrapConfiguration {

    @Bean
    public AppUtils initAppContext() {
        return new AppUtils();
    }
}