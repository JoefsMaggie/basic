package joe.database.conf;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.sql.SQLException;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * @Description : mybatis 数据源
 * Date: 2018/10/29
 */
@Configuration
public class DataSourceConfig {
    private Logger LOG = LoggerFactory.getLogger(DataSourceConfig.class);

    private final DataSourceProperties dataSourceProperties;

    @Autowired
    public DataSourceConfig(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    /**
     * 声明其为Bean实例
     */
    @Primary
    @Bean(destroyMethod = "close")
    public DruidDataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(dataSourceProperties.getUrl());
        datasource.setUsername(dataSourceProperties.getUsername());
        datasource.setPassword(dataSourceProperties.getPassword());
        datasource.setDriverClassName(dataSourceProperties.getDriverClassName());
        // configuration
        datasource.setInitialSize(dataSourceProperties.getInitialSize());
        datasource.setMinIdle(dataSourceProperties.getMinIdle());
        datasource.setMaxActive(dataSourceProperties.getMaxActive());
        datasource.setMaxWait(dataSourceProperties.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(dataSourceProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(dataSourceProperties.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(dataSourceProperties.getValidationQuery());
        datasource.setTestWhileIdle(dataSourceProperties.isTestWhileIdle());
        datasource.setTestOnBorrow(dataSourceProperties.isTestOnBorrow());
        datasource.setTestOnReturn(dataSourceProperties.isTestOnReturn());
        datasource.setPoolPreparedStatements(dataSourceProperties.isPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(dataSourceProperties.getMaxPoolPreparedStatementPerConnectionSize());
        datasource.setConnectionProperties(dataSourceProperties.getConnectionProperties());
        datasource.setAsyncInit(dataSourceProperties.isAsyncInit());
        try {
            datasource.setFilters(dataSourceProperties.getFilters());
        } catch (SQLException e) {
            LOG.error("data source initialization error: " + e);
            throw new RuntimeException("data source initialization error:", e);
        }
        return datasource;
    }
}
