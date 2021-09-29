package i2f.commons.core.validator.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class BeanFieldItem {
    public Field field;
    public Annotation[] annotations;
    public Object value;

    public BeanFieldItem(){

    }

    public BeanFieldItem(Field field, Annotation[] annotations, Object value) {
        this.field = field;
        this.annotations = annotations;
        this.value = value;
    }
}
