package i2f.commons.component.api.shengchen;

import i2f.commons.component.api.shengchen.data.ShengChenReqData;
import i2f.commons.component.api.shengchen.data.ShengChenRespData;
import i2f.commons.component.http.HttpClientUtil;
import i2f.commons.component.json.gson.JsonUtil;
import i2f.commons.core.utils.reflect.core.resolver.BeanResolver;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

//120-生辰助手
public class ShengChenUtil {
    public static String KEY="1129a9c9da0b1b96a981c5d0ef92b774";

    public static ShengChenRespData getNowShengChen() throws IOException {
        return getDateShengChen(new Date());
    }

    public static ShengChenRespData getDateShengChen(Date date) throws IOException {
        ShengChenReqData reqData=new ShengChenReqData();
        reqData.setKey(KEY);
        reqData.parseDate(date);
        Map<String,Object> params= BeanResolver.toMap(reqData,true);
        String js= HttpClientUtil.doGet(ShengChenReqData.URL,params);
        ShengChenRespData ret= JsonUtil.fromJson(js,ShengChenRespData.class);
        return ret;
    }
}
