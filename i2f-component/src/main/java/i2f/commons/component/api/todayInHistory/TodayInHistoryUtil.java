package i2f.commons.component.api.todayInHistory;

import i2f.commons.component.api.todayInHistory.data.TodayInHistoryReqData;
import i2f.commons.component.api.todayInHistory.data.TodayInHistoryRespData;
import i2f.commons.component.http.HttpClientUtil;
import i2f.commons.component.json.gson.JsonUtil;
import i2f.commons.core.utils.reflect.core.resolver.BeanResolver;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

//63-历史上的今天
public class TodayInHistoryUtil {
    public static String KEY="f508d797bf9a3eeaa777551305368fad";

    public static TodayInHistoryRespData getTodayInHistory() throws IOException {
        return getDateInHistory(new Date());
    }

    public static TodayInHistoryRespData getDateInHistory(Date date) throws IOException {
        TodayInHistoryReqData reqData=new TodayInHistoryReqData();
        reqData.setKey(KEY);
        reqData.toReqData(date);
        Map<String,Object> params= BeanResolver.toMap(reqData,true);
        String js= HttpClientUtil.doGet(TodayInHistoryReqData.URL,params);
        TodayInHistoryRespData ret= JsonUtil.fromJson(js,TodayInHistoryRespData.class);
        return ret;
    }
}
