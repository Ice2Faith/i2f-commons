package i2f.commons.core.cache.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存模块，用于指定接口的缓存数据
 * 可以指定强比较进行缓存
 * 注意事项：
 * 对于参数或者返回值属于读一次类型的接口或者方法，请慎用（建议杜绝使用）
 * 例如：输入输出流
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RemoveCache {
    Class forCls(); //指定类名
    String forMethodRegex(); //指定方法名的正则
    String forMethodGenericStringRegex() default ""; //指定方法名的 Method.toGenericString()，优先级最高
}
