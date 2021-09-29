package i2f.commons.core.utils.jdbc;


import i2f.commons.core.utils.jdbc.core.JdbcDao;
import i2f.commons.core.utils.jdbc.data.DBPageData;
import i2f.commons.core.utils.jdbc.data.DBResultData;
import i2f.commons.core.utils.jdbc.data.PageContextData;
import i2f.commons.core.utils.jdbc.wrapper.*;
import i2f.commons.core.utils.jdbc.wrapper.base.SqlBase;
import i2f.commons.core.utils.jdbc.wrapper.base.SqlBaseWrapper;
import i2f.commons.core.utils.reflect.core.resolver.BeanAnnotationResolver;
import i2f.commons.core.utils.reflect.core.resolver.BeanResolver;
import i2f.commons.core.utils.reflect.core.resolver.base.ValueResolver;
import i2f.commons.core.utils.safe.CheckUtil;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JdbcDaoUtil {
    public boolean openCheckBeanIgnoreFieldAnnotation=true;

    public boolean openUseBeanAliasAnnotation=true;

    public void setOpenCheckBeanIgnoreFieldAnnotation(boolean open) {
        this.openCheckBeanIgnoreFieldAnnotation = open;
    }

    public String getTableName(Class clazz){
        if(openUseBeanAliasAnnotation){
            return BeanAnnotationResolver.getTableName(clazz);
        }
        return clazz.getSimpleName();
    }

    public void tryRemovePk(Map<String,Object> kvs,Class clazz){
        Field pkField=BeanAnnotationResolver.getBeanPrimaryKeyField(clazz);
        if(pkField!=null) {
            if (BeanAnnotationResolver.isBeanAutoPrimaryKey(pkField)) {
                if(openUseBeanAliasAnnotation) {
                    kvs.remove(BeanAnnotationResolver.getBeanPrimaryKeyColumnName(clazz));
                }else{
                    kvs.remove(pkField.getName());
                }
            }
        }
    }

    public <T> T queryFillBeanForeignKeyFields(T obj) throws SQLException {
        Map<Field, Object> fkPair = BeanAnnotationResolver.getForeignKeyFieldAndForVal(obj);
        Iterator var3 = fkPair.keySet().iterator();

        while(var3.hasNext()) {
            Field item = (Field)var3.next();
            Class clazz = item.getType();
            Object fkVal = fkPair.get(item);
            if (fkVal != null) {
                Object bean = queryByPkAnn( clazz,  fkVal);
                ValueResolver.setVal(obj, item.getName(), bean, true);
            }
        }

        return obj;
    }

    public JdbcDaoUtil(JdbcDao dao){
        this.dao=dao;
    }
    private JdbcDao dao;
    public JdbcDao getBaseDao(){
        return dao;
    }

    public<T> Map<String,Object> toMap(T obj){
        return BeanResolver.toMap(obj,true,openCheckBeanIgnoreFieldAnnotation,openUseBeanAliasAnnotation);
    }

    public<T> List<T> parseMapList(List<Map<String,Object>> list,Class<T> clazz){
        return BeanResolver.parseMapList(list,clazz,true,openCheckBeanIgnoreFieldAnnotation,openUseBeanAliasAnnotation);
    }
    public<T> DBPageData<T> parse2PageData(List<T> data, Long count, QueryWrapper wrapper){
        if(count==null){
            count=((Integer)data.size()).longValue();
        }
        return new DBPageData<T>(wrapper.pageIndex,wrapper.pageLimit,count.intValue(),data);
    }

    public void setTableNameWhenNull(SqlBaseWrapper wrapper, Class clazz){
        if(CheckUtil.isEmptyStr(wrapper.tableName,true)){
            wrapper.tableName=getTableName(clazz);
        }
    }

    public<T> int emptyTable(Class<T> clazz) throws SQLException {
        return getBaseDao().emptyTable(getTableName(clazz));
    }

    public<T> int dropTable(Class<T> clazz) throws SQLException {
        return getBaseDao().dropTable(getTableName(clazz));
    }

    public<T> long countTable(Class<T> clazz) throws SQLException {
        return getBaseDao().countTable(getTableName(clazz));
    }

    public<T> List<T> query(Class<T> clazz,QueryWrapper wrapper) throws SQLException {
        setTableNameWhenNull(wrapper,clazz);
        DBResultData list=getBaseDao().queryCommon(wrapper);
        return parseMapList(list.getDatas(),clazz);
    }
    public<T> Long count(Class<T> clazz,QueryWrapper wrapper) throws SQLException {
        setTableNameWhenNull(wrapper,clazz);
        return getBaseDao().queryCountCommon(wrapper);
    }
    public<T> DBPageData<T> page(Class<T> clazz, QueryWrapper wrapper) throws SQLException {
        setTableNameWhenNull(wrapper,clazz);
        return pageProxy(clazz,wrapper);
    }


    protected<T> DBPageData<T> pageProxy(Class<T> clazz,QueryWrapper wrapper) throws SQLException {
        PageContextData ctx =getBaseDao().queryPage(wrapper);
        List<T> data=parseMapList(ctx.data.getDatas(),clazz);
        return parse2PageData(data,ctx.count,wrapper);
    }


    public<T> int delete(Class<T> clazz, DeleteWrapper wrapper) throws SQLException {
        setTableNameWhenNull(wrapper,clazz);
        return getBaseDao().deleteCommon(wrapper);
    }

    public<T> int update(Class<T> clazz, UpdateWrapper wrapper) throws SQLException {
        setTableNameWhenNull(wrapper,clazz);
        return getBaseDao().updateCommon(wrapper);
    }
    public<T> int insert(Class<T> clazz, InsertWrapper wrapper) throws SQLException {
        setTableNameWhenNull(wrapper,clazz);
        return getBaseDao().insertCommon(wrapper);
    }
    public<T> int insertBatch(Class<T> clazz, InsertBatchWrapper wrapper) throws SQLException {
        setTableNameWhenNull(wrapper,clazz);
        return getBaseDao().insertCommonBatch(wrapper);
    }
    public<T> long insertBatchLargeData(Class<T> clazz,InsertBatchWrapper wrapper) throws SQLException {
        long startTime=System.currentTimeMillis();
        int singleMaxBatchInsertCount=4096;
        long successCount=0L;
        int batchTimes=0;
        JdbcDao dao=getBaseDao();
        List<Map<String, Object>> data=wrapper.getMultiVals();
        int countOfData=data.size();

        if(countOfData>singleMaxBatchInsertCount){
            InsertBatchWrapper curWrapper=new InsertBatchWrapper();
            curWrapper.setCols(wrapper.getCols());
            List<Map<String, Object>> once=new ArrayList<>();
            curWrapper.setMultiVals(once);
            int pcount=0;
            for(Map<String, Object> item : data){
                once.add(item);
                pcount++;
                if(pcount==singleMaxBatchInsertCount){
                    pcount=0;
                    batchTimes++;
                    int psucCount=dao.insertCommonBatch(curWrapper);
                    once.clear();
                    successCount+=psucCount;
                }
            }
            if(pcount>0){
                pcount=0;
                batchTimes++;
                int psucCount=dao.insertCommonBatch(curWrapper);
                once.clear();
                successCount+=psucCount;
            }
        }else{
            batchTimes++;
            successCount = dao.insertCommonBatch(wrapper);
        }
        long endTime=System.currentTimeMillis();
        long usedTime=endTime-startTime;
        double perTime=usedTime/(countOfData*1.0/(10*10000))/1000;
        System.out.println("batch:insert:large:use:time"+usedTime+" times:"+batchTimes+" successCount:"+successCount+" "+perTime+"s/10w records.");
        return successCount;
    }

    public<T> T queryOne(Class<T> clazz,QueryWrapper wrapper,String ... queryCols) throws SQLException {
        setTableNameWhenNull(wrapper,clazz);
        QueryWrapper.build(wrapper).cols(queryCols);
        List<T> list=parseMapList(getBaseDao().queryCommon(wrapper).getDatas(),clazz);
        if(CheckUtil.notEmptyCollection(list)){
            return list.get(0);
        }
        return null;
    }
    public<T> List<T> queryByCol(Class<T> clazz,String colName,Object colVal,String ... queryCols) throws SQLException {
        QueryWrapper wrapper=QueryWrapper.build()
                .table(getTableName(clazz))
                .cols(queryCols)
                .eq(SqlBase.LinkType.AND,colName,colVal)
                .done();
        return parseMapList(getBaseDao().queryCommon(wrapper).getDatas(),clazz);
    }
    public<T> T queryByPk(Class<T> clazz,String pkName,Object pkVal,String ... queryCols) throws SQLException {
        List<T> list=queryByCol(clazz,pkName,pkVal,queryCols);
        if(CheckUtil.notEmptyCollection(list)){
            return list.get(0);
        }
        return null;
    }
    public<T> T queryByPkAnn(Class<T> clazz,Object pkVal,String ... queryCols) throws SQLException {
        String pkName=BeanAnnotationResolver.getBeanPrimaryKeyColumnName(clazz);
        List<T> list=queryByCol(clazz,pkName,pkVal,queryCols);
        if(CheckUtil.notEmptyCollection(list)){
            return list.get(0);
        }
        return null;
    }
    public<T> int insert(T obj) throws SQLException {
        Class clazz = obj.getClass();
        Map<String, Object> kvs = toMap(obj);
        InsertWrapper wrapper=InsertWrapper.build()
                .table(getTableName(clazz))
                .adds(kvs)
                .done();
        return getBaseDao().insertCommon(wrapper);
    }

    public <T> int insertReturnWithId( T obj) throws SQLException {
        Class clazz = obj.getClass();
        Map<String, Object> kvs = toMap(obj);
        InsertWrapper wrapper=InsertWrapper.build()
                .table(getTableName(clazz))
                .adds(kvs)
                .done();
        getBaseDao().insertCommonReturnId(wrapper);
        return wrapper.getReturnId().intValue();
    }

    public<T> int insertAutoPk(T obj) throws SQLException {
        Class clazz = obj.getClass();
        Map<String, Object> kvs = toMap(obj);
        tryRemovePk(kvs,clazz);
        InsertWrapper wrapper=InsertWrapper.build()
                .table(getTableName(clazz))
                .adds(kvs)
                .done();
        return getBaseDao().insertCommon(wrapper);
    }

    public <T> int insertAutoPkReturnWithId(T obj) throws SQLException {
        Class clazz = obj.getClass();
        Map<String, Object> kvs = toMap(obj);
        tryRemovePk(kvs,clazz);
        InsertWrapper wrapper=InsertWrapper.build()
                .table(getTableName(clazz))
                .adds(kvs)
                .done();
        getBaseDao().insertCommonReturnId(wrapper);
        return wrapper.getReturnId().intValue();
    }
    public<T> int update(T obj,String col,String ope,Object val) throws SQLException {
        Class clazz = obj.getClass();
        UpdateWrapper wrapper=UpdateWrapper.build()
                .table(getTableName(clazz))
                .adds(toMap(obj))
                .link(SqlBase.LinkType.AND)
                .cond(col,ope,val)
                .done();
        return getBaseDao().updateCommon(wrapper);
    }
    public<T> int updateByPk(T obj) throws SQLException {
        String pkName= BeanAnnotationResolver.getBeanPrimaryKeyColumnName(obj);
        Object pkVal=BeanAnnotationResolver.getBeanPrimaryKeyValue(obj);
        return update(obj,pkName,"=",pkVal);
    }
    public <T> int update( T obj, String colName, Object colVal) throws SQLException {
        return update(obj,colName,"=",colVal);
    }

    public <T> int update(T obj, String colName, String colOpe) throws SQLException {
        Object val=ValueResolver.getVal(obj,colName,true);
        return update(obj,colName,colOpe, val);
    }

    public <T> int update(T obj, String colName) throws SQLException {
        Object val=ValueResolver.getVal(obj,colName,true);
        return update(obj,colName,"=", val);
    }

    public<T> int delete( Class<T> clazzBean, String colName, String colOpe, Object colVal) throws SQLException {
        DeleteWrapper wrapper=DeleteWrapper.build()
                .table(getTableName(clazzBean))
                .link(SqlBase.LinkType.AND)
                .cond(colName,colOpe,colVal)
                .done();
        return getBaseDao().deleteCommon(wrapper);
    }

    //需要注解支持
    public int deleteByPk( Class clazzBean, Object pkVal) throws SQLException {
        String pkName=BeanAnnotationResolver.getBeanPrimaryKeyColumnName(clazzBean);
        return delete(clazzBean,pkName,pkVal);
    }

    public int deleteByMultiKeys( Class clazzBean, Object ... pkVals) throws SQLException {
        String pkName=BeanAnnotationResolver.getBeanPrimaryKeyColumnName(clazzBean);
        DeleteWrapper wrapper=DeleteWrapper.build()
                .table(getTableName(clazzBean))
                .in(SqlBase.LinkType.AND,pkName,pkVals)
                .done();
        return getBaseDao().deleteCommon(wrapper);
    }

    public int delete(Class clazzBean, String colName, Object colVal) throws SQLException {
        return delete(clazzBean,colName,"=",colVal);
    }

    public<T> List<T> queryAll(Class<T> clazz,Integer pageIndex,Integer pageLimit,String ... queryCols) throws SQLException {
        QueryWrapper wrapper=QueryWrapper.build()
                .table(getTableName(clazz))
                .cols(queryCols)
                .page(pageIndex,pageLimit)
                .done();
        return parseMapList(getBaseDao().queryPage(wrapper).data.getDatas(),clazz);
    }

}
