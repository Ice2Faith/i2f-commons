package i2f.commons.core.utils.reflect.simple.reflect.impl;

import i2f.reflect.ValueAccessor;

import java.lang.reflect.Array;

/**
 * @author ltb
 * @date 2022/3/14 9:28
 * @desc
 */
public class ArrayValueAccessor implements ValueAccessor {
    public Object arr;
    public int idx;
    public ArrayValueAccessor(Object arr,int idx) {
        this.arr=arr;
        this.idx=idx;
    }

    @Override
    public Object get() {
        return Array.get(arr,idx);
    }

    @Override
    public void set(Object obj) {
        Array.set(arr,idx,obj);
    }

    public Object getArray(){
        return arr;
    }
    public int getIndex(){
        return idx;
    }
}
