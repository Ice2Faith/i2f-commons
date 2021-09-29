package i2f.commons.component.mybatis;


import i2f.commons.core.utils.safe.CheckUtil;

import java.util.HashMap;
import java.util.Map;

public class CommCondUtil {
    public static Map<String,Object> eq(String col,Object val){
        Map<String,Object> ret=new HashMap<>();
        ret.put(col,val);
        return ret;
    }
    public static Map<String,Object> eqs(String[] cols,Object ... vals){
        Map<String,Object> ret=new HashMap<>();
        if(CheckUtil.isExEmptyArray(cols,vals)){
            return ret;
        }
        int minLen= cols.length;
        if(vals.length<minLen){
            minLen= vals.length;
        }
        for (int i = 0; i < minLen; i++) {
            if(vals[i]!=null){
                ret.put(cols[i],vals[i]);
            }
        }
        return ret;
    }
    public static Map<String,Object[]> vals(String col,Object ... vals){
        return valCols(new String[]{col},vals);
    }
    public static Map<String,Object[]> valCols(String[] cols,Object ... vals){
        Map<String,Object[]> ret=new HashMap<>();
        if(CheckUtil.isExEmptyArray(cols,vals)){
            return ret;
        }
        for(String item : cols){
            ret.put(item,vals);
        }
        return ret;
    }
    public static Map<String,Object[]> likes(String col,String ... vals){
        return vals(col,vals);
    }
    public static Map<String,Object[]> likesCols(String[] cols,String ... vals){
        return valCols(cols, vals);
    }
    public static Map<String,Object[]> in(String col,Object ... vals){
        return vals(col,vals);
    }
    public static Map<String,Object[]> inCols(String[] cols,Object ... vals){
        return valCols(cols,vals);
    }
}
