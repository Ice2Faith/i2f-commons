package i2f.commons.core.utils.jdbc.wrapper.base;


import i2f.commons.core.data.Pair;
import i2f.commons.core.data.Triple;
import i2f.commons.core.utils.safe.CheckUtil;

import java.util.HashMap;
import java.util.Map;

public class SqlCondBaseWrapperBuilder extends SqlBase{
    public LinkType linkType= LinkType.AND;
    public String operater="=";
    private SqlCondBaseWrapper wrapper;
    private SqlBaseWrapperBuilder baseWrapper;
    public SqlCondBaseWrapperBuilder(SqlCondBaseWrapper wrapper){
        this.wrapper=wrapper;
        baseWrapper=new SqlBaseWrapperBuilder(wrapper);
    }
    public SqlCondBaseWrapperBuilder table(String tableName){
        baseWrapper.table(tableName);
        return this;
    }
    public SqlCondBaseWrapperBuilder table(Class clazz, String prefix, String suffix){
        baseWrapper.table(clazz, prefix, suffix);
        return this;
    }
    public SqlCondBaseWrapperBuilder link(LinkType linkType){
        this.linkType=linkType;
        return this;
    }

    public SqlCondBaseWrapperBuilder and(){
        return link(LinkType.AND);
    }
    public SqlCondBaseWrapperBuilder or(){
        return link(LinkType.OR);
    }

    public SqlCondBaseWrapperBuilder ope(String operater){
        this.operater=operater;
        return this;
    }

    public SqlCondBaseWrapperBuilder eq(){
        return ope("=");
    }
    public SqlCondBaseWrapperBuilder gt(){
        return ope(">");
    }
    public SqlCondBaseWrapperBuilder lt(){
        return ope("<");
    }
    public SqlCondBaseWrapperBuilder gte(){
        return ope(">=");
    }
    public SqlCondBaseWrapperBuilder lte(){
        return ope("<=");
    }
    public SqlCondBaseWrapperBuilder neq(){
        return ope("!=");
    }
    public SqlCondBaseWrapperBuilder like(){
        return ope("like");
    }
    public SqlCondBaseWrapperBuilder in(){
        return ope("in");
    }


