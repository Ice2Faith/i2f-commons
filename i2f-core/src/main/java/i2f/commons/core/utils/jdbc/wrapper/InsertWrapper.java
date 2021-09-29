package i2f.commons.core.utils.jdbc.wrapper;


import i2f.commons.core.data.Pair;
import i2f.commons.core.utils.jdbc.wrapper.base.SqlBaseWrapper;
import i2f.commons.core.utils.jdbc.wrapper.base.SqlBaseWrapperBuilder;
import i2f.commons.core.utils.safe.CheckUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class InsertWrapper extends SqlBaseWrapper {
    //col val:age,12
    public List<Pair<String,Object>> valPairs=new ArrayList<Pair<String,Object>>();
    //col val:createTime,now()
    public List<Pair<String,String>> nativeValPairs=new ArrayList<Pair<String,String>>();

    private Long returnIdValue=null;
    public Long getReturnId(){
        return returnIdValue;
    }

    public static WrapperBuilder build(){
        return new WrapperBuilder();
    }
    public static WrapperBuilder build(InsertWrapper wrapper){
        return new WrapperBuilder(wrapper);
    }

    public static class WrapperBuilder {
        private SqlBaseWrapperBuilder baseWrapper;
        private InsertWrapper wrapper;
        public WrapperBuilder(){
            wrapper=new InsertWrapper();
            baseWrapper=new SqlBaseWrapperBuilder(wrapper);
        }
        public WrapperBuilder(InsertWrapper wrapper){
            this.wrapper=wrapper;
            baseWrapper=new SqlBaseWrapperBuilder(wrapper);
        }
        public InsertWrapper done(){
            return this.wrapper;
        }


        public WrapperBuilder add(String col,Object val){
            Pair<String,Object> pair=new Pair<String,Object>();
            pair.key=col;
            pair.val=val;
            wrapper.valPairs.add(pair);
            return this;
        }

        public WrapperBuilder adds(Map<String,Object> kvs){
            if(CheckUtil.isEmptyMap(kvs)){
                return this;
            }
            for(String item : kvs.keySet()){
                Object val=kvs.get(item);
                if(val==null){
                    continue;
                }
                String sval=String.valueOf(val);
                if(CheckUtil.isEmptyStr(sval,false)){
                    continue;
                }
                Pair<String,Object> pair=new Pair<String,Object>();
                pair.key=item;
                pair.val=val;
                wrapper.valPairs.add(pair);
            }
            return this;
        }
        public WrapperBuilder addNative(String col,String val){
            Pair<String,String> pair=new Pair<String,String>();
            pair.key=col;
            pair.val=val;
            wrapper.nativeValPairs.add(pair);
            return this;
        }

        public WrapperBuilder table(String tableName){
            baseWrapper.table(tableName);
            return this;
        }
        public WrapperBuilder table(Class clazz, String prefix, String suffix){
            baseWrapper.table(clazz, prefix, suffix);
            return this;
        }
    }
}
