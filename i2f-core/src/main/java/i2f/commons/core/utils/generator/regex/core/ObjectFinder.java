package i2f.commons.core.utils.generator.regex.core;

import i2f.commons.core.utils.reflect.core.resolver.base.ClassResolver;
import i2f.commons.core.utils.reflect.core.resolver.base.ConvertResolver;
import i2f.commons.core.utils.reflect.core.resolver.base.MethodResolver;
import javafx.print.Collation;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author ltb
 * @date 2021/10/22
 */
public class ObjectFinder {
    /**
     * 根据Json的路由匹配模式，得到对象的值
     * 例如：
     * elem.args[2].value@java.lang.Integer.parseInt
     * 其他合法的例子：
     * elem
     * elem.args
     * elem[10]
     * elem.args[2]
     * elem.key@java.lang.Integer.instanceof
     * 这将会获得传入对象的elem属性 的args可迭代对象的第2个元素 的value属性 使用Integer类的parseInt对将value作为入参，其返回的结果作为最终结果
     * 特别的，当方法域(@符号之后最后一个.之后)为instanceof关键字时，表示使用构造方法构造对象
     * 其中当类名没有找到此类，将尝试查找java.lang包下的类，也没有的话将尝试java.utl包，否则视为找不到
     * @param obj
     * @param dotKey
     * @return
     */
    public static Object getObjectByDotKeyWithReference(Object obj, String dotKey){
        return getObjectByDotKeyWithReference(obj, dotKey,null);
    }
    public static Object getObjectByDotKeyWithReference(Object obj, String dotKey,List<String> baseRefPackages){
        String simpleDotKey=dotKey;
        String referenceDotKey=null;
        if(dotKey!=null){
            int idx=dotKey.indexOf("@");
            if(idx>=0){
                simpleDotKey=dotKey.substring(0,idx);
                referenceDotKey=dotKey.substring(idx+1);
            }
        }


        Object val=getObjectByDotKey(obj,simpleDotKey);
        val= ObjectFinder.referenceDotKeyConvert(val,referenceDotKey,baseRefPackages);
        return val;
    }

    /**
     * 根据Json的路由匹配模式，得到对象的值
     * 例如：
     * elem.args[2].value@java.lang.Integer.parseInt
     * 这将会获得传入对象的elem属性 的args可迭代对象的第2个元素 的value属性 作为最终结果
     * @param obj
     * @param dotKey
     * @return
     */
    public static Object getObjectByDotKey(Object obj, String dotKey){
        if(obj==null){
            return null;
        }
        if(dotKey==null || "".equals(dotKey)){
            return obj;
        }
        dotKey=dotKey.trim();
        String[] keys=dotKey.split("\\.",2);
        Class clazz=obj.getClass();
        if(keys.length==0){
            return obj;
        }
        String curKey=keys[0];
        String nextKey=null;
        if(keys.length>1){
            nextKey=keys[1];
        }
        if("null".equals(curKey)){
            return null;
        }
        if("true".equals(curKey)){
            return true;
        }
        if("false".equals(curKey)){
            return false;
        }
        if("class".equals(curKey)){
            return obj.getClass();
        }
        if(clazz.getName().startsWith("java.lang.")){
            return obj;
        }

        if(clazz.isArray()){
            if(curKey.matches("\\[.+\\]")){
                int idx=Integer.parseInt(curKey.substring(1,curKey.length()-1));
                Object nobj= Array.get(obj,idx);
                return getObjectByDotKeyWithReference(nobj,nextKey);
            }
        }
        else if(obj instanceof Map){
            Map map=(Map)obj;
            if(map.containsKey(curKey)){
                return getObjectByDotKeyWithReference(map.get(curKey),nextKey);
            }
        }
        else if(obj instanceof Collation){
            if(curKey.matches("\\[.+\\]")){
                int idx=Integer.parseInt(curKey.substring(1,curKey.length()-2));
                Object nobj=getCollectionIndexObj((Collection)obj,idx);
                return getObjectByDotKeyWithReference(nobj,nextKey);
            }
        }else{
            Field field=getClassFieldByName(clazz,curKey);
            if(field!=null){
                try{
                    field.setAccessible(true);
                    Object val=field.get(obj);
                    return getObjectByDotKeyWithReference(val,nextKey);
                }catch(Exception e){

                }
            }
        }
        if(curKey.matches(".+\\[.+\\]")){
            int fidx=curKey.indexOf("[");
            String pkey=curKey.substring(0,fidx);
            Object val= getObjectByDotKeyWithReference(obj,pkey);
            if(val!=null){
                return getObjectByDotKeyWithReference(val,curKey.substring(fidx));
            }
        }
        return null;
    }

