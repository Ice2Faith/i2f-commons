package i2f.commons.core.utils.jdbc.wrapper.base;


import i2f.commons.core.utils.str.AppendUtil;

public class SqlBaseWrapperBuilder extends SqlBase{
    private SqlBaseWrapper wrapper;
    public SqlBaseWrapperBuilder(){

    }
    public SqlBaseWrapperBuilder(SqlBaseWrapper wrapper){
        this.wrapper=wrapper;
    }
    public SqlBaseWrapperBuilder setWrapper(SqlBaseWrapper wrapper){
        this.wrapper=wrapper;
        return this;
    }
    public SqlBaseWrapperBuilder table(String tableName){
        wrapper.tableName=tableName;
        return this;
    }
    public SqlBaseWrapperBuilder table(Class clazz, String prefix, String suffix){
        wrapper.tableName= AppendUtil.buffer()
                .appends(true,false,null,prefix,suffix,clazz.getSimpleName())
                .done();
        return this;
    }
}
