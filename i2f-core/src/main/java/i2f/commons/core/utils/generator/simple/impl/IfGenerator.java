package i2f.commons.core.utils.generator.simple.impl;

import i2f.commons.core.utils.generator.simple.IGeneratable;
import lombok.Data;

/**
 * @author ltb
 * @date 2021/9/11
 */
@Data
public abstract class IfGenerator<T> implements IGeneratable {
    protected T data;
    protected Object prefix="";
    protected Object suffix="";

    protected abstract boolean isPass(T data);

    protected String data2String(T data){
        return IGeneratable.gen(data);
    }

    public IfGenerator(T data){
        this.data=data;
    }

    public IfGenerator(T data, Object prefix, Object suffix) {
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
        if(isPass(this.data)){
            StringBuilder builder=new StringBuilder();
            builder.append(IGeneratable.gen(prefix));
            builder.append(data2String(this.data));
            builder.append(IGeneratable.gen(suffix));
            return builder.toString();
        }
        return "";
    }
}
