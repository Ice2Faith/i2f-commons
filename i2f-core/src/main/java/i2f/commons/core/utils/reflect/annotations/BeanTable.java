package i2f.commons.core.utils.reflect.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//用于指定映射到数据库的数据库表名
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BeanTable {
    String value() default "";
}
