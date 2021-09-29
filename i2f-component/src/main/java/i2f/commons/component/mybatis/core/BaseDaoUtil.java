package i2f.commons.component.mybatis.core;

import i2f.commons.core.data.web.data.PageData;
import i2f.commons.core.utils.jdbc.type.DbType;
import i2f.commons.core.utils.jdbc.wrapper.*;
import i2f.commons.core.utils.jdbc.wrapper.base.SqlBase;
import i2f.commons.core.utils.jdbc.wrapper.base.SqlBaseWrapper;
import i2f.commons.core.utils.reflect.core.resolver.BeanAnnotationResolver;
import i2f.commons.core.utils.reflect.core.resolver.BeanResolver;
import i2f.commons.core.utils.reflect.core.resolver.base.ValueResolver;
import i2f.commons.core.utils.safe.CheckUtil;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class BaseDaoUtil {
    public static boolean openCheckBeanIgnoreFieldAnnotation=true;

    public static boolean openUseBeanAliasAnnotation=true;

    public static void setOpenCheckBeanIgnoreFieldAnnotation(boolean open) {
        BaseDaoUtil.openCheckBeanIgnoreFieldAnnotation = open;
    }

    public static String getTableName(Class clazz){
        if(openUseBeanAliasAnnotation){
            return BeanAnnotationResolver.getTableName(clazz);
        }
        return clazz.getSimpleName();
    }

    public static void tryRemovePk(Map<String,Object> kvs,Class clazz){
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

    public <T> T queryFillBeanForeignKeyFields(T obj) {
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

    public abstract BaseDao getBaseDao();

    public<T> Map<String,Object> toMap(T obj){
        return BeanResolver.toMap(obj,true,openCheckBeanIgnoreFieldAnnotation,openUseBeanAliasAnnotation);
    }

    public<T> List<T> parseMapList(List<Map<String,Object>> list,Class<T> clazz){
        return BeanResolver.parseMapList(list,clazz,true,openCheckBeanIgnoreFieldAnnotation,openUseBeanAliasAnnotation);
    }
    public<T> PageData<T> parse2PageData(List<T> data, Long count, QueryWrapper wrapper){
        if(count==null){
            count=((Integer)data.size()).longValue();
        }
        return new PageData<T>(data,wrapper.pageIndex,wrapper.pageLimit,count.intValue());
    }

    public static void setTableNameWhenNull(SqlBaseWrapper wrapper, Class clazz){
        if(CheckUtil.isEmptyStr(wrapper.tableName,true)){
            wrapper.tableName=getTableName(clazz);
        }
    }

    public<T> List<T> query(Class<T> clazz,QueryWrapper wrapper){
        setTableNameWhenNull(wrapper,clazz);
        List<Map<String,Object>> list=getBaseDao().queryCommon(wrapper);
        return parseMapList(list,clazz);
    }
    public<T> Long count(Class<T> clazz,QueryWrapper wrapper){
        setTableNameWhenNull(wrapper,clazz);
        return getBaseDao().queryCountCommon(wrapper);
    }
    public<T> PageData<T> page(Connection conn,Class<T> clazz, QueryWrapper wrapper) throws SQLException {
        setTableNameWhenNull(wrapper,clazz);
        List<Map<String,Object>> list=queryCommonPage(conn.getMetaData().getURL(),wrapper);
        return pageProxy(clazz,list,wrapper);
    }
    public<T> PageData<T> page(String jdbcUrl,Class<T> clazz, QueryWrapper wrapper) throws SQLException {
        setTableNameWhenNull(wrapper,clazz);
        List<Map<String,Object>> list=queryCommonPage(jdbcUrl,wrapper);
        return pageProxy(clazz,list,wrapper);
    }

    public<T> PageData<T> pageMysql(Class<T> clazz, QueryWrapper wrapper){
        setTableNameWhenNull(wrapper,clazz);
        List<Map<String,Object>> list=getBaseDao().queryCommonMySqlPage(wrapper);
        return pageProxy(clazz,list,wrapper);
    }
    public<T> PageData<T>  pageOracle(Class<T> clazz,QueryWrapper wrapper){
        setTableNameWhenNull(wrapper,clazz);
        List<Map<String,Object>> list=getBaseDao().queryCommOraclePage(wrapper);
        return pageProxy(clazz,list,wrapper);
    }

    public<T> PageData<T>  pageOracle12c(Class<T> clazz,QueryWrapper wrapper){
        setTableNameWhenNull(wrapper,clazz);
        List<Map<String,Object>> list=getBaseDao().queryCommonOracle12cPage(wrapper);
        return pageProxy(clazz,list,wrapper);
    }

    public<T> PageData<T>  pageGbase(Class<T> clazz,QueryWrapper wrapper){
        setTableNameWhenNull(wrapper,clazz);
        List<Map<String,Object>> list=getBaseDao().queryCommonGbasePage(wrapper);
        return pageProxy(clazz,list,wrapper);
    }

    protected<T> PageData<T> pageProxy(Class<T> clazz,List<Map<String,Object>> list,QueryWrapper wrapper){
        Long count=getBaseDao().queryCountCommon(wrapper);
        List<T> data=parseMapList(list,clazz);
        return parse2PageData(data,count,wrapper);
    }

    public List<Map<String,Object>> queryCommonPage(String jdbcUrl,QueryWrapper wrapper) throws SQLException {
        DbType type=DbType.typeOfJdbcUrl(jdbcUrl);
        switch (type){
            case MYSQL:
                return getBaseDao().queryCommonMySqlPage(wrapper);
            case ORACLE:
                return getBaseDao().queryCommOraclePage(wrapper);
            case MARIADB:
                return getBaseDao().queryCommonMariaDBPage(wrapper);
            case GBASE:
                return getBaseDao().queryCommonGbasePage(wrapper);
            case OSCAR:
                return getBaseDao().queryCommonOscarPage(wrapper);
            case XU_GU:
                return getBaseDao().queryCommonXuGuPage(wrapper);
            case CLICK_HOUSE:
                return getBaseDao().queryCommonClickHousePage(wrapper);
            case OCEAN_BASE:
                return getBaseDao().queryCommonOceanBasePage(wrapper);
            case POSTGRE_SQL:
                return getBaseDao().queryCommonPostgreSqlPage(wrapper);
            case H2:
                return getBaseDao().queryCommonH2Page(wrapper);
            case SQLITE:
                return getBaseDao().queryCommonSqlLitePage(wrapper);
            case HSQL:
                return getBaseDao().queryCommonHSqlPage(wrapper);
            case KINGBASE_ES:
                return getBaseDao().queryCommonKingBaseEsPage(wrapper);
            case PHOENIX:
                return getBaseDao().queryCommonPhoenixPage(wrapper);
            case DM:
                return getBaseDao().queryCommDmPage(wrapper);
            case GAUSS:
                return getBaseDao().queryCommGaussPage(wrapper);
            case ORACLE_12C:
                return getBaseDao().queryCommonOracle12cPage(wrapper);
            case DB2:
                return getBaseDao().queryCommonDb2Page(wrapper);
            case SQL_SERVER:
                return getBaseDao().queryCommonSQLServerPage(wrapper);
            default:
                throw new SQLException("unsupport auto page route db type");
        }
    }

    public<T> int delete(Class<T> clazz, DeleteWrapper wrapper){
        setTableNameWhenNull(wrapper,clazz);
        return getBaseDao().deleteCommon(wrapper);
    }

    public<T> int update(Class<T> clazz, UpdateWrapper wrapper){
        setTableNameWhenNull(wrapper,clazz);
        return getBaseDao().updateCommon(wrapper);
    }
    public<T> int insert(Class<T> clazz, InsertWrapper wrapper){
        setTableNameWhenNull(wrapper,clazz);
        return getBaseDao().insertCommon(wrapper);
    }
    public<T> int insertBatch(Class<T> clazz, InsertBatchWrapper wrapper){
        setTableNameWhenNull(wrapper,clazz);
        return getBaseDao().insertCommonBatch(wrapper);
    }
    public<T> long insertBatchLargeData(Class<T> clazz,InsertBatchWrapper wrapper){
        long startTime=System.currentTimeMillis();
        int singleMaxBatchInsertCount=4096;
        long successCount=0L;
        int batchTimes=0;
        BaseDao dao=getBaseDao();
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

    public<T> T queryOne(Class<T> clazz,QueryWrapper wrapper,String ... queryCols){
        setTableNameWhenNull(wrapper,clazz);
        QueryWrapper.build(wrapper).cols(queryCols);
        List<T> list=parseMapList(getBaseDao().queryCommon(wrapper),clazz);
        if(CheckUtil.notEmptyCollection(list)){
            return list.get(0);
        }
        return null;
    }
    public<T> List<T> queryByCol(Class<T> clazz,String colName,Object colVal,String ... queryCols){
        QueryWrapper wrapper=QueryWrapper.build()
                .table(getTableName(clazz))
                .cols(queryCols)
                .eq(SqlBase.LinkType.AND,colName,colVal)
                .done();
        return parseMapList(getBaseDao().queryCommon(wrapper),clazz);
    }
    public<T> T queryByPk(Class<T> clazz,String pkName,Object pkVal,String ... queryCols){
        List<T> list=queryByCol(clazz,pkName,pkVal,queryCols);
        if(CheckUtil.notEmptyCollection(list)){
            return list.get(0);
        }
        return null;
    }
    public<T> T queryByPkAnn(Class<T> clazz,Object pkVal,String ... queryCols){
        String pkName=BeanAnnotationResolver.getBeanPrimaryKeyColumnName(clazz);
        List<T> list=queryByCol(clazz,pkName,pkVal,queryCols);
        if(CheckUtil.notEmptyCollection(list)){
            return list.get(0);
        }
        return null;
    }
    public<T> int insert(T obj){
        Class clazz = obj.getClass();
        Map<String, Object> kvs = toMap(obj);
        InsertWrapper wrapper=InsertWrapper.build()
                .table(getTableName(clazz))
                .adds(kvs)
                .done();
        return getBaseDao().insertCommon(wrapper);
    }

    public <T> int insertReturnWithId( T obj) {
        Class clazz = obj.getClass();
        Map<String, Object> kvs = toMap(obj);
        InsertWrapper wrapper=InsertWrapper.build()
                .table(getTableName(clazz))
                .adds(kvs)
                .done();
        getBaseDao().insertCommonReturnId(wrapper);
        return wrapper.getReturnId().intValue();
    }

    public<T> int insertAutoPk(T obj){
        Class clazz = obj.getClass();
        Map<String, Object> kvs = toMap(obj);
        tryRemovePk(kvs,clazz);
        InsertWrapper wrapper=InsertWrapper.build()
                .table(getTableName(clazz))
                .adds(kvs)
                .done();
        return getBaseDao().insertCommon(wrapper);
    }

    public <T> int insertAutoPkReturnWithId(T obj) {
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
    public<T> int update(T obj,String col,String ope,Object val){
        Class clazz = obj.getClass();
        UpdateWrapper wrapper=UpdateWrapper.build()
                .table(getTableName(clazz))
                .adds(toMap(obj))
                .link(SqlBase.LinkType.AND)
                .cond(col,ope,val)
                .done();
        return getBaseDao().updateCommon(wrapper);
    }
    public<T> int updateByPk(T obj){
        String pkName= BeanAnnotationResolver.getBeanPrimaryKeyColumnName(obj);
        Object pkVal=BeanAnnotationResolver.getBeanPrimaryKeyValue(obj);
        return update(obj,pkName,"=",pkVal);
    }
    public <T> int update( T obj, String colName, Object colVal) {
        return update(obj,colName,"=",colVal);
    }

    public <T> int update(T obj, String colName, String colOpe) {
        Object val=ValueResolver.getVal(obj,colName,true);
        return update(obj,colName,colOpe, val);
    }

    public <T> int update(T obj, String colName) {
        Object val=ValueResolver.getVal(obj,colName,true);
        return update(obj,colName,"=", val);
    }

    public<T> int delete( Class<T> clazzBean, String colName, String colOpe, Object colVal) {
        DeleteWrapper wrapper=DeleteWrapper.build()
                .table(getTableName(clazzBean))
                .link(SqlBase.LinkType.AND)
                .cond(colName,colOpe,colVal)
                .done();
        return getBaseDao().deleteCommon(wrapper);
    }

    //需要注解支持
    public int deleteByPk( Class clazzBean, Object pkVal) {
        String pkName=BeanAnnotationResolver.getBeanPrimaryKeyColumnName(clazzBean);
        return delete(clazzBean,pkName,pkVal);
    }

    public int deleteByMultiKeys( Class clazzBean, Object ... pkVals) {
        String pkName=BeanAnnotationResolver.getBeanPrimaryKeyColumnName(clazzBean);
        DeleteWrapper wrapper=DeleteWrapper.build()
                .table(getTableName(clazzBean))
                .in(SqlBase.LinkType.AND,pkName,pkVals)
                .done();
        return getBaseDao().deleteCommon(wrapper);
    }

    public int delete(Class clazzBean, String colName, Object colVal) {
        return delete(clazzBean,colName,"=",colVal);
    }

    public<T> List<T> queryAllMySql(Class<T> clazz,Integer pageIndex,Integer pageLimit,String ... queryCols){
        QueryWrapper wrapper=QueryWrapper.build()
                .table(getTableName(clazz))
                .cols(queryCols)
                .page(pageIndex,pageLimit)
                .done();
        return parseMapList(getBaseDao().queryCommonMySqlPage(wrapper),clazz);
    }
    public<T> List<T> queryAllOracle(Class<T> clazz,Integer pageIndex,Integer pageLimit,String ... queryCols){
        QueryWrapper wrapper=QueryWrapper.build()
                .table(getTableName(clazz))
                .cols(queryCols)
                .page(pageIndex,pageLimit)
                .done();
        return parseMapList(getBaseDao().queryCommOraclePage(wrapper),clazz);
    }
}
