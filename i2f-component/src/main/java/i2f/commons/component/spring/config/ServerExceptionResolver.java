package i2f.commons.component.spring.config;

import i2f.commons.component.json.gson.JsonUtil;
import i2f.commons.core.data.web.data.RespData;
import i2f.commons.core.exception.CommonsException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.sql.SQLIntegrityConstraintViolationException;

// use : extends this class and add @Configuration annotation
public class ServerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        ModelAndView mv=new ModelAndView();

        preOnProcess(mv,httpServletRequest,httpServletResponse,o,e);

        if(e instanceof NoHandlerFoundException){ //404 页面异常
            onRequestNotFound404(mv,httpServletRequest,httpServletResponse,o,(NoHandlerFoundException)e);
        }else if(e instanceof UndeclaredThrowableException){ //AOP 中的异常
            onAopException(mv,httpServletRequest,httpServletResponse,o,(Exception)((UndeclaredThrowableException)e).getUndeclaredThrowable());
        } else if(e instanceof CommonsException){ // 自定义的业务异常
            onServerException(mv,httpServletRequest,httpServletResponse,o,(CommonsException)e);
        }else if(e instanceof SQLIntegrityConstraintViolationException){ // SQL 键约束异常
            onSqlConstraintViolationException(mv,httpServletRequest,httpServletResponse,o,(SQLIntegrityConstraintViolationException)e);
        }else if(e instanceof RuntimeException){ // 自定义的业务异常
            onRuntimeException(mv,httpServletRequest,httpServletResponse,o,(RuntimeException)e);
        }else{  //其他异常
            onOtherExceptions(mv,httpServletRequest,httpServletResponse,o,e);
        }
        return mv;
    }


    protected void responseException(HttpServletResponse response, RespData data){
        try {
            response.setStatus(200);
            response.getWriter().write(JsonUtil.toJson(data));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    protected void preOnProcess(ModelAndView mv,HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e){
        System.out.println("---------ex:"+e.getMessage());
        System.out.println("---Global Exception:");
        e.printStackTrace();
    }

    protected void onRequestNotFound404(ModelAndView mv,HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, NoHandlerFoundException e){
        responseException(httpServletResponse,
                RespData.error("404,连接走丢了，请检查URL是否正确或联系管理员.",e));
    }

    protected void onAopException(ModelAndView mv,HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e){
        responseException(httpServletResponse,
                RespData.error("AOP异常:"+e.getMessage(), e));
    }

    protected void onServerException(ModelAndView mv,HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, CommonsException e){
        responseException(httpServletResponse,
                RespData.error("业务异常:"+e.getMessage(),
                        null));
    }

    protected void onSqlConstraintViolationException(ModelAndView mv,HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, SQLIntegrityConstraintViolationException e){
        responseException(httpServletResponse,
                RespData.error("SQL键约束异常:"+e.getMessage(), e));
    }

    protected void onRuntimeException(ModelAndView mv,HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, RuntimeException e){
        responseException(httpServletResponse,
                RespData.error("系统运行异常:"+e.getMessage(),
                        null));
    }


    protected void onOtherExceptions(ModelAndView mv,HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e)  {
        try{
            mv.setViewName("redirect:http://www.baidu.com/s?wd="+ java.net.URLEncoder.encode(e.getMessage(),"utf-8"));
            //mv.setViewName("redirect:https://so.csdn.net/so/search/s.do?q="+e.getMessage());
        }catch(Exception ex){
            System.out.println(e.getMessage());
        }

    }
}
