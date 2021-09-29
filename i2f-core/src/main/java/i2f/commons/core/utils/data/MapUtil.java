package i2f.commons.core.utils.data;


import i2f.commons.core.utils.safe.CheckUtil;

import java.util.Iterator;
import java.util.Map;

public class MapUtil {
    public static Map dels(Map map, Object ... keys){
        for(Object item : keys){
            map.remove(item);
        }
        return map;
    }
    public static Map<String,Object> delsStr(Map<String,Object> map, boolean ignoreCase, String ... keys){
        for(String pk : keys){
            Iterator<String> it=map.keySet().iterator();
            while (it.hasNext()){
                String key=it.next();
                if(ignoreCase){
                    if(key.equalsIgnoreCase(pk)){
                        it.remove();
                        map.remove(key);
                    }
                }else{
                    if(key.equals(pk)){
                        it.remove();
                        map.remove(key);
                    }
                }
            }
        }
        return map;
    }
    public static Map cleanNull(Map map){
        Iterator<String> it=map.keySet().iterator();
        while(it.hasNext()){
            String key=it.next();
            Object val=map.get(key);
            if(CheckUtil.isNull(val)){
                it.remove();
                map.remove(key);
            }
        }
        return map;
    }
    public static Map copy2(Map dstMap,Map srcMap,boolean overWrite){
        for(Object item : srcMap.keySet()){
            Object val=srcMap.get(item);
            if(dstMap.containsKey(item)){
                if(overWrite){
                    dstMap.put(item,val);
                }
            }else{
                dstMap.put(item,val);
            }

        }
        return dstMap;
    }
    public static Map adds(Map map,Object[] keys,Object ... vals){
        if(CheckUtil.isExEmptyArray(keys,vals)){
            return map;
        }
        int minLen=keys.length;
        if(vals.length<minLen){
            minLen= vals.length;
        }
        for (int i = 0; i < minLen; i++) {
            map.put(keys[i],vals[i]);
        }
        return map;
    }
    public static Map addSameVal(Map map,Object val,Object ... keys){
        if(CheckUtil.isExEmptyArray(keys)){
            return map;
        }
        for(Object item : keys){
            map.put(item,val);
        }
        return map;
    }
    public static <T> T out(Map map,Object key){
        Object val=map.get(key);
        return (T)val;
    }
    public static Object[] outs(Map map,Object ... keys){
        Object[] rv=new Object[]{};
        if(keys==null || keys.length==0){
            return rv;
        }
        rv=new Object[keys.length];
        for (int i = 0; i < keys.length; i++) {
            rv[i]=map.get(keys[i]);
        }
        return rv;
    }
    public static <T> T outStr(Map map,boolean ignoreCase,String key){
        Object val=map.get(key);
        if(val!=null) {
            return (T) val;
        }
        Iterator<String> it=map.keySet().iterator();
        while(it.hasNext()){
            String item=it.next();
            if(ignoreCase){
                if(item.equalsIgnoreCase(key)){
                    return (T)map.get(item);
                }
            }else{
                if(item.equals(key)){
                    return (T)map.get(item);
                }
            }
        }
        return null;
    }
    public static Object[] outsStr(Map map,boolean ignoreCase,String ... keys){
        Object[] rv=new Object[]{};
        if(keys==null || keys.length==0){
            return rv;
        }
        rv=new Object[keys.length];
        for (int i = 0; i < keys.length; i++) {
            Iterator<String> it=map.keySet().iterator();
            while(it.hasNext()){
                String item=it.next();
                if(ignoreCase){
                    if(item.equalsIgnoreCase(keys[i])){
                        rv[i]=map.get(item);
                    }
                }else{
                    if(item.equals(keys[i])){
                        rv[i]=map.get(item);
                    }
                }
            }
        }
        return rv;
    }
}
