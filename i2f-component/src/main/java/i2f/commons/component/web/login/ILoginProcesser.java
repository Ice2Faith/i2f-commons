package i2f.commons.component.web.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public interface ILoginProcesser {
    boolean isLogined4Controller(HttpServletRequest request, HttpServletResponse response, Method method);
    boolean isLogined4StaticResources(HttpServletRequest request, HttpServletResponse response);
    void responseNotLogin(HttpServletRequest request,HttpServletResponse response);
}
