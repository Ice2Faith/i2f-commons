package i2f.commons.component.es.query;

import i2f.commons.core.utils.safe.CheckUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.io.IOException;
import java.util.Vector;

public class EsQryUtil {
    public static<T> Page<T> query(ElasticsearchRepository repDao, Query query){
        return repDao.search(query);
    }
    public static SearchResponse query(RestHighLevelClient client,String indexName,QueryBuilder query) throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(query);
        SearchRequest rq = new SearchRequest();
        //索引
        rq.indices(indexName);
        //各种组合条件
        rq.source(sourceBuilder);

        return client.search(rq, RequestOptions.DEFAULT);
    }
    /**
     * 查询构造器
     * 基于状态的构造器，根据先指定的状态，执行不同的状态下的构造
     * 用法：
     * BoolQueryBuilder builder=EsQryUtil.builder().
     * ..
     * .done();
     */
    public static class Builder {
        protected BoolQueryBuilder builder;
        public static final int MUST=1;
        public static final int MUST_NOT=2;
        public static final int SHOULD=3;
        public static final int FILTER=4;
        protected int state=MUST;
        public Builder(BoolQueryBuilder builder){
            this.builder=builder;
        }
        public Builder(){
            builder= qry();
        }

        /**
         * 调整为Must状态，之后再进行的条件都会以must添加
         * 直到下一次状态改变
         * @return
         */
        public Builder must(){
            this.state=MUST;
            return this;
        }
        public Builder mustNot(){
            this.state=MUST_NOT;
            return this;
        }
        public Builder should(){
            this.state=SHOULD;
            return this;
        }
        public Builder filter(){
            this.state=FILTER;
            return this;
        }
        public Builder range(String fieldName, Object from, Object to){
            QueryBuilder item= EsQryUtil.range(fieldName,from,to);
            return stateProxy(item);
        }
        public Builder eqs(String fieldName, Object ... vals){
            QueryBuilder item= EsQryUtil.eqs(fieldName, vals);
            return stateProxy(item);
        }
        public Builder eqsByStr(String fieldName, String val,String splitRegex){
            QueryBuilder item= EsQryUtil.eqsByStr(fieldName, val,splitRegex);
            return stateProxy(item);
        }
        public Builder likes(String fieldName, String ... vals){
            QueryBuilder item= EsQryUtil.likes(fieldName, vals);
            return stateProxy(item);
        }

        /**
         * 状态转换解析代理
         * @param item
         * @return
         */
        protected Builder stateProxy(QueryBuilder item){
            switch (this.state){
                case MUST:
                    EsQryUtil.must(builder,item);
                    break;
                case MUST_NOT:
                    EsQryUtil.mustNot(builder,item);
                    break;
                case SHOULD:
                    EsQryUtil.should(builder,item);
                    break;
                case FILTER:
                    EsQryUtil.filter(builder,item);
                    break;
                default:
                    break;
            }
            return this;
        }
        public BoolQueryBuilder done(){
            return builder;
        }

        /**
         * 转换为原始查询构造器，方便进行后续的构建
         * @return
         */
        public NativeBuilder convert(){
            return new NativeBuilder(builder);
        }
    }

    /**
     * 原始查询构造器
     * 主要用于添加分页和排序能力
     * 用法：
     * 一般在将基本查询构造器Builder构造完毕之后，调用Builder.convert()转换为此查询构造器
     * 继续使用本构造器的功能
     * 用法：
     * NativeSearchQueryBuilder qryIns= EsQryUtil.builder()
     * ..
     * .convert()
     * ..
     * .done();
     */
    public static class NativeBuilder{
        protected NativeSearchQueryBuilder builder;
        public NativeBuilder(){
            this.builder=new NativeSearchQueryBuilder();
        }
        public NativeBuilder(QueryBuilder builder){
            this.builder=new NativeSearchQueryBuilder()
                    .withQuery(builder);
        }

        /**
         * 如果分页信息合理，将会添加分页查询条件
         * 其中一个为null,都不添加分页查询条件
         * @param page
         * @param size
         * @return
         */
        public NativeBuilder page(Integer page,Integer size){
            if(!CheckUtil.isExNull(page,size)){
                Pageable pageable= PageRequest.of(page,size);
                this.builder.withPageable(pageable);
            }
            return this;
        }

        /**
         * 添加排序字段
         * @param fieldName
         * @param direct
         * @return
         */
        public NativeBuilder order(String fieldName, SortOrder direct){
            FieldSortBuilder sortBuilder= SortBuilders.fieldSort(fieldName)
                    .order(direct);
            this.builder.withSort(sortBuilder);
            return this;
        }

        public NativeSearchQueryBuilder done(){
            return this.builder;
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    /**
     * 范围查询
     * 支持from和to出现null
     * 出现null，则是半闭区间查询，否则是完全闭区间查询
     * 同时为null,则返回null,不进行构建，方便实现动态查询逻辑
     * @param fieldName
     * @param from
     * @param to
     * @return
     */
    public static QueryBuilder range(String fieldName,Object from,Object to){
        if(!CheckUtil.isExNull(from,to)){
            return QueryBuilders.rangeQuery(fieldName).from(from).to(to);
        }
        if(CheckUtil.notNull(from)){
            return QueryBuilders.rangeQuery(fieldName).gte(from);
        }
        if(CheckUtil.notNull(to)){
            return QueryBuilders.rangeQuery(fieldName).lte(to);
        }
        return null;
    }

    /**
     * 多个值相等查询，也就是IN语句
     * 当值的个数退化到1个时，也就是等值查询
     * @param fieldName
     * @param vals
     * @return
     */
    public static QueryBuilder eqs(String fieldName,Object ... vals){
        if(CheckUtil.isEmptyArray(vals)){
            return null;
        }
        Vector<Object> invals=new Vector<Object>();
        for(Object item : vals){
            if(item==null){
                continue;
            }
            if(item instanceof String){
                if(CheckUtil.isEmptyStr((String)item,false)){
                    continue;
                }
            }
            invals.add(item);
        }
        if(CheckUtil.isEmptyCollection(invals)){
            return null;
        }
        return QueryBuilders.termsQuery(fieldName, invals);
    }

    /**
     * 类似eqs,但是区别是，将会把val按照splitRegex指定的正则表达式进行拆分为多个，
     * 再进行条件构建
     * @param fieldName
     * @param val
     * @param splitRegex
     * @return
     */
    public static QueryBuilder eqsByStr(String fieldName,String val,String splitRegex){
        if(CheckUtil.isEmptyStr(val,false)){
            return null;
        }

        if(CheckUtil.isExNull(splitRegex)){
            splitRegex="\\s+";
        }
        String[] arr=val.split(splitRegex);
        Vector<String> strs=new Vector<String>();
        for(String item : arr){
            if(CheckUtil.isEmptyStr(item,false)){
                continue;
            }
            strs.add(item);
        }
        return eqs(fieldName, strs.toArray());
    }

    /**
     * 多个值的模糊查询，也就是Like语句
     * 当值的个数退化到1个时，也就是SQL中的like语句
     * @param fieldName
     * @param vals
     * @return
     */
    public static QueryBuilder likes(String fieldName,String ... vals){
        if(CheckUtil.isEmptyArray(vals)){
            return null;
        }
        BoolQueryBuilder queryBuilder=QueryBuilders.boolQuery();
        int count=0;
        for(String item : vals){
            if(CheckUtil.isEmptyStr(item,false)){
                continue;
            }
            queryBuilder.should(QueryBuilders.fuzzyQuery(fieldName, item));
            count++;
        }
        if(count==0){
            return null;
        }
        return queryBuilder;
    }

    /**
     * 分页
     * @param page 页索引
     * @param size 页大小
     * @return
     */
    public static Pageable page(int page,int size){
        return PageRequest.of(page, size);
    }

    /**
     * 构建一个符合查询对象
     * @return
     */
    public static BoolQueryBuilder qry(){
        return QueryBuilders.boolQuery();
    }

    /**
     * 动态检查，当builder不为null时，将会以must方式添加，作为组合查询条件
     * 否则不添加
     * @param boolBuilder
     * @param builder
     * @return
     */
    public static BoolQueryBuilder must(BoolQueryBuilder boolBuilder,QueryBuilder builder){
        if(CheckUtil.notNull(builder)){
            boolBuilder.must(builder);
        }
        return boolBuilder;
    }
    public static BoolQueryBuilder mustNot(BoolQueryBuilder boolBuilder,QueryBuilder builder){
        if(CheckUtil.notNull(builder)){
            boolBuilder.mustNot(builder);
        }
        return boolBuilder;
    }
    public static BoolQueryBuilder should(BoolQueryBuilder boolBuilder,QueryBuilder builder){
        if(CheckUtil.notNull(builder)){
            boolBuilder.should(builder);
        }
        return boolBuilder;
    }
    public static BoolQueryBuilder filter(BoolQueryBuilder boolBuilder,QueryBuilder builder){
        if(CheckUtil.notNull(builder)){
            boolBuilder.filter(builder);
        }
        return boolBuilder;
    }
}
