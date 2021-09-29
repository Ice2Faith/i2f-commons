package i2f.commons.component.web.utils;


import i2f.commons.core.utils.data.DataUtil;
import i2f.commons.core.utils.file.FileUtil;
import i2f.commons.core.utils.file.core.FileMime;
import i2f.commons.core.utils.safe.CheckUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

public class HttpFileUtil {
    /**
     * 支持断点续传的下载方式
     * @param request 需要从请求中解析得到断点的请求范围
     * @param response 响应数据
     * @param responseFile 要响应回去的真实文件
     * @param virtualFileName 响应回去的文件名
     * @param mimeType 可为null的MIME类型，用于指定Content-Type,为null时将根据virtualFileName的后缀自动识别映射
     * @param allowCache 是否允许客户端缓存
     * @param useAttachment 是否指定为附件下载形式，否则就直接打开
     * @throws IOException
     */
    public static void downloadFileRangeSupport(HttpServletRequest request,
                                                HttpServletResponse response,
                                                File responseFile,
                                                String virtualFileName,
                                                String mimeType,
                                                boolean allowCache,
                                                boolean useAttachment
    ) throws IOException {
        response.reset();
        //接受范围下载
        response.setHeader("Accept-Ranges","bytes");
        //设置MIME,特别指定则用指定的，否则用默认匹配的
        if(null!=mimeType){
            response.setContentType(mimeType+";charset=UTF-8");
        }else{
            response.setContentType(FileMime.getMimeType(virtualFileName)+";charset=UTF-8");
        }

        //编码文件名
        String urlEncodedFileName=java.net.URLEncoder.encode(virtualFileName, "UTF-8");
        //设置要求浏览器的接受形式：直接打开 inline 附件下载 attachment
        if(useAttachment){
            response.setHeader("Content-Disposition","attachment; filename=" +urlEncodedFileName); // 设置文件名称
        }else{
            response.setHeader("Content-Disposition","inline; filename=" +urlEncodedFileName);
        }

        //禁用缓存
        if(!allowCache){
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "No-cache");
            response.setHeader("Expires", Long.toString(0L));
        }

        //获取文件大小
        long sumLen=responseFile.length();

        long responseLen=sumLen;
        //解析范围下载请求的范围
        String rangeHead=request.getHeader("Range");
        long rangeBegin=0L,rangeEnd=sumLen-1;
        boolean hasRange=false;
        if(!CheckUtil.isEmptyStr(rangeHead,false)){
            rangeHead=rangeHead.trim();
            if(rangeHead.contains("-")){
                String[] rangeArr=rangeHead.replace("bytes=","").split("-");
                if(rangeArr.length>0){
                    rangeBegin=Long.parseLong(rangeArr[0]);
                }
                if(rangeArr.length>1){
                    rangeEnd=Long.parseLong(rangeArr[1]);
                }
                hasRange=true;
            }
        }

        //规范范围参数
        if(rangeBegin<0){
            rangeBegin=0;
        }

        if(rangeEnd<0){
            rangeEnd=sumLen-1L;
        }

        if(rangeBegin>=sumLen){
            rangeBegin=0;
        }

        if(rangeEnd>=sumLen){
            rangeEnd=sumLen-1L;
        }

        responseLen=rangeEnd-rangeBegin+1L;

        if(rangeBegin+responseLen>=sumLen){
            responseLen=sumLen-rangeBegin;
        }

        if(hasRange){
            response.setStatus(206);
            response.setHeader("Accept-Ranges", String.format("bytes %d-%d/%d", rangeBegin, rangeEnd, responseLen));
        }else{
            response.setStatus(200);
        }

        response.setHeader("Content-Length",Long.toString(responseLen));
        response.setHeader("Content-Range", String.format("bytes %d-%d/%d",rangeBegin,rangeEnd,responseLen));

        InputStream is=new BufferedInputStream(new FileInputStream(responseFile));
        OutputStream os= new BufferedOutputStream(response.getOutputStream());
        long transLen= DataUtil.streamCopyRange(is,os,rangeBegin,responseLen,false);

        is.close();
        os.close();
    }

    /**
     * 使用流式下载文件方式（能够减少内存资源暂用）
     * @param is
     * @param virtualFileName
     * @return
     * @throws IOException
     */
    public static ResponseEntity responseFileInStreamMode(InputStream is, String virtualFileName,String mimeType) throws IOException {
        InputStreamResource inputStreamResource=new InputStreamResource(is);
        HttpHeaders httpHeaders=new HttpHeaders();
        if(null!=mimeType){
            httpHeaders.set("Content-Type",mimeType+";charset=UTF-8");
        }else{
            httpHeaders.set("Content-Type", FileMime.getMimeType(virtualFileName) +";charset=UTF-8");
        }

        httpHeaders.set("Content-Disposition","attachment; filename=" +java.net.URLEncoder.encode(virtualFileName, "UTF-8"));

        return new ResponseEntity(inputStreamResource,httpHeaders, HttpStatus.OK);
    }

    public static void responseNotFileFound(String fileName, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(404);
        response.getWriter().write("<script>alert('404,not found file:"+fileName+"');</script>");
    }

    /**
     * 普通文件直接下载方式
     * @param is
     * @param closeStream
     * @param virtualFileName
     * @param response
     * @throws IOException
     */
    public static void responseAsFileAttachment(InputStream is,boolean closeStream,String virtualFileName,String mimeType, HttpServletResponse response) throws IOException {
        response.reset();
        if(null!=mimeType){
            response.setContentType(mimeType+";charset=UTF-8");
        }else{
            response.setContentType(FileMime.getMimeType(virtualFileName)+";charset=UTF-8");
        }
        response.setHeader("Content-Disposition","attachment; filename=" +java.net.URLEncoder.encode(virtualFileName, "UTF-8")); // 设置文件名称

        OutputStream os= response.getOutputStream();
        DataUtil.streamCopy(is,os,false);

        os.close();
        if(closeStream){
            is.close();
        }
    }

    public static void responseFileAttachment(File filePath,String mimeType, HttpServletResponse response) throws IOException {
        String fileName=filePath.getName();
        InputStream is=new FileInputStream(filePath);
        responseAsFileAttachment(is,true,fileName,mimeType,response);
    }

    public static String getMultipartFileName(MultipartFile multipartFile){
        return new File(multipartFile.getOriginalFilename()).getName();
    }
    public static File saveMultipartFile(MultipartFile multipartFile,File saveFile) throws IOException {
        if(multipartFile==null || multipartFile.isEmpty()){
            return null;
        }
        multipartFile.transferTo(saveFile);
        return saveFile;
    }
    public static File saveMultipartFile2ContextPath(MultipartFile multipartFile, HttpServletRequest request,String relativePath) throws IOException {
        return saveMultipartFile2ContextPath(multipartFile,request.getSession(),relativePath);
    }
    public static File saveMultipartFile2ContextPath(MultipartFile multipartFile, HttpSession session, String relativePath) throws IOException {
        File saveFile=HttpRequestUtil.getContextPath(session,relativePath);
        FileUtil.useDir(saveFile);
        saveFile=new File(saveFile,getMultipartFileName(multipartFile));
        saveFile=saveMultipartFile(multipartFile,saveFile);
        return saveFile;
    }
}
