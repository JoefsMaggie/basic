package joe.core.swagger2;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import joe.config.apollo.ApolloConfigListener;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0

 2
 * @note: swagger配置信息类
 * Date Date : 2018年09月14日 15:37
 */
@Component
@EnableApolloConfig(Swagger2Properties.NAMESPACE)
@ConfigurationProperties(Swagger2Properties.APOLLO_PREFIX)
public class Swagger2Properties implements ApolloConfigListener {
    final static String APOLLO_PREFIX = "api";
    final static String NAMESPACE = "Business2.dcrm.swagger";
    private boolean enableApi;
    private String path;
    private String version;
    private String title;
    private String description;
    private String name;
    private String url;
    private String email;
    private String serviceUrl;

    @ApolloConfigChangeListener(NAMESPACE)
    private void listener(ConfigChangeEvent changeEvent) {
        configurationPropertiesListener(changeEvent, APOLLO_PREFIX);
    }

    public boolean isEnableApi() {
        return enableApi;
    }

    public Swagger2Properties setEnableApi(boolean enableApi) {
        this.enableApi = enableApi;
        return this;
    }

    public String getPath() {
        return path;
    }

    public Swagger2Properties setPath(String path) {
        this.path = path;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public Swagger2Properties setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Swagger2Properties setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Swagger2Properties setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getName() {
        return name;
    }

    public Swagger2Properties setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Swagger2Properties setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Swagger2Properties setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public Swagger2Properties setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
        return this;
    }
}
