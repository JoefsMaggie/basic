package joe.test.controllers.v1;

import joe.core.communicate.Response;
import joe.core.communicate.StringResponse;
import joe.core.resolver.annotation.Var;
import joe.test.apollo.ApolloConfiguration;
import joe.test.model.bean.request.TestBean;
import joe.test.services.TestService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0

 1
 *  测试用
 * Date : 2018年09月14日 16:47
 */
@Api(hidden = true)
@ApiIgnore
@RestController
public class TestController {

    private final static Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    private final TestService testService;
    private final ApolloConfiguration apolloConfiguration;

    @Value("${keyTwo:}")
    private String keyTwo;
    @Value("${test:}")
    private String test;

    @Autowired
    public TestController(TestService testService, ApolloConfiguration apolloConfiguration) {
        this.testService = testService;
        this.apolloConfiguration = apolloConfiguration;
    }

    @PostMapping("/apollo")
    public Response<StringResponse> apollo() {
        System.out.println(keyTwo);
        System.out.println(test);
        System.out.println(apolloConfiguration.getKeyOne());
        return Response.create("msg", String.format("Hello world! I'm %s!", apolloConfiguration.getKeyOne()));
    }

    @PostMapping("/name")
    public Response<StringResponse> hello(@NotBlank String name) {
        return Response.create("msg", String.format("Hello world! I'm %s!", name));
    }

    @PostMapping("/bean")
    public Response<TestBean> bean(@Var @Valid TestBean testBean, @NotBlank String aString) {
        LOGGER.info("bean method");
        LOGGER.info(aString);
        return Response.successData(testBean);
    }

    @PostMapping("/test")
    public Response<Object> test() {
        return Response.successData(testService.test());
    }

}
