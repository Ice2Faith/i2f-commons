package i2f.commons.component.api.jock;

import i2f.commons.component.api.jock.data.JockReqData;
import i2f.commons.component.api.jock.data.JockRespData;
import i2f.commons.component.http.HttpClientUtil;
import i2f.commons.component.json.gson.JsonUtil;
import i2f.commons.core.utils.reflect.core.resolver.BeanResolver;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

//95-笑话大全
public class JockUtil {
    public static String KEY="2e838a3aaa72558a87c4d50553100360";

    public static JockRespData getNowPreviousJock(int pageIndex, int pageSize) throws IOException {
        return getDatePreviousJock(new Date(),pageIndex,pageSize);
    }

    public static JockRespData getDatePreviousJock(Date date,int pageIndex, int pageSize) throws IOException {
        JockReqData reqData=new JockReqData();
        reqData.setKey(KEY);
        reqData.setPage(pageIndex);
        reqData.setPagesize(pageSize);
        reqData.toReqTime(date);
        reqData.previous();
        Map<String,Object> params= BeanResolver.toMap(reqData,true);
        String js= HttpClientUtil.doGet(JockReqData.URL,params);
        JockRespData ret= JsonUtil.fromJson(js,JockRespData.class);
        return ret;
    }
}
