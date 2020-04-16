package joe.core.swagger2;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * swagger配置信息类
 *
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * Date : 2018年09月14日 15:37
 */
@Data
@ConfigurationProperties(Swagger2Properties.SWAGGER_PREFIX)
public class Swagger2Properties {
    final static String SWAGGER_PREFIX = "swagger";
    private boolean enable;
    private String path;
    private String version;
    private String title;
    private String description;
    private String name;
    private String url;
    private String email;
    private String serviceUrl;
}
