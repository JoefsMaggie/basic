package joe.database.conf;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * @Description : Druid 配置
 * Date: 2018/10/26
 */
@Configuration
public class DruidConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(DruidConfiguration.class);

    private final DruidProperties druidProperties;

    @Autowired
    public DruidConfiguration(DruidProperties druidProperties) {
        this.druidProperties = druidProperties;
    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        LOG.info("init Druid Servlet Configuration ");
        ServletRegistrationBean srb = new ServletRegistrationBean();
        srb.setServlet(new StatViewServlet());
        srb.addUrlMappings(druidProperties.getUrlMapping());
        // IP白名单
        srb.addInitParameter("allow", druidProperties.getAllow());
        // IP黑名单(共同存在时，deny优先于allow)
        srb.addInitParameter("deny", druidProperties.getDeny());
        // 控制台管理用户
        srb.addInitParameter("loginUsername", druidProperties.getLoginUsername());
        srb.addInitParameter("loginPassword", druidProperties.getLoginPassword());
        // 慢 sql 设置
        srb.addInitParameter("logSlowSql", druidProperties.getLogSlowSql());
        srb.addInitParameter("slowSqlMillis", druidProperties.getSlowSqlMillis());
        // 是否能够重置数据 禁用HTML页面上的“Reset All”功能
        srb.addInitParameter("resetEnable", druidProperties.getResetEnable());
        return srb;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns(druidProperties.getUrlPattern());
        filterRegistrationBean.addInitParameter("exclusions", druidProperties.getExclusions());
        return filterRegistrationBean;
    }
}
