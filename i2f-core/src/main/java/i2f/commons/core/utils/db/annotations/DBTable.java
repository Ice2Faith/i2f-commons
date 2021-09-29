package i2f.commons.core.utils.db.annotations;

import java.lang.annotation.*;

/**
 * 可以为一个类指定一个别名，以生成SQL语句，不指定则以类名作为表名
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DBTable {
    String schema() default "";
    String table() default "";
    boolean underScore() default false;
    String comment() default "";
}
