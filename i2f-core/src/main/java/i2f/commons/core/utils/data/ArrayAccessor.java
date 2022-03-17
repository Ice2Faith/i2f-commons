package i2f.commons.core.utils.data;

import java.lang.reflect.Array;

/**
 * @author ltb
 * @date 2022/3/16 19:30
 * @desc
 */
public class ArrayAccessor {
    protected Object obj;
    public ArrayAccessor(){

    }
    public ArrayAccessor(Object obj){
        this.obj=obj;
    }
    public boolean isnull(){
        return obj==null;
    }
    public boolean isArray(){
        if(obj==null){
            return false;
        }
        return obj.getClass().isArray();
    }
    public int length(){
        return Array.getLength(obj);
    }
    public<T> T get(int index){
        return (T)Array.get(obj,index);
    }
    public void set(int index,Object val){
        Array.set(obj,index,val);
    }
}
