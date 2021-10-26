package i2f.commons.core.utils.generator.simple;

import javafx.print.Collation;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author ltb
 * @date 2021/9/9
 */
public class SimpleGenerator {

    /**
     * 根据模板，使用参数渲染出结果
     * 参数：${}
     * @param templateStr
     * @param params
     * @return
     */
    public static String render(String templateStr, Map<String,Object> params){
        int len=templateStr.length();
        char[] template=templateStr.toCharArray();
        StringBuilder builder=new StringBuilder(len);
        StringBuilder argBuilder=new StringBuilder(32);
        StringBuilder ctrlBuilder = new StringBuilder(32);
        int idx=0;
        while(idx<len){
            char ch=template[idx];
            if(ch=='$'){
                if(idx+1<len){
                    char nch=template[idx+1];
                    if(nch=='{'){
                        argBuilder.setLength(0);
                        int i=2;
                        while(i+idx<len){
                            char pch=template[i+idx];
                            if(pch=='}'){
                                break;
                            }
                            if(pch>='a' && pch<='z'
                                    || pch>='A' && pch<='Z'
                                    || pch>='0' && pch<='9'
                                    || pch=='.' || pch=='_' || pch=='[' || pch==']'){
                                argBuilder.append(pch);
                            }else{
                                break;
                            }
                            i++;
                        }
                        if(i!=2){
                            char pch=template[i+idx];
                            if(pch=='}'){
                                idx+=i+1;
                                String param=argBuilder.toString();
                                appendParam(builder,param,params);
                            }else{
                                int k=0;
                                while(k<i){
                                    builder.append(template[idx+k]);
                                    k++;
                                }
                                idx+=i;
                            }
                        }else{
                            builder.append("${");
                        }
                    }else{
                        builder.append(ch);
                        idx++;
                    }
                }else{
                    builder.append(ch);
                    idx++;
                }
            }else{
                builder.append(ch);
                idx++;
            }


        }
        return builder.toString();
    }

    private static void appendParam(StringBuilder builder, String paramKey, Map<String, Object> params) {
        Object obj=getObjectByDotKey(params,paramKey);
        builder.append(obj);
    }
    private static Object getObjectByDotKey(Object obj,String dotKey){
        if(obj==null){
            return null;
        }
        if(dotKey==null || "".equals(dotKey)){
            if(obj instanceof IGeneratable){
                return ((IGeneratable) obj).toGen();
            }
            return obj;
        }
        String[] keys=dotKey.split("\\.",2);
        Class clazz=obj.getClass();
        if(keys.length==0){
            return obj;
        }
        if(clazz.getName().startsWith("java.lang.")){
            return obj;
        }
        String curKey=keys[0];
        String nextKey=null;
        if(keys.length>1){
            nextKey=keys[1];
        }
        if(clazz.isArray()){
            if(curKey.matches("\\[.+\\]")){
                int idx=Integer.parseInt(curKey.substring(1,curKey.length()-1));
                Object nobj=Array.get(obj,idx);
                return getObjectByDotKey(nobj,nextKey);
            }
        }
        else if(obj instanceof Map){
            Map map=(Map)obj;
            if(map.containsKey(curKey)){
                return getObjectByDotKey(map.get(curKey),nextKey);
            }
        }
        else if(obj instanceof Collation){
            if(curKey.matches("\\[.+\\]")){
                int idx=Integer.parseInt(curKey.substring(1,curKey.length()-2));
                Object nobj=getCollectionIndexObj((Collection)obj,idx);
                return getObjectByDotKey(nobj,nextKey);
            }
        }else{
            Field field=getClassFieldByName(clazz,curKey);
            if(field!=null){
                try{
                    field.setAccessible(true);
                    Object val=field.get(obj);
                    return getObjectByDotKey(val,nextKey);
                }catch(Exception e){

                }
            }
        }
        if(curKey.matches(".+\\[.+\\]")){
            int fidx=curKey.indexOf("[");
            String pkey=curKey.substring(0,fidx);
            Object val=getObjectByDotKey(obj,pkey);
            if(val!=null){
                return getObjectByDotKey(val,curKey.substring(fidx));
            }
        }
        return null;
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
}
