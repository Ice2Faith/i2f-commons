package i2f.commons.core.utils.data;

import i2f.commons.core.data.interfaces.IMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ltb
 * @date 2022/3/16 19:39
 * @desc
 */
public class MapBean extends HashMap<String,Object> {
    public<T> T getAs(String key){
        return (T)get(key);
    }
    public MapBean mapKeys(IMap<String,String> keyMapper){
        MapBean ret=new MapBean();
        for(Entry<String,Object> item : entrySet()){
            ret.put(keyMapper.map(item.getKey()),item.getValue());
        }
        return ret;
    }
    public void removeNulls(){
        Set<String> rmKeys=new HashSet<>();
        for(Entry<String,Object> item : entrySet()){
            if(item.getValue()==null){
                rmKeys.add(item.getKey());
            }
        }
        for(String item : rmKeys){
            remove(item);
        }
    }
}
