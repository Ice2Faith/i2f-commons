package i2f.commons.component.api.constellation;

import i2f.commons.component.api.constellation.data.*;
import i2f.commons.component.http.HttpClientUtil;
import i2f.commons.component.json.gson.JsonUtil;
import i2f.commons.core.utils.reflect.core.resolver.BeanResolver;

import java.io.IOException;
import java.util.Map;

//58-星座运势
public class ConstellationUtil {
    public static String KEY="006ae0f01e647899d120f787f91b7d8a";

    public static DayConstellationRespData getConstellationTodayData(String consName) throws IOException {
        ConstellationReqData reqData=new ConstellationReqData();
        reqData.setKey(KEY);
        reqData.setConsName(consName);
        reqData.setType("today");
        Map<String,Object> params= BeanResolver.toMap(reqData,true);
        String js= HttpClientUtil.doGet(ConstellationReqData.URL,params);
        DayConstellationRespData ret= JsonUtil.fromJson(js,DayConstellationRespData.class);
        return  ret;
    }

    public static DayConstellationRespData getConstellationTomorrowData(String consName) throws IOException {
        ConstellationReqData reqData=new ConstellationReqData();
        reqData.setKey(KEY);
        reqData.setConsName(consName);
        reqData.setType("tomorrow");
        Map<String,Object> params= BeanResolver.toMap(reqData,true);
        String js= HttpClientUtil.doGet(ConstellationReqData.URL,params);
        DayConstellationRespData ret= JsonUtil.fromJson(js,DayConstellationRespData.class);
        return  ret;
    }

    public static WeekConstellationRespData getConstellationWeekData(String consName) throws IOException {
        ConstellationReqData reqData=new ConstellationReqData();
        reqData.setKey(KEY);
        reqData.setConsName(consName);
        reqData.setType("week");
        Map<String,Object> params= BeanResolver.toMap(reqData,true);
        String js= HttpClientUtil.doGet(ConstellationReqData.URL,params);
        WeekConstellationRespData ret= JsonUtil.fromJson(js,WeekConstellationRespData.class);
        return  ret;
    }

    public static MonthConstellationRespData getConstellationMonthData(String consName) throws IOException {
        ConstellationReqData reqData=new ConstellationReqData();
        reqData.setKey(KEY);
        reqData.setConsName(consName);
        reqData.setType("month");
        Map<String,Object> params= BeanResolver.toMap(reqData,true);
        String js= HttpClientUtil.doGet(ConstellationReqData.URL,params);
        MonthConstellationRespData ret= JsonUtil.fromJson(js,MonthConstellationRespData.class);
        return  ret;
    }

    public static YearConstellationRespData getConstellationYearData(String consName) throws IOException {
        ConstellationReqData reqData=new ConstellationReqData();
        reqData.setKey(KEY);
        reqData.setConsName(consName);
        reqData.setType("year");
        Map<String,Object> params= BeanResolver.toMap(reqData,true);
        String js= HttpClientUtil.doGet(ConstellationReqData.URL,params);
        YearConstellationRespData ret= JsonUtil.fromJson(js,YearConstellationRespData.class);
        return  ret;
    }
}
