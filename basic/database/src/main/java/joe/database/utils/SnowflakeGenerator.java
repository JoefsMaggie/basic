package joe.database.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * snowflake 算法生成 ID
 * Date: 2018/10/31
 */
@Component
@ConditionalOnMissingBean(IdGenerator.class)
public class SnowflakeGenerator implements IdGenerator {
    /**
     * 默认工作节点ID
     */
    private static final int DEFAULT_WORKER_ID = 1;
    /**
     * 工作节点ID
     */
    private int workerId = DEFAULT_WORKER_ID;
    /**
     * 根据配置设置数据中心ID，默认为1
     */
    @Value("${Joe.dc:1}")
    private int dataCenterId = 1;
    /**
     * Snowflake算法ID生成器
     */
    private SnowflakeFactory snowflakeFactory;

    public SnowflakeGenerator() {

    }

    public SnowflakeGenerator(int workerId, int dataCenterId) {
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
        initWorkerId();
    }

    @PostConstruct
    public void initWorkerId() {
        if (this.workerId == -1) {
            this.workerId = getWorkerId();
        }
        snowflakeFactory = new SnowflakeFactory(this.workerId, dataCenterId);
    }

    private int getWorkerId() {
        String localIp;
        InetAddress address;
        int workerId = 1;
        try {
            address = InetAddress.getLocalHost();
            localIp = address.getHostAddress(); // 获取本机ip
            String[] ips = localIp.split("\\.");
            String lastThreeNum = ips[ips.length - 1];
            workerId = Integer.parseInt(lastThreeNum);

            // 128=2^7, 因为workerId占用7bit,所以ip后三位要截取
            if (workerId >= 128) {
                workerId = workerId % 100;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return workerId;
    }

    @Override
    public long newId() {
        return snowflakeFactory.nextId();
    }
}
