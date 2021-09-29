package i2f.commons.core.utils.reflect.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//用于指定同Bean里面的某一个属性值作为当前注解的Bean字段的外键引用来源
//举例：

/**
 * private int userId;
 *
 * @FieldForeignKeyFor("userId") //这里就指定了外键引用了本Bean中的userId属性，这样，你可以根据自己的需求，进行一些开发
 * private User fkUser;
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldForeignKeyFor {
    String value() default "";
}
