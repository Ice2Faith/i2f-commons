package i2f.commons.core.utils.reflect.simple.reflect.impl;


import i2f.commons.core.utils.reflect.simple.reflect.PropertyAccessor;
import i2f.commons.core.utils.reflect.simple.reflect.convert.ConvertResolver;
import i2f.commons.core.utils.reflect.simple.reflect.exception.FieldAccessException;

import java.lang.reflect.Method;

/**
 * @author ltb
 * @date 2022/3/14 9:28
 * @desc
 */
public class MethodValueAccessor implements PropertyAccessor {
    public Method getter;
    public Method setter;
    public Object ivkObj;
    public String name;
    public Class type;
    public MethodValueAccessor(Method getter,Method setter,String name,Class type){
        this.getter = getter;
        this.setter = setter;
        this.name=name;
        this.type=type;
    }
    public MethodValueAccessor(Method getter,Method setter, Object obj) {
        this.getter = getter;
        this.setter = setter;
        this.ivkObj =obj;
    }

    @Override
    public void setInvokeObject(Object ivkObj) {
        this.ivkObj=ivkObj;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean writable() {
        return setter!=null;
    }

    @Override
    public boolean readable() {
        return getter!=null;
    }

    @Override
    public Class getType() {
        return type;
    }

    @Override
    public Object get() {
        try{
            getter.setAccessible(true);
            return getter.invoke(ivkObj);
        }catch(Exception e){
            throw new FieldAccessException("method["+getter.getName()+"] access error:"+e.getMessage());
        }
    }

    @Override
    public void set(Object obj) {
        try{
            setter.setAccessible(true);
            Object sval=obj;
            if(obj!=null){
                Class srcType=obj.getClass();
                Class dstType=setter.getParameterTypes()[0];
                if(!srcType.equals(dstType)){
                    if(ConvertResolver.isValueConvertible(obj,dstType)){
                        sval=ConvertResolver.tryConvertible(obj,dstType);
                    }
                }
            }
            setter.invoke(ivkObj,sval);
        }catch(Exception e){
            throw new FieldAccessException("method["+setter.getName()+"] access error:"+e.getMessage());
        }
    }

    public Method getGetter(){
        return getter;
    }

    public Method getSetter(){
        return setter;
    }

    public Object getInvokeObject(){
        return ivkObj;
    }
}

