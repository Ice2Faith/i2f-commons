package i2f.commons.component.web.login.impl;

import i2f.commons.component.web.login.ILoginProcesser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class BaseLoginProcesser implements ILoginProcesser {
    @Override
    public boolean isLogined4Controller(HttpServletRequest request, HttpServletResponse response, Method method) {
        return true;
    }

    @Override
    public boolean isLogined4StaticResources(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

    @Override
    public void responseNotLogin(HttpServletRequest request, HttpServletResponse response) {
        try{
            response.reset();
            response.setStatus(401);
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("<script>alert('Not Login,Please login.');</script>");
        }catch(Exception e){

        }
    }

}
