package i2f.commons.core.utils.reflect.simple.reflect.impl;

import i2f.reflect.ValueAccessor;

import java.util.List;

/**
 * @author ltb
 * @date 2022/3/14 9:28
 * @desc
 */
public class ListValueAccessor implements ValueAccessor {
    public List list;
    public int idx;
    public ListValueAccessor(List list, int idx) {
        this.list=list;
        this.idx=idx;
    }

    @Override
    public Object get() {
        return list.get(idx);
    }

    @Override
    public void set(Object obj) {
        list.set(idx,obj);
    }

    public List getList(){
        return list;
    }
    public int getIndex(){
        return idx;
    }
}
