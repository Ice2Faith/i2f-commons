package test;

import i2f.commons.component.http.HttpClientUtil;
import i2f.commons.component.http.data.HttpRequestData;
import i2f.commons.core.utils.file.FileUtil;
import i2f.commons.core.utils.tool.CmdLine;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @author ltb
 * @date 2021/10/15
 */
public class TestHttpCLient {
    public static void main(String[] args) throws IOException, InterruptedException {
//        String rs=CmdLine.runLine("cmd /c tracert 114.114.114.114");
//        System.out.println(rs);

        HttpRequestData reqData=new HttpRequestData()
                .url("http://www.baidu.com/s")
                .method(HttpRequestData.RequestMethod.GET)
                .addUrlParam("wd","你好")
                .addUrlParam("date",new Date())
                .allowRedirect(true);

        String retStr= HttpClientUtil.request(reqData);
        System.out.println(retStr);

        FileUtil.save(retStr,new File("D:\\01test\\client.html"));

        CmdLine.run("explorer D:\\01test\\client.html");
    }
}
