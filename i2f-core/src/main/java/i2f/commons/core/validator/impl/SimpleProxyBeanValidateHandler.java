package i2f.commons.core.validator.impl;


import i2f.commons.core.validator.IProxyBeanValidateHandler;
import i2f.commons.core.validator.annotations.VproxyBean;
import i2f.commons.core.validator.core.ValidateWorker;
import i2f.commons.core.validator.exception.ValidateException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;


public abstract class SimpleProxyBeanValidateHandler implements IProxyBeanValidateHandler {
    protected VproxyBean validator;
    protected Object bean;
    protected Class clazz;
    protected ArrayList<BeanFieldItem> fieldItems=new ArrayList<>();
    private void parseBeanFieldsInfo(){
        fieldItems.clear();
        if(bean==null){
            return;
        }
        clazz=bean.getClass();
        Field[] fields = clazz.getFields();
        if (fields == null || fields.length == 0) {
            return;
        }
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Annotation[] annotations=field.getDeclaredAnnotations();
            annotations= ValidateWorker.getSupportValidateAnnotations(annotations);
            Object value=null;
            try {
                field.setAccessible(true);
                value = field.get(bean);
            } catch (IllegalAccessException | SecurityException e) {

            }
            fieldItems.add(new BeanFieldItem(field,annotations,value));
        }
    }
    @Override
    public void test(VproxyBean validator, Object bean) throws ValidateException {
        this.validator=validator;
        this.bean=bean;
        parseBeanFieldsInfo();

    }

}
