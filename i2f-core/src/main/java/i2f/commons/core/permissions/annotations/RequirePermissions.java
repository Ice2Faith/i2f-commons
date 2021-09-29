package i2f.commons.core.permissions.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限控制模块，其中可以指定一个权限数组
 * 权限数组值可以在PermissionType接口中获取
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermissions {
    int[] ids() default {};
    String[] names() default {};
    Class tag() default Object.class;
}
