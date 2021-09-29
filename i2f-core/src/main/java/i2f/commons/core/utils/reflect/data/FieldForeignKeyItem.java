package i2f.commons.core.utils.reflect.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

@Data
@NoArgsConstructor
public class FieldForeignKeyItem<T> {
    public T obj;
    public Field annFkField;
    public Field forField;
    public Object forFieldValue;

    public FieldForeignKeyItem(T obj, Field annFkField, Field forField, Object forFieldValue) {
        this.obj = obj;
        this.annFkField = annFkField;
        this.forField = forField;
        this.forFieldValue = forFieldValue;
    }
}
