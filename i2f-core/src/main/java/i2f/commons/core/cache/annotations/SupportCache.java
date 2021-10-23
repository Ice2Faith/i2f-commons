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
public @interface SupportCache {
    boolean open() default true; //是否开启缓存
    long expire() default 15*1000; //缓存的有效时间--毫秒
    boolean ignoreArgs() default false; //是否需要匹配参数，参数一致时才满足缓存条件
    boolean ignoreObj() default false; //是否需要匹配调用的对象，调用对象一致时才满足缓存条件
    RemoveCache[] rmCaches() default {}; //调用到目标方法将会引发那些缓存失效，前提open()=true
}
