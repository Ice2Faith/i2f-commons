package i2f.commons.component.web.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ltb
 * @date 2021/9/2
 */
public class ServerUtils {
    /**
     * 获取完整的请求路径，包括：域名，端口，上下文访问路径
     *
     * 举例：http://127.0.0.1:8080
     * 如果是部署在Tomcat中，指定了上下文：test
     * 那么：
     * http://127.0.0.1:8080/test
     * @return 服务地址
     */
    public String getUrl()
    {
        HttpServletRequest request = ServletUtils.getRequest();
        return getDomain(request);
    }

    public static String getDomain(HttpServletRequest request)
    {
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString();
    }
}
