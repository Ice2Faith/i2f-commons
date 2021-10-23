package i2f.commons.core.utils.generator.simple.impl;

import i2f.commons.core.utils.generator.simple.IGeneratable;
import lombok.Data;

/**
 * @author ltb
 * @date 2021/9/11
 */
@Data
public class TrimGenerator<T> implements IGeneratable {
    protected T data;
    protected Object prefix="";
    protected Object suffix="";

    public TrimGenerator(T data){
        this.data=data;
    }

    public TrimGenerator(T data, Object prefix, Object suffix) {
        this.data = data;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public String toGen() {
        String rt=IGeneratable.gen(this.data);
        if(rt==null || "".equals(rt)){
            return rt;
        }
        String pre=IGeneratable.gen(prefix);
        if(pre!=null && !"".equals(pre)){
            if(!rt.startsWith(pre)){
                rt=rt.trim();
            }
            if(rt.startsWith(pre)){
                rt=rt.substring(pre.length());
            }
        }
        String suf=IGeneratable.gen(suffix);
        if(suf!=null && !"".equals(suf)){
            if(!rt.endsWith(suf)){
                rt=rt.trim();
            }
            if(rt.endsWith(suf)){
                rt=rt.substring(0,rt.length()-suf.length());
            }
        }
        return rt;
    }
}