    public SqlCondBaseWrapperBuilder cond(LinkType linkType, String col,String operator, Object val){
        if(val==null){
            return this;
        }
        String sval=String.valueOf(val);
        if(CheckUtil.isEmptyStr(sval,false)){
            return this;
        }
        Triple<String,String,Object> join=new Triple<String,String,Object>();
        join.fst=linkType.linker();
        join.sec=col+" "+operator+" ";
        join.trd=val;
        wrapper.whereConditions.add(join);
        return this;
    }
    public SqlCondBaseWrapperBuilder cond(String col,String operator, Object val){
        return cond(linkType,col,operator,val);
    }
    public SqlCondBaseWrapperBuilder cond(String col,Object val){
        return cond(linkType,col,operater,val);
    }
    public SqlCondBaseWrapperBuilder conds(LinkType linkType,String operator,Map<String,Object> kvs){
        if(CheckUtil.isEmptyMap(kvs)){
            return this;
        }
        for(String item : kvs.keySet()){
            cond(linkType,item,"=",kvs.get(item));
        }
        return this;
    }
    public SqlCondBaseWrapperBuilder conds(String operater,Map<String,Object> kvs){
        return conds(this.linkType,operater,kvs);
    }
    public SqlCondBaseWrapperBuilder conds(Map<String,Object> kvs){
        return conds(this.linkType,this.operater,kvs);
    }
    public SqlCondBaseWrapperBuilder eq(LinkType linkType, String col, Object val){
        return cond(linkType,col,"=",val);
    }
    public SqlCondBaseWrapperBuilder eq(String col,Object val){
        return eq(this.linkType,col,val);
    }
    public SqlCondBaseWrapperBuilder eqs(LinkType linkType,Map<String,Object> kvs){
        return conds(linkType,"=",kvs);
    }
    public SqlCondBaseWrapperBuilder eqs(Map<String,Object> kvs){
        return eqs(this.linkType,kvs);
    }
    public SqlCondBaseWrapperBuilder gt(LinkType linkType, String col, Object val){
        return cond(linkType,col,">",val);
    }
    public SqlCondBaseWrapperBuilder gt(String col,Object val){
        return gt(this.linkType,col,val);
    }
    public SqlCondBaseWrapperBuilder gte(LinkType linkType, String col, Object val){
        return cond(linkType,col,">=",val);
    }
    public SqlCondBaseWrapperBuilder gte(String col,Object val){
        return gte(this.linkType,col,val);
    }
    public SqlCondBaseWrapperBuilder lt(LinkType linkType, String col, Object val){
        return cond(linkType,col,"<",val);
    }
    public SqlCondBaseWrapperBuilder lt(String col,Object val){
        return lt(this.linkType,col,val);
    }
    public SqlCondBaseWrapperBuilder lte(LinkType linkType, String col, Object val){
        return cond(linkType,col,"<=",val);
    }
    public SqlCondBaseWrapperBuilder lte(String col,Object val){
        return lte(this.linkType,col,val);
    }
    public SqlCondBaseWrapperBuilder neq(LinkType linkType, String col, Object val){
        return cond(linkType,col,"<>",val);
    }
    public SqlCondBaseWrapperBuilder neq(String col,Object val){
        return neq(this.linkType,col,val);
    }
    public SqlCondBaseWrapperBuilder in(LinkType linkType, String col, Object ... vals){
        if(CheckUtil.isEmptyArray(vals)){
            return this;
        }
        Triple<String,String,Object[]> join=new Triple<String,String,Object[]>();
        join.fst=linkType.linker();
        join.sec=col;
        join.trd=vals;
        wrapper.inWhereCondition.add(join);
        return this;
    }
    public SqlCondBaseWrapperBuilder in(String col,Object ... vals){
        return in(col,vals);
    }
    public SqlCondBaseWrapperBuilder like(LinkType linkType, String col, Object ... vals){
        if(CheckUtil.isEmptyArray(vals)){
            return this;
        }
        Triple<String,String,Object[]> join=new Triple<String,String,Object[]>();
        join.fst=linkType.linker();
        join.sec=col;
        join.trd=vals;
        wrapper.likeWhereCondition.add(join);
        return this;
    }
    public SqlCondBaseWrapperBuilder like(String col,Object ... vals){
        return like(this.linkType,col,vals);
    }
    public SqlCondBaseWrapperBuilder likes(LinkType linkType, String[] cols, Object ... vals){
        if(CheckUtil.isEmptyArray(vals)){
            return this;
        }
        if(CheckUtil.isEmptyArray(cols)){
            return this;
        }
        for(String col : cols){
            Triple<String,String,Object[]> join=new Triple<String,String,Object[]>();
            join.fst=linkType.linker();
            join.sec=col;
            join.trd=vals;
            wrapper.likeWhereCondition.add(join);
        }
        return this;
    }
    public SqlCondBaseWrapperBuilder likes(String[] cols,Object ... vals){
        return likes(this.linkType,cols,vals);
    }
    public SqlCondBaseWrapperBuilder freeCond(LinkType linkType, String condition){
        Pair<String,String> cond=new Pair<String,String>();
        cond.key=linkType.linker();
        cond.val=condition;
        wrapper.freeWhereCondition.add(cond);
        return this;
    }
    public SqlCondBaseWrapperBuilder freeCond(String condition){
        return freeCond(this.linkType,condition);
    }
    public SqlCondBaseWrapperBuilder multiLikes(LinkType linkType, String[] cols, Object ... vals){
        if(CheckUtil.isEmptyArray(vals)){
            return this;
        }
        if(CheckUtil.isEmptyArray(cols)){
            return this;
        }
        Pair<String, Map<String,Object[]>> pair=new Pair<String, Map<String,Object[]>>();
        pair.key=linkType.linker();
        Map<String,Object[]> val=new HashMap<String,Object[]>();
        for(String item : cols){
            val.put(item,vals);
        }
        pair.val=val;
        wrapper.multiLikeWhereCondition.add(pair);
        return this;
    }
    public SqlCondBaseWrapperBuilder multiLikes(String[] cols,Object ... vals){
        return multiLikes(this.linkType,cols,vals);
    }
}
