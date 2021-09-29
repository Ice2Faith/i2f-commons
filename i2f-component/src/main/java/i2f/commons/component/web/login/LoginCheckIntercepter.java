package i2f.commons.component.web.login;

import i2f.commons.component.web.login.annotations.RequireLogin;
import i2f.commons.component.web.login.impl.BaseLoginProcesser;
import i2f.commons.core.utils.reflect.ReflectUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class LoginCheckIntercepter implements HandlerInterceptor {
    protected ILoginProcesser processer = new BaseLoginProcesser();

    public LoginCheckIntercepter() {

    }

    public LoginCheckIntercepter(ILoginProcesser processer) {
        this.processer = processer;
    }

    public ILoginProcesser setLoginProsser(ILoginProcesser processer) {
        ILoginProcesser old = this.processer;
        this.processer = processer;
        return old;
    }

    @Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        beforeCheck(request, response);
        boolean allowDoNext=true;
        if (processer != null) {
            if (handler instanceof HandlerMethod) { // for controller
                HandlerMethod controllerHandler = (HandlerMethod) handler;
                Method method = controllerHandler.getMethod();
                RequireLogin mrl = method.getAnnotation(RequireLogin.class);
                boolean needCheck = false;
                if (mrl != null) {
                    needCheck = mrl.value();
                }else{
                    List<Annotation> annos = ReflectUtil.getAnnotations(method, RequireLogin.class);
                    for (Annotation item : annos) {
                        RequireLogin it = (RequireLogin) item;
                        if (it.value()) {
                            needCheck = true;
                            break;
                        }
                    }
                }
                if (needCheck) {
                    allowDoNext= processer.isLogined4Controller(request, response,method);
                }
            } else if (handler instanceof ResourceHttpRequestHandler) { // for static resources
                ResourceHttpRequestHandler resourceHandler = (ResourceHttpRequestHandler) handler;
                allowDoNext=processer.isLogined4StaticResources(request, response);
            }
            if(allowDoNext==false){
                processer.responseNotLogin(request, response);
            }
        }

        return allowDoNext;
    }

    protected void beforeCheck(HttpServletRequest request,HttpServletResponse response){
        try{
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
        }catch(Exception e){

        }
    }

    @Override
    public final void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public final void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
