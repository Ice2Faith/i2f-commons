package i2f.commons.component.kuaidi100;

import i2f.commons.component.http.HttpClientUtil;
import i2f.commons.component.json.gson.JsonUtil;
import i2f.commons.component.kuaidi100.data.Kuaidi100ReqData;
import i2f.commons.component.kuaidi100.data.Kuaidi100RespData;
import i2f.commons.core.utils.reflect.core.resolver.BeanResolver;

import java.io.IOException;
import java.util.Map;

public class Kuaidi100Util {
    public static Kuaidi100RespData getKuaidiData(String mailNumber,String type) throws IOException {
        Kuaidi100ReqData reqData=new Kuaidi100ReqData();
        reqData.setPostid(mailNumber);
        reqData.setType(type);
        Map<String,Object> params= BeanResolver.toMap(reqData,true);
        String js= HttpClientUtil.doGet(Kuaidi100ReqData.URL,params);
        Kuaidi100RespData ret= JsonUtil.fromJson(js,Kuaidi100RespData.class);
        return ret;
    }
}
