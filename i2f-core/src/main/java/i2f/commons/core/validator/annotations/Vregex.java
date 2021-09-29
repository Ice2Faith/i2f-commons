package i2f.commons.core.validator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用于字符串检查，给定正则表达式进行检测
 */
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Vregex {
    //验证字符串的正则表达式
    String value();
    //验证错误时异常的信息
    String exMsg() default "value not match regex";
    //默认不检查为null时
    boolean checkNull() default false;
}
