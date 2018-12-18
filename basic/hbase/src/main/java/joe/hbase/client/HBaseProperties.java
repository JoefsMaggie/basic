package joe.hbase.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 *  hbase 配置信息类
 * Date Date : 2018年09月20日 9:24
 */
@Component
@ConfigurationProperties("hbase")
public class HBaseProperties {
    private Map<String, String> config;

    public Map<String, String> getConfig() {
        return config;
    }

    public HBaseProperties setConfig(Map<String, String> config) {
        this.config = config;
        return this;
    }
}
