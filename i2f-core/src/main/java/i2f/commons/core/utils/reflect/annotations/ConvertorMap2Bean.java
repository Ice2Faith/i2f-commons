package i2f.commons.core.utils.reflect.annotations;


import i2f.commons.core.utils.reflect.core.Map2BeanFieldConvertor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//用于自定义数据转换规则，暂时无效注解
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConvertorMap2Bean {
    Class<? extends Map2BeanFieldConvertor<?,?>> value();
}
