package i2f.commons.core.utils.jdbc.wrapper;


import i2f.commons.core.utils.jdbc.wrapper.base.SqlCondBaseWrapper;
import i2f.commons.core.utils.jdbc.wrapper.base.SqlCondBaseWrapperBuilder;
import lombok.Data;

import java.util.Map;

@Data
public class DeleteWrapper extends SqlCondBaseWrapper {

    public static WrapperBuilder build(){
        return new WrapperBuilder();
    }
    public static WrapperBuilder build(DeleteWrapper wrapper){
        return new WrapperBuilder(wrapper);
    }

    public static class WrapperBuilder {
        private SqlCondBaseWrapperBuilder baseWrapper;
        private DeleteWrapper wrapper;
        public WrapperBuilder(){
            wrapper=new DeleteWrapper();
            baseWrapper=new SqlCondBaseWrapperBuilder(wrapper);
        }
        public WrapperBuilder(DeleteWrapper wrapper){
            this.wrapper=wrapper;
            baseWrapper=new SqlCondBaseWrapperBuilder(wrapper);
        }
        public DeleteWrapper done(){
            return this.wrapper;
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


        public WrapperBuilder cond(LinkType linkType, String col,String operator, Object val){
            baseWrapper.cond(linkType, col, operator, val);
            return this;
        }
        public WrapperBuilder cond(String col,String operator, Object val){
            baseWrapper.cond(col, operator, val);
            return this;
        }
        public WrapperBuilder cond(String col,Object val){
            baseWrapper.cond(col, val);
            return this;
        }
        public WrapperBuilder conds(LinkType linkType, String operator, Map<String,Object> kvs){
            baseWrapper.conds(linkType, operator, kvs);
            return this;
        }
        public WrapperBuilder conds(String operater,Map<String,Object> kvs){
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
        public WrapperBuilder eq(String col,Object val){
            baseWrapper.eq(col, val);
            return this;
        }
        public WrapperBuilder eqs(LinkType linkType,Map<String,Object> kvs){
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
        public WrapperBuilder gt(String col,Object val){
            baseWrapper.gt(col, val);
            return this;
        }
        public WrapperBuilder gte(LinkType linkType, String col, Object val){
            baseWrapper.gte(linkType, col, val);
            return this;
        }
        public WrapperBuilder gte(String col,Object val){
            baseWrapper.gte(col, val);
            return this;
        }
        public WrapperBuilder lt(LinkType linkType, String col, Object val){
            baseWrapper.lt(linkType, col, val);
            return this;
        }
        public WrapperBuilder lt(String col,Object val){
            baseWrapper.lt(col, val);
            return this;
        }
        public WrapperBuilder lte(LinkType linkType, String col, Object val){
            baseWrapper.lte(linkType, col, val);
            return this;
        }
        public WrapperBuilder lte(String col,Object val){
            baseWrapper.lte(col, val);
            return this;
        }
        public WrapperBuilder neq(LinkType linkType, String col, Object val){
            baseWrapper.neq(linkType, col, val);
            return this;
        }
        public WrapperBuilder neq(String col,Object val){
            baseWrapper.neq(col, val);
            return this;
        }
        public WrapperBuilder in(LinkType linkType, String col, Object ... vals){
            baseWrapper.in(linkType, col, vals);
            return this;
        }
        public WrapperBuilder in(String col,Object ... vals){
            baseWrapper.in(col, vals);
            return this;
        }
        public WrapperBuilder like(LinkType linkType, String col, Object ... vals){
            baseWrapper.like(linkType, col, vals);
            return this;
        }
        public WrapperBuilder like(String col,Object ... vals){
            baseWrapper.like(col, vals);
            return this;
        }
        public WrapperBuilder likes(LinkType linkType, String[] cols, Object ... vals){
            baseWrapper.likes(linkType, cols, vals);
            return this;
        }
        public WrapperBuilder likes(String[] cols,Object ... vals){
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
        public WrapperBuilder multiLikes(String[] cols,Object ... vals){
            baseWrapper.multiLikes(cols, vals);
            return this;
        }
    }
}
