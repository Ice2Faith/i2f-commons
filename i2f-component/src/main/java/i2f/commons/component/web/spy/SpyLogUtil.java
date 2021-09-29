package i2f.commons.component.web.spy;


import i2f.commons.core.utils.str.AppendUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class SpyLogUtil {
    public static void logoutRequestParams(HttpServletRequest request,Exception ex){
        //here print request log info
        String reqUrl=request.getRequestURI();
        String method=request.getMethod();
        AppendUtil.AppendBuilder builder= AppendUtil.builder()
                .addsLine("---- request:",method," : ",reqUrl," ----");
        Map<String,String[]> reqParams=request.getParameterMap();
        if(reqParams!=null && reqParams.size()>0){
            builder.addsLine("+parameters:");
            for(String key : reqParams.keySet()){
                builder.addsLine("+\t",key,":");
                String[] vals=reqParams.get(key);
                for(String val : vals){
                    builder.addsLine("+\t\t",val);
                }
            }
            builder.addsLine("--------");
        }
        System.out.println(builder.done());
        if(ex!=null){
            System.out.println("--Exception-- request:"+reqUrl+" ----");
            ex.printStackTrace();
        }
    }
}
