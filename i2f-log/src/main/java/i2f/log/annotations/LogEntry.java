package i2f.log.annotations;

import i2f.log.enums.InvocationRecord;
import i2f.log.enums.InvocationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ltb
 * @date 2022/3/2 14:56
 * @desc
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogEntry {
    boolean value() default true;
    String system() default "";
    String module() default "";
    String label() default "";
    InvocationType[] entries() default {InvocationType.EXCEPTION};
    InvocationRecord[] records() default {InvocationRecord.EXCEPTION};
}
