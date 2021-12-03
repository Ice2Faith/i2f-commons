package i2f.commons.component.es.query;

import i2f.commons.component.es.query.annotation.EsField;
import i2f.commons.component.es.query.annotation.EsId;
import i2f.commons.component.es.query.annotation.EsIndex;
import i2f.commons.component.es.query.annotation.EsQuery;
import i2f.commons.component.es.query.enums.EsQueryType;
import i2f.commons.component.json.jackson.JsonUtil;
import i2f.commons.core.utils.reflect.core.resolver.base.ClassResolver;
import i2f.commons.core.utils.reflect.core.resolver.base.FieldResolver;
import i2f.commons.core.utils.reflect.core.resolver.base.ValueResolver;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class EsBeanUtil {

    public static String resolveIndexName(Class clazz){
        EsIndex idx=(EsIndex) clazz.getAnnotation(EsIndex.class);
        String idxName = clazz.getSimpleName();
        if(idx!=null){
            if(idx.value()!=null && !"".equals(idx.value())){
                idxName=idx.value();
            }
        }
        return idxName;
    }

    public static boolean indexExist(RestHighLevelClient client,Class clazz) throws IOException {
       String idxName=resolveIndexName(clazz);
       return EsUtil.indexExist(client,idxName);
    }

    public static CreateIndexResponse indexCreate(RestHighLevelClient client,Class clazz) throws IOException {
        String idxName=resolveIndexName(clazz);
        return EsUtil.indexCreate(client,idxName);
    }

    public static boolean documentInsert(RestHighLevelClient client,Object obj) throws IOException {
        if(obj==null){
            return false;
        }
        Class clazz=obj.getClass();
        String indexName=resolveIndexName(clazz);
        String id="";
        String json="";

        Set<Field> fields= FieldResolver.getAllFields(clazz,true);
        Map<String,Object> record=new HashMap<>();
        for(Field item : fields){
            String fieldName= item.getName();
            boolean isId=false;
            EsId eiann=item.getAnnotation(EsId.class);
            if(eiann!=null){
                if(eiann.value()){
                    isId=true;
                }
            }
            EsField efann=item.getAnnotation(EsField.class);
            if(efann!=null){
                if(!efann.value()){
                    if(eiann==null){
                        continue;
                    }
                }
                if(efann.alias()!=null && !"".equals(efann.alias())){
                    fieldName=efann.alias();
                }
            }
            Object val= ValueResolver.getVal(obj,clazz,item.getName(),false);
            if(isId){
                id=String.valueOf(val);
            }
            record.put(fieldName,val);
        }

        json=JsonUtil.toJson(record);

        return EsUtil.documentInsert(client,indexName,id,json);
    }

    public static<T> List<T> documentQuery(RestHighLevelClient client, Object queryBean,Class indexClazz, Class<T> returnType) throws IOException {
        String indexName=resolveIndexName(indexClazz);
        EsQryUtil.Builder builder=EsQryUtil.builder();

        Class clazz=queryBean.getClass();

        Set<Field> fields= FieldResolver.getAllFields(clazz,true);
        Map<String,Object> record=new HashMap<>();
        for(Field item : fields){
            String fieldName= item.getName();
            EsQuery eqann=item.getAnnotation(EsQuery.class);
            if(eqann==null){
                continue;
            }
            if(eqann.alias()!=null && !"".equals(eqann.alias())){
                fieldName= eqann.alias();
            }

            Object val= ValueResolver.getVal(queryBean,clazz,item.getName(),false);
            EsQueryType type=eqann.type();

            if(type==EsQueryType.EQ){
                builder.eqs(fieldName,val);
            }else if(type==EsQueryType.LIKE){
                builder.likes(fieldName,String.valueOf(val));
            }else if(type==EsQueryType.LTE){
                builder.range(fieldName,null,val);
            }else if(type==EsQueryType.GTE){
                builder.range(fieldName,val,null);
            }
        }


        QueryBuilder query=builder.done();

        SearchResponse resp=EsQryUtil.query(client,indexName,query);
        if(resp==null){
            return new ArrayList<>();
        }
        SearchHits hits=resp.getHits();
        if(hits==null){
            return new ArrayList<>();
        }

        SearchHit[] list= hits.getHits();
        List<T> ret=new ArrayList<>(list.length);
        for(SearchHit item : list){
            Map<String,Object> map=item.getSourceAsMap();
            clazz=returnType;
            fields= FieldResolver.getAllFields(clazz,true);
            T one= ClassResolver.instance(returnType);
            for(Field field : fields){
                String fieldName= field.getName();

                EsField efann=field.getAnnotation(EsField.class);
                if(efann!=null){
                    if(efann.alias()!=null && !"".equals(efann.alias())){
                        fieldName=efann.alias();
                    }
                }
                if(map.containsKey(fieldName)){
                    Object val=map.get(fieldName);
                    ValueResolver.setVal(one,field.getName(),val,false);
                }

            }
            ret.add(one);
        }

        return ret;
    }

}
