package i2f.commons.component.translate;

import i2f.commons.component.http.HttpClientUtil;
import i2f.commons.component.json.gson.JsonUtil;
import i2f.commons.component.translate.data.YouDaoTranslateReqData;
import i2f.commons.component.translate.data.YouDaoTranslateRespData;
import i2f.commons.core.utils.reflect.core.resolver.BeanResolver;

import java.io.IOException;
import java.util.Map;

public class YouDaoTranslateUtil {
    //支持语言参考
    //https://ai.youdao.com/DOCSIRMA/html/%E8%87%AA%E7%84%B6%E8%AF%AD%E8%A8%80%E7%BF%BB%E8%AF%91/API%E6%96%87%E6%A1%A3/%E6%96%87%E6%9C%AC%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1/%E6%96%87%E6%9C%AC%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1-API%E6%96%87%E6%A1%A3.html#section-9
    public static final String URL="https://openapi.youdao.com/api";

    public static String APP_KEY="1994ab4d82fd730a";

    public static String APP_SECRET="rkVZjRsmT3PugFAkxrB07BrILnlxNnXy";

    public static YouDaoTranslateRespData Zh2En(String content) throws IOException{
        return translate(content,"zh-CHS","en");
    }

    public static YouDaoTranslateRespData En2Zh(String content) throws IOException{
        return translate(content,"en","zh-CHS");
    }

    public static YouDaoTranslateRespData translate2En(String content) throws IOException {
        return translate(content,"auto","en");
    }

    public static YouDaoTranslateRespData translate2Zh(String content) throws IOException{
        return translate(content,"auto","zh-CHS");
    }

    public static YouDaoTranslateRespData translate2Jp(String content) throws IOException{
        return translate(content,"auto","ja");
    }

    public static YouDaoTranslateRespData translate2Korean(String content) throws IOException{
        return translate(content,"auto","ko");
    }
    public static YouDaoTranslateRespData translate2France(String content) throws IOException{
        return translate(content,"auto","fr");
    }
    public static YouDaoTranslateRespData translate2Russia(String content) throws IOException{
        return translate(content,"auto","ru");
    }

    public static YouDaoTranslateRespData translate(String content, String from, String to) throws IOException {
        YouDaoTranslateReqData reqData=YouDaoTranslateReqData.build()
                .begin(APP_KEY)
                .trans(content,from,to)
                .sign(APP_SECRET)
                .done();
        Map<String,Object> params= BeanResolver.toMap(reqData,true);
        String js= HttpClientUtil.doPost(URL,params);
        YouDaoTranslateRespData ret= JsonUtil.fromJson(js,YouDaoTranslateRespData.class);
        return ret;
    }
}
