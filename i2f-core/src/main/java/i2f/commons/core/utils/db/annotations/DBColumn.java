package i2f.commons.core.utils.db.annotations;

import java.lang.annotation.*;

/**
 * 用于给一个Java类注解，以方便将一个Java类对象，生成其对应的SQL语句
 * 包含：建表，CRUD语句
 * 这样你可以方便的修改一下语句即可使用
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBColumn{
    //列名
    String name() default "";
    //列类型
    String type() default "";
    //指定类型为text
    boolean text() default false;
    //列约束
    String restrict() default "";
    // 检查属性
    String check() default "";
    //不参与生成语句
    boolean ignore() default false;
    //属性为主键
    boolean primaryKey() default false;
    //外键约束
    String foreignKey() default "";
    //属性为默认值,值为默认值的数据
    String defaultRt() default "";
    //自动增长主键列，这个标识的，将不会出现在生成的insert语句中,但是其他生成语句中依然存在
    boolean autoIncrement() default false;
    //自动增长的主键的开始值
    long autoIncrementBeginNumber() default -1;
    //唯一约束
    boolean unique() default false;
    //非空约束
    boolean notNull() default false;
    //备注
    String comment() default "";
    // 转为下划线
    boolean underScore() default false;
    //生成表排序
    int order() default Integer.MAX_VALUE;
}
