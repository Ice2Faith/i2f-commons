package i2f.commons.core.utils.reflect.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//用于指定某个字段是否是主键字段
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldPrimaryKey {
    boolean value() default true;
    boolean isAuto() default false;
}
