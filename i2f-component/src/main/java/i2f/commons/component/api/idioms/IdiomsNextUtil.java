package i2f.commons.component.api.idioms;

import i2f.commons.component.api.idioms.data.IdiomsNextReqData;
import i2f.commons.component.api.idioms.data.IdiomsNextRespData;
import i2f.commons.component.api.idioms.data.IdiomsReqData;
import i2f.commons.component.http.HttpClientUtil;
import i2f.commons.component.json.gson.JsonUtil;
import i2f.commons.core.utils.reflect.core.resolver.BeanResolver;

import java.io.IOException;
import java.util.Map;

public class IdiomsNextUtil {
    public static String KEY="02609a607219a14611ce0281abc11ff8";

    public static IdiomsNextRespData getNext(String curIdiom) throws IOException {
        IdiomsNextReqData reqData=new IdiomsNextReqData();
        reqData.setKey(KEY);
        reqData.setWd(curIdiom);
        Map<String,Object> params= BeanResolver.toMap(reqData,true);
        String js= HttpClientUtil.doGet(IdiomsReqData.URL,params);
        IdiomsNextRespData ret= JsonUtil.fromJson(js,IdiomsNextRespData.class);
        return ret;
    }

}
