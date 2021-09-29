package i2f.commons.core.utils.jdbc.wrapper;


import i2f.commons.core.utils.jdbc.wrapper.base.SqlBaseWrapper;
import i2f.commons.core.utils.jdbc.wrapper.base.SqlBaseWrapperBuilder;
import i2f.commons.core.utils.safe.CheckUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class InsertBatchWrapper extends SqlBaseWrapper {

    public Map<String,Object> cols=new HashMap<String,Object>();

    public List<Map<String,Object>> multiVals=new ArrayList<Map<String,Object>>();

    public static WrapperBuilder build(){
        return new WrapperBuilder();
    }
    public static WrapperBuilder build(InsertBatchWrapper wrapper){
        return new WrapperBuilder(wrapper);
    }

    public static class WrapperBuilder {
        private SqlBaseWrapperBuilder baseWrapper;
        private InsertBatchWrapper wrapper;
        public WrapperBuilder(){
            wrapper=new InsertBatchWrapper();
            baseWrapper=new SqlBaseWrapperBuilder(wrapper);
        }
        public WrapperBuilder(InsertBatchWrapper wrapper){
            this.wrapper=wrapper;
            baseWrapper=new SqlBaseWrapperBuilder(wrapper);
        }
        public InsertBatchWrapper done(){
            return this.wrapper;
        }

        public WrapperBuilder col(String col){
            return cols(col);
        }

        public WrapperBuilder cols(String ... cols){
            if(CheckUtil.isEmptyArray(cols)){
                return this;
            }
            for(String item : cols){
                wrapper.cols.put(item,null);
            }
            return this;
        }

        public WrapperBuilder add(Map<String,Object> kvs){
            if(CheckUtil.isEmptyMap(kvs)){
                return this;
            }
            Map<String,Object> item=new HashMap<String,Object>();
            for(String key : wrapper.cols.keySet()){
                if(!kvs.containsKey(key)){
                    item.put(key,null);
                }
                Object val=kvs.get(key);
                item.put(key,val);
            }
            wrapper.multiVals.add(item);
            return this;
        }

        public WrapperBuilder add(String[] cols,Object ... vals){
            Map<String,Object> kvs=new HashMap<>();
            int minLen=cols.length;
            if(vals.length<minLen){
                minLen=vals.length;
            }
            for(int i=0;i<minLen;i++){
                kvs.put(cols[i],vals[i]);
            }
            return add(kvs);
        }

        public WrapperBuilder adds(List<Map<String,Object>> records){
            if(CheckUtil.isEmptyCollection(records)){
                return this;
            }
            for(Map<String,Object> item : records){
                add(item);
            }
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
