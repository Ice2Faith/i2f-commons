package i2f.commons.core.utils.generator.simple.impl;

import i2f.commons.core.utils.generator.simple.IGeneratable;
import lombok.Data;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author ltb
 * @date 2021/9/11
 */
@Data
public class ForGenerator<T> implements IGeneratable {

    protected T data;
    protected Object prefix="";
    protected Object suffix="";
    protected Object separator=",";

    protected String item2String(boolean isFirst,Object obj,int idx){
        return IGeneratable.gen(obj);
    }

    protected boolean filter(int idx,Object obj){
        return true;
    }

    public ForGenerator(T data){
        this.data=data;
    }
    public ForGenerator(T data, Object separator){
        this.data=data;
        this.separator=separator;
    }
    public ForGenerator(T data, Object separator, Object prefix, Object suffix){
        this.data=data;
        this.separator=separator;
        this.prefix=prefix;
        this.suffix=suffix;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public String toGen() {
        if(this.data==null){
            return item2String(true,this.data,0);
        }
        StringBuilder builder=new StringBuilder();
        builder.append(IGeneratable.gen(prefix));

        Class clazz=this.data.getClass();
        if(clazz.isArray()){

            int len= Array.getLength(this.data);
            int idx=0;
            while(idx<len){
                Object obj=Array.get(this.data,idx);
                if(!filter(idx,obj)){
                    idx++;
                    continue;
                }
                if(idx!=0){
                    builder.append(IGeneratable.gen(separator));
                }
                builder.append(item2String(idx==0,obj,idx));
                idx++;
            }

        }else if(this.data instanceof Iterable){
            Iterable itble=(Iterable)this.data;

            Iterator it= itble.iterator();
            boolean isFirst=true;
            int idx=0;
            while(it.hasNext()){
                Object obj=it.next();
                if(!filter(idx,obj)){
                    idx++;
                    continue;
                }
                if(!isFirst){
                    builder.append(IGeneratable.gen(separator));
                }
                builder.append(item2String(isFirst,obj,idx));
                isFirst=false;
                idx++;
            }

        }else if(this.data instanceof Map){
            Map map=(Map)this.data;

            boolean isFirst=true;
            Set<Map.Entry> vals=map.entrySet();
            Iterator it= vals.iterator();
            int idx=0;
            while(it.hasNext()){
                Object obj=it.next();
                if(!filter(idx,obj)){
                    idx++;
                    continue;
                }
                if(!isFirst){
                    builder.append(IGeneratable.gen(separator));
                }
                builder.append(item2String(isFirst,obj,idx));
                isFirst=false;
                idx++;
            }
        }else{
            return item2String(true,this.data,0);
        }
        builder.append(IGeneratable.gen(suffix));
        return builder.toString();
    }
}
