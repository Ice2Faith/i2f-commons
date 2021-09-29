package i2f.commons.component.netty.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ltb
 * @date 2021/8/18
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NettyRequestMapping {
    String value() default "";
    NettyHttpMethod[] methods() default {NettyHttpMethod.GET,NettyHttpMethod.PUT,NettyHttpMethod.POST,NettyHttpMethod.DELETE};
}
