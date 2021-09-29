package i2f.commons.component.netty.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ltb
 * @date 2021/8/18
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NettyParam {
    String value() ;
    boolean ignoreCase() default false;
    NettyParamSource[] source() default {NettyParamSource.URL,NettyParamSource.BODY};
    String dateFmt() default "yyyy-MM-dd HH:mm:ss";
}
