package i2f.commons.core.validator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用于对象判空
 * 如果是String，再判断长度是否为0
 * 如果是Collection及其子类，判断其size是否为0
 * 如果是Map及其子类，判断其size是否为0
 * 如果是数组，判断其length是否为0
 */
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface VnullEmpty {
    //验证错误时异常的信息
    String exMsg() default "value cannot equals null or empty";
}