    /**
     * 根据Json的路由匹配模式，得到对象的转换结果
     * 例如：
     * size
     * java.lang.Integer.parseInt
     * 类找不到，将依次尝试java.lang包，java.utl包，java.math包，java.time包，java.io包下的类名，最后以传入对象本身类兜底
     * 因此，
     * 这样也是可以的：
     * String.valueOf 隐含java.lang
     * Integer.parseInt
     * reflect.Array.copyOf 隐含java.util
     * reflect.Field.getDeclareAnnotations
     * 这将会获得传入对象的作为入参值，进行parseInt转换，结果作为最终结果
     * 也就意味着，这种方法是需要满足一定条件的
     * 1.必须应该有返回值，但是也可以没有
     * 2.最多有一个参数
     * 3.这个参数可以接受传入的对象类型，方法不会去管数据类型是否匹配
     * 未指明类名时，默认在传入对象本类查找方法
     * 指明类名时，从对应类名中查找方法
     * 特别的，当函数名为【instanceof】关键字时，意味着是使用构造函数
     * @param obj
     * @param dotKey
     * @return
     */
    public static Object referenceDotKeyConvert(Object obj,String dotKey){
        return referenceDotKeyConvert(obj, dotKey,null);
    }

    /**
     * 是上个函数的底层依赖，支持@方法指定包名前缀，以减少模板中全类名的书写
     * @param obj
     * @param dotKey
     * @param basePackages
     * @return
     */
    public static Object referenceDotKeyConvert(Object obj,String dotKey,List<String> basePackages){
        if(dotKey==null ||"".equals(dotKey)){
            return obj;
        }
        dotKey=dotKey.trim();
        if(dotKey==null || "".equals(dotKey)){
            return obj;
        }
        if(basePackages==null){
            basePackages=new ArrayList<>();
        }
        /**
         * 添加默认java的基本包
         */
        basePackages.add(0,"java.io");
        basePackages.add(0,"java.time");
        basePackages.add(0,"java.math");
        basePackages.add(0,"java.util");
        basePackages.add(0,"java.lang");

        String refClassName=null;
        String refMethodName=dotKey;
        int idx=dotKey.lastIndexOf(".");
        if(idx>=0){
            refClassName=dotKey.substring(0,idx);
            refMethodName=dotKey.substring(idx+1);
        }
        Class refClass=null;
        if(refClassName!=null && !"".equals(refClassName)){
            refClass= ClassResolver.getClazz(refClassName);
        }
        if(basePackages!=null){
            for(String item : basePackages){
                if(refClass!=null){
                    break;
                }
                String langRefCLass=item+"."+refClassName;
                refClass=ClassResolver.getClazz(langRefCLass);
            }
        }
        if(refClass==null){
            if(obj!=null) {
                refClass = obj.getClass();
            }
        }
        if("class".equals(refMethodName)){
            return getConvertObjectByClass(obj,refClass);
        }
        if("instanceof".equals(refMethodName)){
            return getConvertObjectByConstructor(obj, refClass);
        }
        return getConvertObjectByMethodName(obj, refClass, refMethodName);
    }

    public static Object getCollectionIndexObj(Collection col,int idx){
        if(idx<0){
            return null;
        }
        int i=0;
        Object ret=null;
        Iterator it=col.iterator();
        while(it.hasNext()){
            ret=it.next();
            if(i==idx){
                break;
            }
            i++;
        }
        if(i==idx){
            return ret;
        }
        return null;
    }

    public static Field getClassFieldByName(Class clazz,String fieldName){
        Set<Field> sets=new HashSet<>(48);
        Field[] declare=clazz.getDeclaredFields();
        Field[] fields=clazz.getFields();
        for(Field item : declare){
            sets.add(item);
        }
        for(Field item : fields){
            sets.add(item);
        }
        for(Field item : sets){
            String name=item.getName();
            if(name.equals(fieldName)){
                return item;
            }
        }
        return null;
    }

