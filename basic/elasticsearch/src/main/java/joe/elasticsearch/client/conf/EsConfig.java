package joe.elasticsearch.client.conf;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import joe.config.apollo.ApolloConfigListener;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ES 需要的一些配置
 *
 * @author : Joe
 * @version V1.0
 * Date : 2018年09月03日 16:54
 */
@Component
@EnableApolloConfig(EsConfig.NAMESPACE)
@ConfigurationProperties(prefix = EsConfig.APOLLO_PREFIX)
public class EsConfig implements ApolloConfigListener {
    final static String APOLLO_PREFIX = "elasticsearch";
    final static String NAMESPACE = "Business2.dcrm.elasticsearch";
    /**
     * 是否开启自动嗅探
     */
    private boolean sniff;
    /**
     * ES 集群名
     */
    private String clusterName;
    /**
     * ES 节点列表
     */
    private String clusterNodes;

    @ApolloConfigChangeListener(NAMESPACE)
    public void listener(ConfigChangeEvent changeEvent) {
        configurationPropertiesListener(changeEvent, APOLLO_PREFIX);
    }

    public boolean isSniff() {
        return sniff;
    }

    public EsConfig setSniff(boolean sniff) {
        this.sniff = sniff;
        return this;
    }

    public String getClusterName() {
        return clusterName;
    }

    public EsConfig setClusterName(String clusterName) {
        this.clusterName = clusterName;
        return this;
    }

    public String getClusterNodes() {
        return clusterNodes;
    }

    public EsConfig setClusterNodes(String clusterNodes) {
        this.clusterNodes = clusterNodes;
        return this;
    }
}
