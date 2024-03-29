package i2f.commons.core.utils.reflect.simple.reflect.impl;


import i2f.commons.core.utils.reflect.simple.reflect.PropertyAccessor;
import i2f.commons.core.utils.reflect.simple.reflect.convert.ConvertResolver;
import i2f.commons.core.utils.reflect.simple.reflect.exception.FieldAccessException;

import java.lang.reflect.Field;

/**
 * @author ltb
 * @date 2022/3/14 9:28
 * @desc
 */
public class FieldValueAccessor implements PropertyAccessor {
    public Field field;
    public Object ivkObj;
    public FieldValueAccessor(Field field){
        this.field=field;
    }
    public FieldValueAccessor(Field field, Object obj) {
        this.field = field;
        this.ivkObj =obj;
    }
    @Override
    public void setInvokeObject(Object ivkObj){
        this.ivkObj=ivkObj;
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public boolean writable() {
        return true;
    }

    @Override
    public boolean readable() {
        return true;
    }

    @Override
    public Class getType() {
        return field.getType();
    }

    @Override
    public Object get() {
        try{
            field.setAccessible(true);
            return field.get(ivkObj);
        }catch(Exception e){
            throw new FieldAccessException("field["+field.getName()+"] access error:"+e.getMessage());
        }
    }

    @Override
    public void set(Object obj) {
        try{
            field.setAccessible(true);
            Object sval=obj;
            if(obj!=null){
                Class srcType=obj.getClass();
                Class dstType=field.getType();
                if(!srcType.equals(dstType)){
                    if(ConvertResolver.isValueConvertible(obj,dstType)){
                        sval=ConvertResolver.tryConvertible(obj,dstType);
                    }
                }
            }
            field.set(ivkObj,sval);
        }catch(Exception e){
            throw new FieldAccessException("field["+field.getName()+"] access error:"+e.getMessage());
        }
    }

    public Field getField(){
        return field;
    }
    public Object getInvokeObject(){
        return ivkObj;
    }
}
