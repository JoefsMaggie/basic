package joe.database.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * druid 配置属性类
 *
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * Date: 2018/10/31
 */
@Data
@ConfigurationProperties(prefix = DruidProperties.DRUID_PREFIX)
public class DruidProperties {
    static final String DRUID_PREFIX = "db.druid";
    /**
     *
     */
    private String urlMapping = "/druid/*";
    /**
     * IP 白名单
     */
    private String allow = "";
    /**
     * IP 黑名单
     */
    private String deny = "";
    /**
     * 登录账号
     */
    private String loginUsername = "admin";
    /**
     * 登录密码
     */
    private String loginPassword = "admin";
    /**
     * 是否记录慢 sql
     */
    private String logSlowSql = "true";
    /**
     * 慢 sql 时间设置 毫秒
     */
    private String slowSqlMillis = "3000";
    /**
     * 是否启用充值功能
     */
    private String resetEnable = "true";
    /**
     *
     */
    private String urlPattern = "/*";
    /**
     * 忽略
     */
    private String exclusions = "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*";

}
