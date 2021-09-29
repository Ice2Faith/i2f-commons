package i2f.commons.core.data;


import i2f.commons.core.utils.data.MapUtil;

import java.util.HashMap;

public class KeyMap extends HashMap<String,Object> {
    public <T> T out(String name){
        return MapUtil.out(this,name);
    }
    public <T> T in(String name,Object val){
        T rv=out(name);
        this.put(name,val);
        return rv;
    }
    public <T> T del(String name){
        T rv=out(name);
        this.remove(name);
        return rv;
    }
    public void cleanNull(){
        MapUtil.cleanNull(this);
    }
    public void dels(String ... names){
        MapUtil.dels(this,false,names);
    }
    public Object[] qry(String ... names){
        return MapUtil.outs(this,names);
    }
    public void adds(String[] keys,Object ... vals){
        MapUtil.adds(this,keys,vals);
    }
    public void addsSameVal(Object val,String ... keys){
        MapUtil.addSameVal(this,val,keys);
    }
}
