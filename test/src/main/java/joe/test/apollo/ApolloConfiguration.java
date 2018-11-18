package joe.test.apollo;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import joe.config.apollo.ApolloConfigListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0


 * 测试监听
 * Date : 2018年10月15日 12:02
 */
@Component
@EnableApolloConfig(ApolloConfiguration.NAMESPACE)
@ConfigurationProperties(prefix = ApolloConfiguration.APOLLO_PREFIX)
public class ApolloConfiguration implements ApolloConfigListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(ApolloConfiguration.class);

    final static String NAMESPACE = "Business2.test2-public";
    final static String APOLLO_PREFIX = "apollo.test";
    private String keyOne;

    @ApolloConfigChangeListener(NAMESPACE)
    private void listener(ConfigChangeEvent changeEvent) {
        configurationPropertiesListener(changeEvent, APOLLO_PREFIX);
    }

    public String getKeyOne() {
        return keyOne;
    }

    public ApolloConfiguration setKeyOne(String keyOne) {
        this.keyOne = keyOne;
        return this;
    }
}
