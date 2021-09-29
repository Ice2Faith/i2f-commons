package i2f.commons.component.api.laohauangli;

import i2f.commons.component.api.laohauangli.data.LaoHuangLiReqData;
import i2f.commons.component.api.laohauangli.data.LaoHuangLiRespData;
import i2f.commons.component.http.HttpClientUtil;
import i2f.commons.component.json.gson.JsonUtil;
import i2f.commons.core.utils.reflect.core.resolver.BeanResolver;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

//65-老黄历
public class LaoHuangLiUtil {
    public static String KEY="2fd810f4c87a5a315c5cf3bff7d0958d";

    public static LaoHuangLiRespData getTodayLaoHuangLi() throws IOException {
        return getDateLaoHuangLi(new Date());
    }

    public static LaoHuangLiRespData getDateLaoHuangLi(Date date) throws IOException {
        LaoHuangLiReqData reqData=new LaoHuangLiReqData();
        reqData.setKey(KEY);
        reqData.toReqDate(date);
        Map<String,Object> params= BeanResolver.toMap(reqData,true);
        String js= HttpClientUtil.doGet(LaoHuangLiReqData.URL,params);
        LaoHuangLiRespData ret= JsonUtil.fromJson(js,LaoHuangLiRespData.class);
        return ret;
    }
}
