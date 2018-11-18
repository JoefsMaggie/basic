package joe.database.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * @note: 数据源配置
 * Date : 2018年10月26日 12:03
 */
@Data
@Configuration
@ConfigurationProperties(prefix = DataSourceProperties.DB_PREFIX)
public class DataSourceProperties {
    static final String DB_PREFIX = "spring.datasource";
    /**
     * jdbc url
     */
    private String url;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * driver
     */
    private String driverClassName;
    /**
     * 初始连接数
     */
    private Integer initialSize;
    /**
     * 最小连接数
     */
    private Integer minIdle;
    /**
     * 最大连接数
     */
    private Integer maxActive;
    /**
     * 获取连接等待超时的时间
     */
    private Integer maxWait;
    private Integer timeBetweenEvictionRunsMillis;
    private Integer minEvictableIdleTimeMillis;
    private String validationQuery;
    private boolean testWhileIdle;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private boolean poolPreparedStatements;
    private Integer maxPoolPreparedStatementPerConnectionSize;
    private String filters;
    private String connectionProperties;
    private boolean asyncInit;

}
