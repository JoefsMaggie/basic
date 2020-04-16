package joe.core.swagger2;

import joe.core.filter.FilterOrder;
import org.apache.catalina.connector.RequestFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * 通过修改 uri 的方式解决 swagger 不能测试问题
 *
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * Date : 2018年09月29日 09:40
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + FilterOrder.REPEATABLE - 1)
@ConditionalOnProperty(name = "swagger.enable", havingValue = "true")
public class SwaggerRequestFilter implements Filter {

    private static final String V2_API_DOCS = "/v2/api-docs";
    private static final String SWAGGER_UI_HTML = "swagger-ui.html";
    @Value("${server.servlet.context-path}")
    private String hostPath;
    private String swaggerHost;

    @PostConstruct
    public void host() {
        swaggerHost = hostPath + V2_API_DOCS;
    }

    @Override
    public void init(FilterConfig filterConfig) {
        // empty
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestURI = ((RequestFacade) request).getRequestURI();
        if (StringUtils.equals(requestURI, swaggerHost)
                || StringUtils.contains(requestURI, SWAGGER_UI_HTML)
                || !StringUtils.contains(requestURI, V2_API_DOCS)) {
            chain.doFilter(request, response);
            return;
        }
        chain.doFilter(new HttpServletRequestWrapper((HttpServletRequest) request) {
            @Override
            public String getRequestURI() {
                return requestURI.replace(V2_API_DOCS, "");
            }

            @Override
            public String getServletPath() {
                return ((RequestFacade) request).getServletPath().replace(V2_API_DOCS, "");
            }
        }, response);
    }

    @Override
    public void destroy() {
        // empty
    }
}
