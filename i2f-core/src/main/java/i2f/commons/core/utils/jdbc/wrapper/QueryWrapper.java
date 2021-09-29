package i2f.commons.core.utils.jdbc.wrapper;


import i2f.commons.core.data.Pair;
import i2f.commons.core.data.Triple;
import i2f.commons.core.utils.jdbc.wrapper.base.SqlCondBaseWrapper;
import i2f.commons.core.utils.jdbc.wrapper.base.SqlCondBaseWrapperBuilder;
import i2f.commons.core.utils.safe.CheckUtil;
import i2f.commons.core.utils.str.AppendUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class QueryWrapper extends SqlCondBaseWrapper {
    public List<String> queryCols=new ArrayList<String>();
    //left|inner|right table condition:left,User,Role.id=User.roleId
    public List<Triple<String,String,String>> joinPart=new ArrayList<Triple<String,String,String>>();
    //col:age
    public List<String> groupBy=new ArrayList<String>();
    //and|or colCond colVal:and,age>=,12
    public List<Triple<String,String,Object>> havingConditions=new ArrayList<Triple<String,String,Object>>();
    //col desc|asc
    public List<Pair<String,String>> orderBy=new ArrayList<Pair<String,String>>();
    //for mysql page query
    public Integer pageIndex;
    public Integer pageLimit;

    public Long pageOffset;
    public Long pageOffsetEnd;

    public static WrapperBuilder build(){
        return new WrapperBuilder();
    }
    public static WrapperBuilder build(QueryWrapper wrapper){
        return new WrapperBuilder(wrapper);
    }

    public static class WrapperBuilder {
        private SqlCondBaseWrapperBuilder baseWrapper;
        private QueryWrapper wrapper;
        private JoinType joinType=JoinType.INNER;
        private SortType sortType=SortType.ASC;
        public WrapperBuilder(){
            wrapper=new QueryWrapper();
            baseWrapper=new SqlCondBaseWrapperBuilder(wrapper);
        }
        public WrapperBuilder(QueryWrapper wrapper){
            this.wrapper=wrapper;
            baseWrapper=new SqlCondBaseWrapperBuilder(wrapper);
        }
        public QueryWrapper done(){
            return this.wrapper;
        }



        public WrapperBuilder sort(SortType sortType){
            this.sortType=sortType;
            return this;
        }
        public WrapperBuilder join(JoinType joinType){
            this.joinType=joinType;
            return this;
        }
        public WrapperBuilder asc(){
            return sort(SortType.ASC);
        }
        public WrapperBuilder desc(){
            return sort(SortType.DESC);
        }
        public WrapperBuilder inner(){
            return join(JoinType.INNER);
        }
        public WrapperBuilder left(){
            return join(JoinType.LEFT);
        }
        public WrapperBuilder right(){
            return join(JoinType.RIGHT);
        }
        public WrapperBuilder outer(){
            return join(JoinType.OUTER);
        }

        public WrapperBuilder order(String col,SortType type){
            Pair<String,String> order=new Pair<String,String>();
            order.key=col;
            order.val=type.sort();
            wrapper.orderBy.add(order);
            return this;
        }

        public WrapperBuilder order(String col){
            return order(col,sortType);
        }

        public WrapperBuilder asc(String col){
            return order(col,SortType.ASC);
        }
        public WrapperBuilder desc(String col){
            return order(col,SortType.DESC);
        }


        public WrapperBuilder countCol(String alias){
            return col("count(*)",alias);
        }
        public WrapperBuilder col(String col){
            wrapper.queryCols.add(col);
            return this;
        }
        public WrapperBuilder allCol(){
            return col("*");
        }
        public WrapperBuilder col(String col,String alias){
            wrapper.queryCols.add(
                    AppendUtil.strSep(" ",col,alias)
            );
            return this;
        }
        public WrapperBuilder cols(String ... cols){
            if(CheckUtil.isEmptyArray(cols)){
                return this;
            }
            for(String item : cols){
                wrapper.queryCols.add(item);
            }
            return this;
        }
        public WrapperBuilder join(JoinType joinType,String joinTable,String condition){
            Triple<String,String,String> join=new Triple<String,String,String>();
            join.fst=joinType.type();
            join.sec=joinTable;
            join.trd=condition;
            wrapper.joinPart.add(join);
            return this;
        }
        public WrapperBuilder join(String joinTable,String condition){
            return join(this.joinType,joinTable,condition);
        }
        public WrapperBuilder join(JoinType joinType,Class joinTable,String condition){
            Triple<String,String,String> join=new Triple<String,String,String>();
            join.fst=joinType.type();
            join.sec=joinTable.getSimpleName();
            join.trd=condition;
            wrapper.joinPart.add(join);
            return this;
        }
        public WrapperBuilder join(Class joinTable,String condition){
            return join(this.joinType,joinTable,condition);
        }

        public WrapperBuilder group(String ... cols){
            if(CheckUtil.isEmptyArray(cols)){
                return this;
            }
            for(String item : cols){
                wrapper.groupBy.add(item);
            }
            return this;
        }
        public WrapperBuilder hvCond(LinkType linkType,String colOpe,Object val){
            Triple<String,String,Object> join=new Triple<String,String,Object>();
            join.fst=linkType.linker();
            join.sec=colOpe;
            join.trd=val;
            wrapper.havingConditions.add(join);
            return this;
        }
        public WrapperBuilder hvCond(String colOpe,Object val){
            return hvCond(baseWrapper.linkType,colOpe,val);
        }
        public WrapperBuilder hvEq(LinkType linkType,String col,Object val){
            return hvCond(linkType,col+" = ",val);
        }
        public WrapperBuilder hvEq(String col,Object val){
            return hvEq(baseWrapper.linkType,col,val);
        }
        public WrapperBuilder hvGt(LinkType linkType,String col,Object val){
            return hvCond(linkType,col+" > ",val);
        }
        public WrapperBuilder hvGt(String col,Object val){
            return hvGt(baseWrapper.linkType,col,val);
        }
        public WrapperBuilder hvGte(LinkType linkType,String col,Object val){
            return hvCond(linkType,col+" >= ",val);
        }
        public WrapperBuilder hvGte(String col,Object val){
            return hvGte(baseWrapper.linkType,col,val);
        }
        public WrapperBuilder hvLt(LinkType linkType,String col,Object val){
            return hvCond(linkType,col+" < ",val);
        }
        public WrapperBuilder hvLt(String col,Object val){
            return hvLt(baseWrapper.linkType,col,val);
        }
        public WrapperBuilder hvLte(LinkType linkType,String col,Object val){
            return hvCond(linkType,col+" <= ",val);
        }
        public WrapperBuilder hvLte(String col,Object val){
            return hvLte(baseWrapper.linkType,col,val);
        }
        public WrapperBuilder hvNeq(LinkType linkType,String col,Object val){
            return hvCond(linkType,col+" <> ",val);
        }
        public WrapperBuilder hvNeq(String col,Object val){
            return hvNeq(baseWrapper.linkType,col,val);
        }

        public WrapperBuilder page(Integer index,Integer size){
            if(index!=null && size!=null){
                wrapper.pageOffset=index*size*1L;
                wrapper.pageOffsetEnd=(index+1)*size*1L;
            }
            wrapper.pageIndex=index;
            wrapper.pageLimit=size;
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
        public WrapperBuilder link(LinkType linkType){
            baseWrapper.link(linkType);
            return this;
        }

        public WrapperBuilder and(){
            baseWrapper.and();
            return this;
        }
        public WrapperBuilder or(){
            baseWrapper.or();
            return this;
        }

        public WrapperBuilder ope(String operater){
            baseWrapper.ope(operater);
            return this;
        }

        public WrapperBuilder eq(){
            baseWrapper.eq();
            return this;
        }
        public WrapperBuilder gt(){
            baseWrapper.gt();
            return this;
        }
        public WrapperBuilder lt(){
            baseWrapper.lt();
            return this;
        }
        public WrapperBuilder gte(){
            baseWrapper.gte();
            return this;
        }
        public WrapperBuilder lte(){
            baseWrapper.lte();
            return this;
        }
        public WrapperBuilder neq(){
            baseWrapper.neq();
            return this;
        }
        public WrapperBuilder like(){
            baseWrapper.like();
            return this;
        }
        public WrapperBuilder in(){
            baseWrapper.in();
            return this;
        }


        public WrapperBuilder cond(LinkType linkType, String col, String operator, Object val){
            baseWrapper.cond(linkType, col, operator, val);
            return this;
        }
        public WrapperBuilder cond(String col, String operator, Object val){
            baseWrapper.cond(col, operator, val);
            return this;
        }
        public WrapperBuilder cond(String col, Object val){
            baseWrapper.cond(col, val);
            return this;
        }
        public WrapperBuilder conds(LinkType linkType, String operator, Map<String,Object> kvs){
            baseWrapper.conds(linkType, operator, kvs);
            return this;
        }
        public WrapperBuilder conds(String operater, Map<String,Object> kvs){
            baseWrapper.conds(operater, kvs);
            return this;
        }
        public WrapperBuilder conds(Map<String,Object> kvs){
            baseWrapper.conds(kvs);
            return this;
        }
        public WrapperBuilder eq(LinkType linkType, String col, Object val){
            baseWrapper.eq(linkType, col, val);
            return this;
        }
        public WrapperBuilder eq(String col, Object val){
            baseWrapper.eq(col, val);
            return this;
        }
        public WrapperBuilder eqs(LinkType linkType, Map<String,Object> kvs){
            baseWrapper.eqs(linkType, kvs);
            return this;
        }
        public WrapperBuilder eqs(Map<String,Object> kvs){
            baseWrapper.eqs(kvs);
            return this;
        }
        public WrapperBuilder gt(LinkType linkType, String col, Object val){
            baseWrapper.gt(linkType, col, val);
            return this;
        }
        public WrapperBuilder gt(String col, Object val){
            baseWrapper.gt(col, val);
            return this;
        }
        public WrapperBuilder gte(LinkType linkType, String col, Object val){
            baseWrapper.gte(linkType, col, val);
            return this;
        }
        public WrapperBuilder gte(String col, Object val){
            baseWrapper.gte(col, val);
            return this;
        }
        public WrapperBuilder lt(LinkType linkType, String col, Object val){
            baseWrapper.lt(linkType, col, val);
            return this;
        }
        public WrapperBuilder lt(String col, Object val){
            baseWrapper.lt(col, val);
            return this;
        }
        public WrapperBuilder lte(LinkType linkType, String col, Object val){
            baseWrapper.lte(linkType, col, val);
            return this;
        }
        public WrapperBuilder lte(String col, Object val){
            baseWrapper.lte(col, val);
            return this;
        }
        public WrapperBuilder neq(LinkType linkType, String col, Object val){
            baseWrapper.neq(linkType, col, val);
            return this;
        }
        public WrapperBuilder neq(String col, Object val){
            baseWrapper.neq(col, val);
            return this;
        }
        public WrapperBuilder in(LinkType linkType, String col, Object ... vals){
            baseWrapper.in(linkType, col, vals);
            return this;
        }
        public WrapperBuilder in(String col, Object ... vals){
            baseWrapper.in(col, vals);
            return this;
        }
        public WrapperBuilder like(LinkType linkType, String col, Object ... vals){
            baseWrapper.like(linkType, col, vals);
            return this;
        }
        public WrapperBuilder like(String col, Object ... vals){
            baseWrapper.like(col, vals);
            return this;
        }
        public WrapperBuilder likes(LinkType linkType, String[] cols, Object ... vals){
            baseWrapper.likes(linkType, cols, vals);
            return this;
        }
        public WrapperBuilder likes(String[] cols, Object ... vals){
            baseWrapper.likes(cols, vals);
            return this;
        }
        public WrapperBuilder freeCond(LinkType linkType, String condition){
            baseWrapper.freeCond(linkType, condition);
            return this;
        }
        public WrapperBuilder freeCond(String condition){
            baseWrapper.freeCond(condition);
            return this;
        }
        public WrapperBuilder multiLikes(LinkType linkType, String[] cols, Object ... vals){
            baseWrapper.multiLikes(linkType, cols, vals);
            return this;
        }
        public WrapperBuilder multiLikes(String[] cols, Object ... vals){
            baseWrapper.multiLikes(cols, vals);
            return this;
        }

    }
}
