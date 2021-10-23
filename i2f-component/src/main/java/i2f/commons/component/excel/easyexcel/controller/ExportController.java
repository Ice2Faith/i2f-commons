package i2f.commons.component.excel.easyexcel.controller;


import i2f.commons.component.excel.easyexcel.util.ExportUtil;
import i2f.commons.component.json.jackson.JsonUtil;
import i2f.commons.component.web.utils.ServletUtils;
import i2f.commons.core.data.web.data.RespData;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;

/**
 * @author ltb
 * @date 2021/10/18
 */
@Controller
@RequestMapping("export")
public class ExportController {
    public static final String EXPORT_CHECK_URL_PREFIX="export/checkFileIsOK";
    public static final String EXPORT_DOWNLOAD_URL_PREFIX="export/download";
    public static final long EXPORT_FILE_MAX_SAVE_TIME=1000*60*20;

    @ResponseBody
    @PostMapping(value = "checkFileIsOK/**")
    public void checkFileIsOK(HttpServletRequest request) throws UnsupportedEncodingException {

        String path= ExportUtil.getFilePathByRequest(request,EXPORT_CHECK_URL_PREFIX);
        path= URLDecoder.decode(path,"UTF-8");

        File file=ExportUtil.getWebFile(request,path);
        boolean exist=file.exists();
        ServletUtils.respJsonObj(RespData.success(exist));
    }

    @GetMapping("download/**")
    public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String path= ExportUtil.getFilePathByRequest(request,EXPORT_DOWNLOAD_URL_PREFIX);
        path= URLDecoder.decode(path,"UTF-8");

        File file=ExportUtil.getWebFile(request,path);

        if(!file.exists()){
            ServletUtils.respJson(JsonUtil.toJson(RespData.error("request file not found.")),404);
            return;
        }

        response.reset();
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition","attachment; filename=" +java.net.URLEncoder.encode(file.getName(), "UTF-8")); // 设置文件名称

        OutputStream os= response.getOutputStream();
        BufferedInputStream is=new BufferedInputStream(new FileInputStream(file));
        byte[] buf=new byte[4096];
        int len=0;
        while((len=is.read(buf))>0){
            os.write(buf,0,len);
        }
        is.close();
        os.flush();

        // TODO 考虑删除已经下载的Excel文件，并考虑过期时长
        long lastModify=file.lastModified();
    }
}
