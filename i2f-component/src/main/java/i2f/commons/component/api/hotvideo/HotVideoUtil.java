package i2f.commons.component.api.hotvideo;

import i2f.commons.component.api.hotvideo.data.HotVideoReqData;
import i2f.commons.component.api.hotvideo.data.HotVideoRespData;
import i2f.commons.component.http.HttpClientUtil;
import i2f.commons.component.json.gson.JsonUtil;
import i2f.commons.core.utils.reflect.core.resolver.BeanResolver;

import java.io.IOException;
import java.util.Map;

//573-热门视频榜单
public class HotVideoUtil {
    public static String KEY="edeec8c2ccf5f2c18c847f95cfc8b91b";

    public static HotVideoRespData getHotVideos(int size) throws IOException {
        HotVideoReqData reqData=new HotVideoReqData();
        reqData.setKey(KEY);
        reqData.setSize(size);
        reqData.setType("hot_video");
        Map<String,Object> params= BeanResolver.toMap(reqData,true);
        String js= HttpClientUtil.doGet(HotVideoReqData.URL,params);
        HotVideoRespData ret= JsonUtil.fromJson(js,HotVideoRespData.class);
        return ret;
    }
}
