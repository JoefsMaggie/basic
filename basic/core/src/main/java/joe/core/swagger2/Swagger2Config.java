package joe.core.swagger2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger配置生成
 *
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * Date : 2018年09月14日 11:42
 */
@EnableSwagger2
@Configuration
@EnableConfigurationProperties(Swagger2Properties.class)
@ConditionalOnProperty(name = "swagger.enable", havingValue = "true")
public class Swagger2Config {

    private final Swagger2Properties swagger2Properties;

    @Autowired
    public Swagger2Config(Swagger2Properties swagger2Properties) {
        this.swagger2Properties = swagger2Properties;
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                .enable(swagger2Properties.isEnable())
                .select()
                .apis(RequestHandlerSelectors.basePackage(swagger2Properties.getPath()))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/");
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact(swagger2Properties.getName(),
                swagger2Properties.getUrl(),
                swagger2Properties.getEmail());
        return new ApiInfoBuilder()
                .version(swagger2Properties.getVersion())
                .title(swagger2Properties.getTitle())
                .description(swagger2Properties.getDescription())
                .termsOfServiceUrl(swagger2Properties.getServiceUrl())
                .contact(contact)
                .build();
    }
}
