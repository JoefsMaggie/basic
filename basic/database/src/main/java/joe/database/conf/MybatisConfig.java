package joe.database.conf;

import joe.database.mybatis.interceptor.MyInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * mybatis 配置
 *
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * Date: 2018/10/29
 */
@Configuration
// 加入 spring 事物支持
@EnableTransactionManagement
@PropertySource(value = {"classpath:application.properties", "classpath:application-*.properties",
        "classpath:application.yml", "classpath:application-*.yml"}, ignoreResourceNotFound = true)
// 配置扫描 mapper 路径
@MapperScan(basePackages = "joe.*.*.mapper", sqlSessionFactoryRef = "sqlSessionFactory")
public class MybatisConfig {
    private Logger LOG = LoggerFactory.getLogger(MybatisConfig.class);

    private final DataSource dataSource;

    @Autowired
    public MybatisConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Value("${db.underscore-to-camel:true}")
    private boolean mapUnderscoreTOCamelCase;

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(mapUnderscoreTOCamelCase);
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setConfiguration(configuration);
        bean.setTypeAliasesPackage("joe");
        // 分页插件
        /*PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");
        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);*/

        MyInterceptor myInterceptor = new MyInterceptor();
        // 添加插件
        bean.setPlugins(new Interceptor[]{myInterceptor});

        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            // 扫描 Mapper.xml 文件
            bean.setMapperLocations(resolver.getResources("classpath:**/mapping/*Mapper.xml"));
            return bean.getObject();
        } catch (Exception e) {
            LOG.error("sqlSessionFactory initialization resources error", e);
            throw new RuntimeException(e);
        }
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