    public static Object getConvertObjectByClass(Object obj,Class refClass){
        if(obj!=null){
            return obj.getClass();
        }
        return refClass;
    }
    public static Object getConvertObjectByMethodName(Object obj, Class refClass, String refMethodName) {
        List<Method> methods= MethodResolver.getMethods(refClass,false, refMethodName);
        if(methods.size()==0){
            return obj;
        }
        List<Method> possibleMethod=new ArrayList<>();
        List<Method> matchMethod=new ArrayList<>();
        for(Method item : methods){
            int pcount=item.getParameterCount();
            Class pclazz=item.getReturnType();
            if(pcount>1){
                continue;
            }
            if(pcount==1){
                Class typeClass=item.getParameterTypes()[0];
                if(ConvertResolver.canConvert2(typeClass, obj)){
                    matchMethod.add(item);
                }
            }
            possibleMethod.add(item);
        }
        if(possibleMethod.size()==0){
            return obj;
        }

        boolean isMatch=true;
        Object retVal= obj;
        Method method= possibleMethod.get(0);
        Object ivkObj=obj;
        if(matchMethod.size()>0){
            isMatch=true;
            method= matchMethod.get(0);
            method.setAccessible(true);

            if(Modifier.isStatic(method.getModifiers())){
                ivkObj=null;
            }else{
                ivkObj= obj;
                Class objClass= obj.getClass();
                if(objClass.equals(refClass)){
                    ivkObj= obj;
                }else{
                    ivkObj=ClassResolver.instance(refClass);
                }
            }
        }else{
            isMatch=false;
            method= possibleMethod.get(0);
            method.setAccessible(true);

            if(Modifier.isStatic(method.getModifiers())){
                ivkObj=null;
            }else{
                ivkObj= obj;
                Class objClass= obj.getClass();
                if(objClass.equals(refClass)){
                    ivkObj= obj;
                }else{
                    ivkObj=ClassResolver.instance(refClass);
                }

            }
        }

        retVal=getConvertObjectByInvokeMethod(method,ivkObj,obj,isMatch);

        return retVal;
    }

    public static Object getConvertObjectByInvokeMethod(Method method, Object ivkObj, Object obj, boolean isMatch) {
        Object retVal=obj;
        boolean isVoidRet=void.class.equals(method.getReturnType());
        try{
            if(method.getParameterCount()>0){
                Object cvtObj= obj;
                if(isMatch){
                    Class typeClass= method.getParameterTypes()[0];
                    cvtObj=ConvertResolver.tryConvertType(typeClass, obj);
                }
                if(isVoidRet){
                    method.invoke(ivkObj,cvtObj);
                    retVal=cvtObj;
                }else{
                    retVal= method.invoke(ivkObj,cvtObj);
                }
            }else{
                if(isVoidRet){
                    method.invoke(ivkObj);
                    retVal= obj;
                }else{
                    retVal= method.invoke(ivkObj);
                }
            }
        }catch(Exception e){

        }
        return retVal;
    }


    public static Object getConvertObjectByConstructor(Object obj, Class refClass) {
        Set<Constructor> constructors= ClassResolver.getConstructors(refClass);
        List<Constructor> possibleConstructors=new ArrayList<>();
        List<Constructor> matchConstructors=new ArrayList<>();
        for(Constructor item : constructors){
            int pcount=item.getParameterCount();
            if(pcount>1){
                continue;
            }
            if(pcount==1){
                Class typeClass=item.getParameterTypes()[0];
                if(ConvertResolver.canConvert2(typeClass, obj)){
                    matchConstructors.add(item);
                }
            }
            possibleConstructors.add(item);
        }
        if(possibleConstructors.size()==0){
            return obj;
        }
        Object retVal= obj;
        boolean isMatch=false;
        Constructor cons= possibleConstructors.get(0);
        if(matchConstructors.size()>0){
            isMatch=true;
            cons= matchConstructors.get(0);
        }else{
            isMatch=false;
            cons= possibleConstructors.get(0);
        }
        retVal=getConvertObjectByConstructor(obj, cons, isMatch);

        return retVal;
    }

    public static Object getConvertObjectByConstructor(Object obj, Constructor cons, boolean isMatch) {
        Object retVal= obj;
        try{
            if(cons.getParameterCount()>0){
                Object cvtObj= obj;
                if(isMatch){
                    Class typeCLass= cons.getParameterTypes()[0];
                    cvtObj= ConvertResolver.tryConvertType(typeCLass, obj);
                }
                retVal= cons.newInstance(cvtObj);
            }else{
                retVal= cons.newInstance();
            }
        }catch(Exception e){

        }
        return retVal;
    }
}
