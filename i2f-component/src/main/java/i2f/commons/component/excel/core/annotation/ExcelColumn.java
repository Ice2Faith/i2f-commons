package i2f.commons.component.excel.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ltb
 * @date 2021/9/17
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
    String name();
    int order() default Integer.MAX_VALUE;
    String dateFmt() default "yyyy-MM-dd HH:mm:ss";
    String dict() default "";
    String separator() default ",";
    int width() default 16;
    int height() default 14;
    String prefix() default "";
    String suffix() default "";
    String defaultVal() default "";
    Align align() default Align.AUTO;

    public enum Align {
        AUTO(0), LEFT(1), CENTER(2), RIGHT(3);
        private final int value;

        Align(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }

    ColumnType type() default ColumnType.STRING;

    public enum ColumnType {
        NUMERIC(0), STRING(1),DATE(2),DICT(3), IMAGE(12);
        private final int value;

        ColumnType(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }
}
