package i2f.commons.core.validator.annotations;


import i2f.commons.core.validator.IProxyValidateHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用自定义验证类进行验证
 */
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Vproxy {
    //代理处理类
    Class<? extends IProxyValidateHandler> value();
    //验证错误时异常的信息
    String exMsg() default "value not pass proxy validate test";
    //附加自定义标志位
    int flag() default 0;
    //附加自定义串
    String tag() default "";
}
