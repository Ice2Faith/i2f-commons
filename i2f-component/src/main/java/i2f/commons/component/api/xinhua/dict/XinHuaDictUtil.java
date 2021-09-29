package i2f.commons.component.api.xinhua.dict;

import i2f.commons.component.api.xinhua.dict.data.XinHuaDictReqData;
import i2f.commons.component.api.xinhua.dict.data.XinHuaDictRespData;
import i2f.commons.component.http.HttpClientUtil;
import i2f.commons.component.json.gson.JsonUtil;
import i2f.commons.core.utils.reflect.core.resolver.BeanResolver;

import java.io.IOException;
import java.util.Map;

//156-新华字典
public class XinHuaDictUtil {
    public static String KEY="6a933dbe80d30992543f46374a38fc12";
    public static XinHuaDictRespData getCharInXinHuaDict(char ch) throws IOException {
        XinHuaDictReqData reqData=new XinHuaDictReqData();
        reqData.setKey(KEY);
        reqData.setWord(ch);
        reqData.setDtype("json");
        Map<String,Object> params= BeanResolver.toMap(reqData,true);
        String js= HttpClientUtil.doGet(XinHuaDictReqData.URL,params);
        XinHuaDictRespData ret= JsonUtil.fromJson(js,XinHuaDictRespData.class);
        return ret;
    }
}
