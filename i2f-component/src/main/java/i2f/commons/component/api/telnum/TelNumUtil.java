package i2f.commons.component.api.telnum;

import i2f.commons.component.api.telnum.data.TelNumReqData;
import i2f.commons.component.api.telnum.data.TelNumRespData;
import i2f.commons.component.http.HttpClientUtil;
import i2f.commons.component.json.gson.JsonUtil;
import i2f.commons.core.utils.reflect.core.resolver.BeanResolver;

import java.io.IOException;
import java.util.Map;

//11-手机号码归属地
public class TelNumUtil {
    public static String KEY="03fc9691684e127763faf19cb127f28e";

    public static TelNumRespData getTelNumData(String tel) throws IOException {
        TelNumReqData reqData=new TelNumReqData();
        reqData.setKey(KEY);
        reqData.setPhone(tel);
        reqData.setDtype("json");
        Map<String,Object> params= BeanResolver.toMap(reqData,true);
        String js= HttpClientUtil.doGet(TelNumReqData.URL,params);
        TelNumRespData ret= JsonUtil.fromJson(js,TelNumRespData.class);
        return ret;
    }
}
