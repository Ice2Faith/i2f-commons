package i2f.commons.component.web.interceptors;

import i2f.commons.component.web.interceptors.annotations.RepeatReduce;
import i2f.commons.component.web.utils.ServletUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.concurrent.*;

/**
 * @author ltb
 * @date 2021/10/18
 */
public class RepeatableInterceptor implements HandlerInterceptor {
    private static class RepeatRequestItem implements Delayed {
        private String url;
        private long expire;
        public RepeatRequestItem(String url,long expire){
            this.url=url;
            this.expire=expire;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return expire-System.currentTimeMillis();
        }

        @Override
        public int compareTo(Delayed o) {
            RepeatRequestItem item = (RepeatRequestItem) o;
            long diff = this.expire - item.expire;
            if (diff <= 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }
    private ConcurrentHashMap<String,Object> urlMap=new ConcurrentHashMap<String,Object>();
    public DelayQueue<RepeatRequestItem> queue=new DelayQueue<RepeatRequestItem>();
    private ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);
    public RepeatableInterceptor(){
        pool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try{
                    RepeatRequestItem item=queue.take();
                    if(item!=null){
                        urlMap.remove(item.url);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        },0,30,TimeUnit.MILLISECONDS);
    }
    @Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) { // for controller
            HandlerMethod controllerHandler = (HandlerMethod) handler;
            Method method = controllerHandler.getMethod();
            RepeatReduce ann = method.getAnnotation(RepeatReduce.class);
            if (ann == null) {
                ann = method.getDeclaringClass().getAnnotation(RepeatReduce.class);
            }
            if (ann != null && ann.value()) {
                String url=request.getRequestURL().toString();
                String params=request.getQueryString();
                if(params!=null && !"".equals(params)){
                    url=url+"?"+params;
                }
                if(urlMap.containsKey(url)){
                    ServletUtils.respJson(response, "illegal repeat request checked!", 502);
                    return false;
                }
                RepeatRequestItem item=new RepeatRequestItem(url,ann.expire()+System.currentTimeMillis());
                queue.add(item);
            }
        } else if (handler instanceof ResourceHttpRequestHandler) { // for static resources

        }

        return true;
    }

    @Override
    public final void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public final void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
