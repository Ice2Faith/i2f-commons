package i2f.commons.core.utils.jdbc.data;

import i2f.commons.core.exception.CommonsException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作为数据库查询返回结果集存在
 * 提供更高效的值获取方式
 * 通过方法：getCols():List<String>获取返回结果的列名
 * 通过方法：getDatas():List<Map<String, Object>>获取所有的结果，其中的每一个Map就是一行数据
 * Map以列名为键，以数据为值
 * 因此你需要注意列名的大小写是影响你获取值的
 * 因此提供了方法：getDataIgnoreCase(int,String):T来不区分大小写方式获取一个值
 */
@Data
@NoArgsConstructor
public class DBResultData{
    private List<String> cols=new ArrayList<>();
    private List<Map<String,Object>> datas=new ArrayList<>();

    public DBResultData(List<String> cols,List<Map<String,Object>> datas){
        this.cols=cols;
        this.datas=datas;
    }
    public int getCountRows(){
        return datas.size();
    }
    public int getCountCols(){
        return cols.size();
    }
    public boolean hasData(){
        return datas.size()!=0;
    }
    public String getColumnName(int index){
        return cols.get(index);
    }
    public<T> T getData(int line,int col){
        return (T)(datas.get(line).get(cols.get(col)));
    }
    public<T> T getData(int line,String colName){
        return  (T)(datas.get(line).get(colName));
    }
    public<T> T getDataIgnoreCase(int line,String colName){
        T ret=null;
        for(String pk : cols){
            if(pk.equalsIgnoreCase(colName)){
                return  (T)(datas.get(line).get(pk));
            }
        }
        return  ret;
    }

    /**
     * 将此结果集转换为对应的实体类列表，也是列名和属性进行匹配实现
     * 列名属性名相同则赋值
     * 否则不处理
     * 原则上来说，查询结果对应实体类直接映射是最好的
     * 但是，如果结果里面存在实体里面多余的列，也是可以进行解析的，只不过可能效果不好
     * 极端情况下，结果不可用
     * 用法：
     * List<Admin> list=result.parserBeans(Admin.class,true);
     * 这样，就尝试将结果集转换为Admin的列表，并且忽略列名大小写进行匹配
     * @param clazz 类类型
     * @param ignoreCase 是否忽略属性名列名大小写匹配
     * @param <T> 类型
     * @return 实体列表
     * @throws CommonsException
     */
    public<T> List<T> parserBeans(Class<T> clazz,boolean ignoreCase) throws CommonsException {
        List<T> ret=new ArrayList<>();
        try{
            int colCount=cols.size();
            Field[] fields=clazz.getDeclaredFields();
            for(Map<String,Object> item : datas){
                T obj=clazz.newInstance();
                for(int i=0;i<colCount;i++){
                    String colName=cols.get(i);
                    Object colValue=item.get(colName);

                    for(Field field : fields){
                        field.setAccessible(true);
                        String attName=field.getName();
                        try{
                            if(ignoreCase){
                                if(colName.equalsIgnoreCase(attName) && colValue!=null){
                                    field.set(obj,colValue);
                                }
                            }else{
                                if(colName.equals(attName) && colValue!=null){
                                    field.set(obj,colValue);
                                }
                            }
                        }catch(Exception e){
                            //ignore error
                        }

                    }

                }
                ret.add(obj);
            }


        }catch(Exception e){
            throw CommonsException.cat("parse bean error",e,null);
        }
        return ret;
    }

    /**
     * 将此结果按照给定的属性和列名进行映射赋值，返回一个类对象列表
     * 注意如果出现类型不一致将会被跳过，不进行赋值
     * 如果出现匹配不上，也会被跳过，也就是映射中，不存在对应的列名，或者不存在对应的属性名
     * 因此你要保证，列名和属性名映射没有写错，特别是在不忽略大小写的情况下
     * @param clazz 类类型
     * @param attrColMapping 类属性--列名的映射，key=类属性，value=列名
     * @param ignoreCase 是否忽略列名与属性的大小写
     * @param <T> 类型
     * @return 返回满足要求的对象列表
     */
    public<T> List<T> parserBeans(Class<T> clazz,Map<String,String> attrColMapping,boolean ignoreCase) throws CommonsException {
        List<T> ret=new ArrayList<>();
        try{
            //获取所有对象属性
            Field[] fields=clazz.getDeclaredFields();
            //遍历每一行数据
            for(Map<String,Object> line : datas){
                T obj=clazz.newInstance();
                //比较列名和哪一个属性对应
                for(String dataKey : line.keySet()){
                    for(String attrKey : attrColMapping.keySet()){
                        //获取匹配上列名的属性名
                        String attrMapCol=attrColMapping.get(attrKey);
                        boolean isCmp=false;
                        if(ignoreCase){
                            if(dataKey.equalsIgnoreCase(attrMapCol)){
                                isCmp=true;
                            }
                        }else{
                            if(dataKey.equals(attrMapCol)){
                                isCmp=true;
                            }
                        }
                        //如果属性名与列名按照映射匹配上了
                        if(isCmp){
                            //获得匹配上的属性名对应的的属性
                            for(Field field : fields){
                                //获得匹配属性名的属性
                                field.setAccessible(true);
                                String fieldName=field.getName();
                                isCmp=false;
                                if(ignoreCase){
                                    if(attrKey.equalsIgnoreCase(fieldName)){
                                        isCmp=true;
                                    }
                                }else{
                                    if(attrKey.equals(fieldName)){
                                        isCmp=true;
                                    }
                                }
                                if(isCmp){
                                    //如果属性名与属性匹配上了，就执行赋值
                                    try {
                                        field.set(obj, line.get(dataKey));
                                    }catch (Exception e){
                                        //如果出现类型不兼容，赋值失败等，不处理，直接跳过
                                    }
                                }
                            }
                        }
                    }
                }
                ret.add(obj);
            }
        }catch (Exception e){
            throw CommonsException.cat("parse beans error",e,null);
        }

        return ret;
    }

}
