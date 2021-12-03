package i2f.commons.component.es.query;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

public class EsUtil {
    public static  boolean indexExist(RestHighLevelClient client,String indexName) throws IOException {
        if(indexName==null || "".equals(indexName)){
            return false;
        }
        GetIndexRequest request=new GetIndexRequest()
                .indices(indexName);
        return client.indices().exists(request, RequestOptions.DEFAULT);
    }

    public static  CreateIndexResponse indexCreate(RestHighLevelClient client,String indexName) throws IOException {
        CreateIndexRequest request=new CreateIndexRequest(indexName);
        CreateIndexResponse response=client.indices().create(request,RequestOptions.DEFAULT);
        return response;
    }

    public static boolean documentInsert(RestHighLevelClient client,String indexName,String id,String json) throws IOException {
        IndexRequest request=new IndexRequest(indexName);
        request.id(id);
        request.source(json, XContentType.JSON);

        BulkRequest blk=new BulkRequest();
        blk.add(request);

        blk.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        BulkResponse blkResp=client.bulk(blk,RequestOptions.DEFAULT);
        return !blkResp.hasFailures();
    }
}
