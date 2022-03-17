package i2f.commons.core.data.interfaces.impl;

import i2f.commons.core.data.interfaces.ICompare;

/**
 * @author ltb
 * @date 2022/3/17 11:08
 * @desc
 */
public class EqualsCompare<T> implements ICompare<T> {
    @Override
    public int compare(T t1, T t2) {
        if(t1==null && t2==null){
            return 0;
        }
        if(t1==null){
            return 1;
        }
        if(t2==null){
            return -1;
        }
        if(t1.equals(t2)){
            return 0;
        }
        return 1;
    }
}
