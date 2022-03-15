package i2f.commons.core.utils.reflect.simple.reflect;


import i2f.commons.core.utils.reflect.simple.reflect.core.FastReflect;
import i2f.commons.core.utils.reflect.simple.reflect.exception.FieldAccessException;
import i2f.commons.core.utils.reflect.simple.reflect.exception.FieldNotFoundException;
import i2f.commons.core.utils.reflect.simple.reflect.exception.MethodNotFoundException;
import i2f.commons.core.utils.reflect.simple.reflect.impl.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author ltb
 * @date 2022/3/14 10:17
 * @desc
 */
public class ValueVisitor {

    /**
     * 根据表达式获取值
     * @param obj Object|Map...
     * @param routePatten obj.arr[2].key | obj.set.map[zhang]
     * @return
     */
    public static ValueAccessor visit(Object obj, String routePatten){
        if(obj==null && routePatten==null){
            return new NullValueAccessor();
        }
        if(routePatten==null){
            return new ReadonlyValueAccessor(obj);
        }
        routePatten=routePatten.trim();
        if("".equals(routePatten)){
            return new ReadonlyValueAccessor(obj);
        }
        if(obj==null){
            throw new FieldNotFoundException("null object could not found field of {"+routePatten+"}");
        }

        String[] pattens=routePatten.split("\\.",2);
        String patten=pattens[0];
        ValueAccessor stepObj=new ReadonlyValueAccessor(obj);
        if(patten.contains("[")){
            int idx=patten.indexOf("[");
            String quoteExpress=patten.substring(idx);
            quoteExpress=quoteExpress.substring(1,quoteExpress.length()-1);
            if(idx>0){
                String stepPatten=patten.substring(0,idx);
                stepObj=visitOnly(obj,stepPatten);
                stepObj=visitOnly(stepObj.get(),quoteExpress);
            }else{
                stepObj=visitOnly(obj,quoteExpress);
            }
        }else{
            stepObj=visitOnly(obj,patten);
        }
        if(pattens.length>1){
            return visit(stepObj.get(),pattens[1]);
        }
        return stepObj;
    }

    /**
     * 仅获取一级属性
     * @param obj Object|Map...
     * @param express map | 2 | arr | zhang
     * @return
     */
    public static ValueAccessor visitOnly(Object obj,String express){
        if(express==null || obj==null){
            return null;
        }
        express=express.trim();
        if("".equals(express)){
            return new ReadonlyValueAccessor(obj);
        }
        Class clazz=obj.getClass();
        if(express.matches("^\\d+$")){
            int idx=Integer.parseInt(express);
            if(clazz.isArray()){
                if(idx>= Array.getLength(obj)){
                    return null;
                }
                return new ArrayValueAccessor(obj,idx);
            }else if(obj instanceof Collection){
                Collection col=(Collection)obj;
                int ci=0;
                Iterator<Object> it=col.iterator();
                while(it.hasNext()){
                    Object val=it.next();
                    if(ci==idx){
                        if(obj instanceof List){
                            List list=(List)obj;
                            return new ListValueAccessor(list,ci);
                        }else{
                            return new ReadonlyValueAccessor(val);
                        }
                    }
                    ci++;
                }
                return null;
            }
        }else{
            String key=express;
            if(obj instanceof Map){
                Map map=(Map)obj;
                return new MapValueAccessor(map,key);
            }else{
                return visitOnlyObj(obj,key);
            }
        }
        return null;
    }

    /**
     * 从对象获取值
     * @param obj Object
     * @param express  map | arr | zhang
     * @return
     */
    public static ValueAccessor visitOnlyObj(Object obj, String express){
        if(express==null || obj==null){
            return null;
        }
        express=express.trim();
        if("".equals(express)){
            return new ReadonlyValueAccessor(obj);
        }
        if(express.contains("[")){
            return visit(obj,express);
        }
        if(express.matches("^\\d+$")){
            return visitOnly(obj,express);
        }
        try{
            return getOnlyObjByMethod(obj,express);
        }catch(MethodNotFoundException | FieldNotFoundException e){
            return getOnlyObjByField(obj,express);
        }
    }



    /**
     * 使用getter方式从对象中获取值
     * @param obj Object
     * @param express user | name
     * @return
     */
    public static ValueAccessor getOnlyObjByMethod(Object obj, String express){
        Class clazz=obj.getClass();
        Set<Method> setters= FastReflect.findSetters(clazz,express);
        Set<Method> getters= FastReflect.findGetters(clazz,express);

        if(getters.size()>0){
            Iterator<Method> it=getters.iterator();
            while(it.hasNext()){
                Method method=it.next();
                try{
                    method.setAccessible(true);
                    Iterator<Method> sit=setters.iterator();
                    Method setter=null;
                    if(sit.hasNext()){
                        setter=sit.next();
                    }
                    return new MethodValueAccessor(method,setter,obj);
                }catch(Exception e){

                }
            }
        }else if(setters.size()>0){
            Iterator<Method> it=setters.iterator();
            while(it.hasNext()){
                Method method=it.next();
                try{
                    method.setAccessible(true);
                    Iterator<Method> sit=getters.iterator();
                    Method getter=null;
                    if(sit.hasNext()){
                        getter=sit.next();
                    }
                    return new MethodValueAccessor(getter,method,obj);
                }catch(Exception e){

                }
            }
        }
        throw new MethodNotFoundException("not found getter method for field["+express+"] on class{"+clazz.getName()+"}");
    }


    /**
     * 使用直接访问方式从对象中获取值
     * @param obj Object
     * @param express name | age
     * @return
     */
    public static ValueAccessor getOnlyObjByField(Object obj,String express){
        Class clazz=obj.getClass();
        Field item= FastReflect.findField(clazz,express);
        if(item!=null){
            try{
                return new FieldValueAccessor(item,obj);
            }catch(Exception e){
                throw new FieldAccessException("query object field["+express+"] on class{"+clazz.getName()+"} value error:"+e.getMessage());
            }
        }
        throw new FieldNotFoundException("not field found in class{"+clazz.getName()+"}");
    }
}
