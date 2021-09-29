package i2f.commons.core.utils.reflect.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//用于指定映射到数据库的表的字段名
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldColumn {
    String value() default "";
    boolean underScore() default true;
}
