package i2f.commons.component.excel.poi.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 扁平Excel表头信息
 * 键：
 *  用作读取和写入时的Map结构中的键名
 * 值：
 *  用作表格的标题行
 * 对应关系：
 *  Map中的键《--》表格标题名称
 * 用法：推荐使用构造器方式：
 *  SheetMeta meta=SheetMeta.build()
 *      .add("name","用户姓名")
 *      .done();
 */
public class SheetMeta extends HashMap<String,String> {
    /**
     * 表格名称，
     * 在读取表格时，先优先按照sheetIndex查找表格
     * 在写入表格时，此项将作为表格名称
     */
    public String sheetName;
    /**
     * 表格索引，可为空
     * 在读取表格时，优先按照此项指定的索引读取，否则按照sheetName查找
     */
    public Integer sheetIndex;
    /**
     * 键的索引顺序
     * 用作辅助进行顺序的读取本键值映射
     * 正常情况下，你不应该修改此内容，仅用作读取
     */
    public List<String> keySortList=new ArrayList<String>();

    @Override
    public String put(String key, String value) {
        if(!this.containsKey(key)){
            keySortList.add(key);
        }
        return super.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        for(String item : m.keySet()){
            if(!this.containsKey(item)){
                keySortList.add(item);
            }
        }
        super.putAll(m);
    }

    public static Builder build(){
        return new Builder();
    }

    public static class Builder{
        private SheetMeta sheetMeta;
        public Builder(){
            sheetMeta=new SheetMeta();
        }
        public Builder setSheetName(String sheetName){
            sheetMeta.sheetName=sheetName;
            return this;
        }
        public Builder setSheetIndex(Integer sheetIndex){
            sheetMeta.sheetIndex=sheetIndex;
            return this;
        }
        public Builder add(String key,String title){
            sheetMeta.put(key, title);
            return this;
        }
        public Builder adds(Map<String,String> map){
            sheetMeta.putAll(map);
            return this;
        }
        public SheetMeta done(){
            return sheetMeta;
        }
    }
}
