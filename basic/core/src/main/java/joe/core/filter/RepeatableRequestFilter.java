package joe.core.filter;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * @author : Joe
 * @version V1.0


 * @note: 可重复使用数据包输入流
 * Date Date : 2018年09月03日 11:57
 */
@Order(Ordered.HIGHEST_PRECEDENCE + FilterOrder.REPEATABLE)
public class RepeatableRequestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        // 留空
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        final String servletPath = ((HttpServletRequest) request).getServletPath();
        if (servletPath.equals("/druid")) {
            chain.doFilter(request, response);
            return;
        }
        chain.doFilter(new RepeatableHttpServletRequestWrapper(httpServletRequest).init(), response);
    }

    @Override
    public void destroy() {
        // 留空
    }

    private static class RepeatableHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private byte[] byteIn;

        /**
         * Constructs a request object wrapping the given request.
         *
         * @param request 请求
         * @throws IllegalArgumentException if the request is null
         */
        RepeatableHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public BufferedReader getReader() {
            return new BufferedReader(new InputStreamReader(this.getInputStream()));
        }

        RepeatableHttpServletRequestWrapper init() throws IOException {
//            super.getRequest().getParameterNames();
            if (byteIn == null) {
                int contentLength = super.getRequest().getContentLength();
                contentLength = contentLength == -1 ? 0 : contentLength;
                byte[] bytes = new byte[contentLength];
                IOUtils.read(this.getRequest().getInputStream(), bytes, 0, contentLength);
                byteIn = bytes;
//                byteIn = ByteStreams.toByteArray(super.getInputStream());
            }
            return this;
        }

        public ServletInputStream getInputStream() {
            InputStream ins = new ByteArrayInputStream(byteIn);
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return true;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener readListener) {

                }

                @Override
                public int read() throws IOException {
                    return ins.read();
                }
            };
        }
    }

}
