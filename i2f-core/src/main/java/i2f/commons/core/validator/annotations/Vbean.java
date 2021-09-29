package i2f.commons.core.validator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用于对象检查属性
 */
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Vbean {
    //验证错误时异常的信息，仅仅用于对象为null时
    String nullMsg() default "obj value cannot equals null";
    //默认不检查为null时
    boolean checkNull() default false;
}
