package i2f.commons.component.spring.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

// use : extends this class and add @Component annotation
public class SpringBaseUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        if(SpringBaseUtil.applicationContext == null){
            SpringBaseUtil.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name,clazz);
    }

    public static AutowireCapableBeanFactory getAutowireCapableBeanFactory(){
        return getApplicationContext().getAutowireCapableBeanFactory();
    }

    public static void makeAutowireSupport(Object existBean,int autowireMode,boolean dependencyCheck){
        getAutowireCapableBeanFactory().autowireBeanProperties(existBean,autowireMode,dependencyCheck);
    }

    public static void makeAutowireSupport(Object existBean){
        getAutowireCapableBeanFactory().autowireBeanProperties(existBean,AutowireCapableBeanFactory.AUTOWIRE_BY_NAME,false);
    }
}
