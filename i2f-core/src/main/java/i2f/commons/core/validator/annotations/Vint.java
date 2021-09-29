package i2f.commons.core.validator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于判断整数的范围，可用于数字字符串
 * min和max不必都填写[闭区间]
 * 支持类型长度小于等于BigInteger类型的进行比较
 * 也就是Character,Byte,Short,Integer,Long,BigInteger都支持
 */
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Vint {
    String min() default "";
    String max() default "";
    //验证错误时异常的信息
    String exMsg() default "value isn't a int";
    //默认不检查为null时
    boolean checkNull() default false;
    //强制要求为数字格式，就算是字符串，为true时空串将被检查
    boolean forceNum() default false;
}
