package i2f.commons.component.spring.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Enumeration;

/**
 * @author ltb
 * @date 2021/8/11
 */
public class SpringMvcUtil {

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public static Cookie[] getCookie(){
        return getRequest().getCookies();
    }

    public static Enumeration<String> getHeaders(String key){
        return getRequest().getHeaders(key);
    }

    public static HttpSession getSession(){
        return getRequest().getSession();
    }

    public static ServletContext getServletContext(){
        return getSession().getServletContext();
    }

    public static File getContextRealPath(String subPath){
        String path=getServletContext().getRealPath(subPath);
        return new File(path);
    }
}
