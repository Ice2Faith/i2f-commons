package i2f.commons.core.utils.reflect.simple.reflect;

import i2f.commons.core.data.interfaces.ICompare;
import i2f.commons.core.data.interfaces.IMap;
import i2f.commons.core.data.interfaces.impl.EqualsCompare;
import i2f.commons.core.utils.reflect.simple.reflect.convert.ConvertResolver;
import i2f.commons.core.utils.reflect.simple.reflect.core.ReflectResolver;
import i2f.commons.core.utils.reflect.simple.reflect.exception.InstanceException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/17 9:12
 * @desc
 */
public class BeanResolver {
    public static <T> T copy(Object srcObj, Class<T> dstClass) {
        return copy(srcObj, dstClass, false);
    }

    public static <T> T copy(Object srcObj, Class<T> dstClass, boolean includeNull) {
        return copy(srcObj, dstClass, includeNull, null);
    }

    /**
     * 从任意对象srcObj（可以为Map/Bean）复制到dstClass类型的实体中
     * includeNull指定是否包含null值得复制
     * keyCmp可为null，指定两个属性名之间的映射是否可行，null，默认为属性名一致
     * @param srcObj
     * @param dstClass
     * @param includeNull
     * @param keyCmp
     * @param <T>
     * @return
     */
    public static <T> T copy(Object srcObj, Class<T> dstClass, boolean includeNull, ICompare<String> keyCmp) {
        try {
            T dstObj = dstClass.newInstance();
            copy(srcObj, dstObj, includeNull, keyCmp);
            return dstObj;
        } catch (Exception e) {
            throw new InstanceException("instance class{" + dstClass.getName() + "} error:" + e.getMessage());
        }
    }

    public static void copy(Object srcObj, Object dstObj) {
        copy(srcObj, dstObj, false);
    }

    public static void copy(Object srcObj, Object dstObj, boolean includeNull) {
        copy(srcObj, dstObj, includeNull, null);
    }

    /**
     * 从任意对象srcObj（可以为Map/Bean）复制到dstObj(可以为Map/Bean)类型的实体中
     * includeNull指定是否包含null值得复制
     * keyCmp可为null，指定两个属性名之间的映射是否可行，null，默认为属性名一致
     * @param srcObj
     * @param dstObj
     * @param includeNull
     * @param keyCmp
     * @return
     */
    public static void copy(Object srcObj, Object dstObj, boolean includeNull, ICompare<String> keyCmp) {
        if (dstObj == null || srcObj == null) {
            return;
        }
        if (keyCmp == null) {
            keyCmp = new EqualsCompare<String>();
        }
        Class srcClass = srcObj.getClass();
        Class dstClass = dstObj.getClass();
        if (srcObj instanceof Map && dstObj instanceof Map) {
            Map srcMap = (Map) srcObj;
            Map dstMap = (Map) dstObj;
            for (Object item : srcMap.keySet()) {
                Object val = srcMap.get(item);
                if (!includeNull && val == null) {
                    continue;
                }
                dstMap.put(item, val);
            }
        } else if (srcObj instanceof Map) {
            Map srcMap = (Map) srcObj;
            List<PropertyAccessor> dstFields = ReflectResolver.getLogicalWritableFields(dstClass);
            for (Object item : srcMap.keySet()) {
                String key = String.valueOf(item);
                for (PropertyAccessor accessor : dstFields) {
                    if (keyCmp.compare(accessor.getName(), key) == 0) {
                        Object val = srcMap.get(item);
                        if (!includeNull && val == null) {
                            continue;
                        }
                        if (ConvertResolver.isValueConvertible(val, accessor.getType())) {
                            val = ConvertResolver.tryConvertible(val, accessor.getType());
                        }
                        accessor.setInvokeObject(dstObj);
                        accessor.set(val);
                    }
                }
            }
        } else if (dstObj instanceof Map) {
            List<PropertyAccessor> srcFields = ReflectResolver.getLogicalReadableFields(srcClass);
            Map dstMap = (Map) dstObj;
            for (PropertyAccessor item : srcFields) {
                item.setInvokeObject(srcObj);
                Object val = item.get();
                if (!includeNull && val == null) {
                    continue;
                }
                dstMap.put(item.getName(), val);
            }
        } else {
            List<PropertyAccessor> srcFields = ReflectResolver.getLogicalReadableFields(srcClass);
            List<PropertyAccessor> dstFields = ReflectResolver.getLogicalWritableFields(dstClass);
            for (PropertyAccessor src : srcFields) {
                String srcName = src.getName();
                for (PropertyAccessor dst : dstFields) {
                    if (keyCmp.compare(dst.getName(), srcName) == 0) {
                        src.setInvokeObject(srcObj);
                        Object val = src.get();
                        if (!includeNull && val == null) {
                            continue;
                        }
                        if (ConvertResolver.isValueConvertible(val, dst.getType())) {
                            val = ConvertResolver.tryConvertible(val, dst.getType());
                        }
                        dst.setInvokeObject(dstObj);
                        dst.set(val);
                        break;
                    }
                }
            }
        }
    }

