package i2f.commons.core.validator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于判断字符串
 * 参数不必都填写，按照需求填写即可
 * 参数检查优先级由高到低为：
 * tnlne nlne
 * checkNull
 * needTrim
 * notEmpty
 * lengthEqual
 * lengthMin        lengthMax
 * regexMatch       regexNotMatch
 * contains         notContains
 * startWith        notStartWith
 * endWith          notEndWith
 */
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Vstring {
    String contains() default "";
    String notContains() default "";

    String regexMatch() default "";
    String regexNotMatch() default "";

    String startWith() default "";
    String notStartWith() default "";

    String endWith() default "";
    String notEndWith() default "";

    int lengthEqual() default -1;
    int lengthMin() default -1;
    int lengthMax() default -1;
    //验证错误时异常的信息
    String exMsg() default "value not pass string validate test";
    //默认不检查为null时
    boolean checkNull() default false;
    boolean notEmpty() default false;
    boolean needTrim() default false;

    //检查不为空，trim之后不为空串，
    //优先级比上面三者高，此值为true时，上面三者也会被当做true
    //也就是覆盖上面三者
    boolean tnlne() default false;
    //检查不为空，不为空串，默认开启，同理覆盖
    boolean nlne() default true;
}
