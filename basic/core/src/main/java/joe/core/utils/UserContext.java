package joe.core.utils;

import com.fasterxml.jackson.databind.JsonNode;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 *  获取请求报文，结合filter来解决请求报文不能重复读取的问题
 * Date Date : 2018年09月04日 13:46
 */
public class UserContext {

    private final HttpServletRequest request;
    private static final String USER_CONTEXT = UserContext.class.getName() + "." + "USER_CONTEXT";

    private JsonNode bodyJson;

    public UserContext(HttpServletRequest request) {
        this.request = request;
        try {
            this.bodyJson = Jackson.fromJson(request.getInputStream(), JsonNode.class);
        } catch (IOException e) {
            throw new IllegalStateException("读取HTTP输入流失败, 没有开启Repeatable Filter?", e);
        }
    }

    /**
     * 获取当前用户上下文
     */
    public static UserContext currentUserContext() {
        HttpServletRequest request = RequestUtils.getRequest();
        Object userContext = request.getAttribute(USER_CONTEXT);
        if (userContext != null) {
            return (UserContext) userContext;
        }
        userContext = new UserContext(request);
        request.setAttribute(USER_CONTEXT, userContext);
        return (UserContext) userContext;
    }

    public HttpServletRequest currentRequest() {
        return this.request;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public String getUserId() {
        return this.request.getParameter("userId");
    }

    public String getKey() {
        return this.request.getParameter("key");
    }


    public String getRemoteAddr() {
        return this.request.getRemoteAddr();
    }

    public JsonNode jsonBody() {
        return bodyJson;
    }
}