    /**
     * 将Map转换为类型为clazz的Bean
     * includeNull则包含值为null的字段
     * mapKey指定按照什么关系将Map的key映射为Bean的字段名
     * @param map
     * @param clazz
     * @param includeNull
     * @param mapKey
     * @param <T>
     * @return
     */
    public static<T> T map2Bean(Map<String,Object> map, Class<T> clazz, boolean includeNull, IMap<String,String> mapKey){
        if(mapKey!=null){
            Map<String,Object> nmap=new HashMap<>();
            for(Map.Entry<String, Object> item : map.entrySet()){
                nmap.put(mapKey.map(item.getKey()),item.getValue());
            }
            map=nmap;
        }
        return copy(map,clazz,includeNull);
    }

    /**
     * 将Bean转换为Map
     * includeNull则包含值为null的字段
     * mapKey指定按照什么关系将Bean的字段名映射为Map的key
     * @param obj
     * @param includeNull
     * @param mapKey
     * @return
     */
    public static Map<String, Object> bean2Map(Object obj,boolean includeNull, IMap<String,String> mapKey){
        if(obj==null){
            return null;
        }
        Map<String,Object> ret=new HashMap<>();
        copy(obj,ret,includeNull);
        if(mapKey!=null){
            Map<String,Object> nmap=new HashMap<>();
            for(Map.Entry<String, Object> item : ret.entrySet()){
                nmap.put(mapKey.map(item.getKey()),item.getValue());
            }
            ret=nmap;
        }
        return ret;
    }

    public static<T> List<T> mapList2BeanList(List<Map<String,Object>> list,Class<T> clazz,boolean includeNull, IMap<String,String> mapKey){
        List<T> ret=new ArrayList<>();
        for(Map<String,Object> item : list){
            ret.add(map2Bean(item,clazz,includeNull,mapKey));
        }
        return ret;
    }

    public static List<Map<String, Object>> beanList2MapList(List list,boolean includeNull, IMap<String,String> mapKey){
        List<Map<String, Object>> ret=new ArrayList<>();
        for(Object item : list){
            ret.add(bean2Map(item,includeNull,mapKey));
        }
        return ret;
    }

    public static void empty2null(Object obj,boolean trim){
        if(obj==null){
            return;
        }
        Class clazz = obj.getClass();
        List<PropertyAccessor> fds=ReflectResolver.getLogicalReadWriteFields(clazz);
        for(PropertyAccessor item : fds){
            item.setInvokeObject(obj);
            Object val=item.get();
            if(val!=null && val instanceof String){
                if(trim){
                    val=((String)val).trim();
                }
                if("".equals(val)){
                    item.set(null);
                }
            }
        }
    }

}
