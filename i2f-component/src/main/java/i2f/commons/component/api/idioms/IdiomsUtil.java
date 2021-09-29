package i2f.commons.component.api.idioms;

import i2f.commons.component.api.idioms.data.IdiomsReqData;
import i2f.commons.component.api.idioms.data.IdiomsRespData;
import i2f.commons.component.http.HttpClientUtil;
import i2f.commons.component.json.gson.JsonUtil;
import i2f.commons.core.utils.reflect.core.resolver.BeanResolver;

import java.io.IOException;
import java.util.Map;

//159-成语大全
public class IdiomsUtil {
    public static String KEY="f21b540f0139555ac13bc4c3a85df260";

    public static IdiomsRespData getIdiomsData(String idioms) throws IOException {
        IdiomsReqData reqData=new IdiomsReqData();
        reqData.setKey(KEY);
        reqData.setWd(idioms);
        Map<String,Object> params= BeanResolver.toMap(reqData,true);
        String js= HttpClientUtil.doGet(IdiomsReqData.URL,params);
        IdiomsRespData ret= JsonUtil.fromJson(js,IdiomsRespData.class);
        return ret;
    }

}
