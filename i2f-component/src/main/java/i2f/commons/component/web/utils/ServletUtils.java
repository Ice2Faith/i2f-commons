package i2f.commons.component.web.utils;

import i2f.commons.component.json.jackson.JsonUtil;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author ltb
 * @date 2021/8/31
 */
public class ServletUtils
{

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest()
    {
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取response
     */
    public static HttpServletResponse getResponse()
    {
        return getRequestAttributes().getResponse();
    }

    /**
     * 获取session
     */
    public static HttpSession getSession()
    {
        return getRequest().getSession();
    }

    public static RequestDispatcher getDispatcher(String target){
        return getRequest().getRequestDispatcher(target);
    }

    public static void forward(String target) throws ServletException, IOException {
        getDispatcher(target).forward(getRequest(),getResponse());
    }

    public static void include(String target) throws ServletException, IOException {
        getDispatcher(target).include(getRequest(),getResponse());
    }

    public static void redirect(String target) throws IOException {
        getResponse().sendRedirect(target);
    }

    public static void sessionSet(String key,Object val){
        getSession().setAttribute(key,val);
    }

    public static Object sessionGet(String key){
        return getSession().getAttribute(key);
    }

    public static ServletContext getServletContext(){
        return getSession().getServletContext();
    }

    public static void requestSet(String key,Object val){
        getRequest().setAttribute(key,val);
    }

    public static Object requestGet(String key){
        return getRequest().getAttribute(key);
    }

    public static String getContextRealPath(String path){
        return getServletContext().getRealPath(path);
    }

    public static ServletRequestAttributes getRequestAttributes()
    {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    public static void respJson(HttpServletResponse response, String string){
        respJson(response, string,200);
    }
    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string 待渲染的字符串
     * @return null
     */
    public static void respJson(HttpServletResponse response, String string,int status)
    {
        try
        {
            response.setStatus(status);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return ;
    }

    public static void respJson(String json){
        respJson(json,200);
    }
    public static void respJson(String json,int status){
        respJson(getResponse(),json,status);
    }

    public static void respJsonObj(Object obj){
        respJson(JsonUtil.toJson(obj),200);
    }

}
