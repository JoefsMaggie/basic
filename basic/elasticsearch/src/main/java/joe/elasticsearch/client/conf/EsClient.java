package joe.elasticsearch.client.conf;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Joe
 * @version V1.0


 * @note: ES TransportClient 生成
 * Date Date : 2018年09月03日 15:02
 */
@Configuration
public class EsClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(EsClient.class);

    /**
     * 配置类
     */
    private final EsConfig esConfig;

    @Autowired
    public EsClient(EsConfig esConfig) {
        this.esConfig = esConfig;
    }

    @Bean
    public TransportClient client() {
        // 设置集群的名字
        Settings settings = Settings.builder()
                                    .put("cluster.name", esConfig.getClusterName())
                                    .put("client.transport.sniff", esConfig.isSniff())
                                    .build();
        // 创建集群client并添加集群节点地址
        TransportClient client = new PreBuiltTransportClient(settings);
        Map<String, Integer> nodeMap = parseNodeIpInfo();
        for (Map.Entry<String, Integer> entry : nodeMap.entrySet()) {
            try {
                client.addTransportAddress(new TransportAddress(InetAddress.getByName(entry.getKey()), entry.getValue()));
            } catch (UnknownHostException e) {
                LOGGER.error("Elasticsearch address error", e);
            }
        }
        return client;
    }

    private Map<String, Integer> parseNodeIpInfo() {
        Map<String, Integer> nodeInfos = new HashMap<>();
        Arrays.stream(esConfig.getClusterNodes().split(","))
              .map(String::trim)
              .forEach(node -> {
                  String[] ipPort = node.split(":");
                  nodeInfos.put(ipPort[0], Integer.valueOf(ipPort[1]));
              });
        return nodeInfos;
    }
}
