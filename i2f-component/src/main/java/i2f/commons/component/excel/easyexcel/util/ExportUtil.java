package i2f.commons.component.excel.easyexcel.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import i2f.commons.component.excel.easyexcel.controller.ExportController;
import i2f.commons.component.excel.easyexcel.util.core.IDataProvider;
import i2f.commons.component.excel.easyexcel.util.core.IFilesManageProcessor;
import i2f.commons.component.excel.easyexcel.util.core.PageRequestData;
import i2f.commons.component.excel.easyexcel.util.core.impl.DefaultContextPathFilesManageProcessor;
import i2f.commons.component.excel.easyexcel.util.core.impl.ListDataProvider;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ltb
 * @date 2021/10/18
 */
public class ExportUtil {
    private static IFilesManageProcessor filesManageProcessor=new DefaultContextPathFilesManageProcessor();

    public static final String EXPORT_EXCEL_RELATIVE_PATH="excel/export";
    public static String getFilePathByRequest(HttpServletRequest request,String prefix){
        String url=request.getRequestURI();
        String path="";
        int idx=url.indexOf(prefix);
        if(idx>=0){
            path=url.substring(idx+prefix.length());
        }

        if(path.contains("..")){
            throw new RuntimeException("bad directory found.");
        }
        return path;
    }

    public static File getWebFile(HttpServletRequest request, String path){
        return filesManageProcessor.getFile(path,request);
    }

    private static volatile ExecutorService threadPool=null;
    public static ExecutorService getThreadPool() {
        if(threadPool==null){
            synchronized (ExportUtil.class){
                if(threadPool==null){
                    threadPool=Executors.newFixedThreadPool(5);
                    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(threadPool!=null){
                                if(!threadPool.isShutdown()){
                                    threadPool.shutdown();
                                }
                            }
                        }
                    }));
                }
            }
        }
        return threadPool;
    }
    public static void submitTask(Runnable task){
        getThreadPool().submit(task);
    }

    public static WebExcelRespData writeExcelFile(HttpServletRequest request, boolean useSync, IDataProvider provider, String excelName, String sheetName){
        String fileName=EXPORT_EXCEL_RELATIVE_PATH+"/"+excelName+("-"+System.currentTimeMillis())+".xls";
        File file= getWebFile(request,fileName);
        File tmpFile= getWebFile(request,fileName+".tmp");
        ExportTask task=new ExportTask(provider,file,tmpFile,sheetName);
        provider.preProcess();
        if(useSync){
            submitTask(task);
        }else{
            task.run();
        }
        return genWebExcelRespData(fileName);
    }

    public static WebExcelRespData writeExcelFile(HttpServletRequest request, boolean useSync, List data, Class clazz, String excelName, String sheetName){
        IDataProvider provider=new ListDataProvider(data,clazz);
        return writeExcelFile(request,useSync,provider,excelName,sheetName);
    }

    public static WebExcelRespData genWebExcelRespData(String fileName){
        String checkPrefix="/"+ ExportController.EXPORT_CHECK_URL_PREFIX +"/";
        String downloadPrefix="/"+ExportController.EXPORT_DOWNLOAD_URL_PREFIX+"/";

        String checkUrl=checkPrefix+fileName;
        String downloadUrl=downloadPrefix+fileName;
        return new WebExcelRespData(fileName,checkUrl,downloadUrl);
    }

    public static class ExportTask implements Runnable{
        private IDataProvider provider;
        private File  file;
        private File tmpFile;
        private String sheetName;
        public ExportTask(IDataProvider provider, File file, File tmpFile, String sheetName){
            this.provider=provider;
            this.file=file;
            this.tmpFile=tmpFile;
            this.sheetName=sheetName;
        }


        @Override
        public void run() {
            if(tmpFile.exists()){
                tmpFile.delete();
            }
            File parentDir=tmpFile.getParentFile();
            if(!parentDir.exists()){
                parentDir.mkdirs();
            }
            boolean isSupportPage=provider.supportPage();
            System.out.println("pageSupport:"+isSupportPage);
            int pageSize=65535;
            if(!isSupportPage){
                Class clazz=provider.getDataClass();
                List data=provider.getData(null);
                int size=data.size();
                if(size<=pageSize){
                    EasyExcel.write(tmpFile, clazz)
                            .sheet(sheetName)
                            .doWrite(data);
                }else{
                    ExcelWriter excelWriter=EasyExcel.write(tmpFile,clazz)
                            .build();
                    int curPageIndex=0;
                    int count=0;
                    List curData=new ArrayList(pageSize);
                    for(Object item : data){
                        if(count==pageSize){

                            WriteSheet writeSheet=EasyExcel.writerSheet(curPageIndex,sheetName+curPageIndex)
                                    .build();
                            excelWriter.write(data,writeSheet);

                            count=0;
                            curPageIndex++;
                            curData.clear();
                        }
                        curData.add(item);
                        count++;
                    }
                    if(count>0){
                        WriteSheet writeSheet=EasyExcel.writerSheet(curPageIndex,sheetName+curPageIndex)
                                .build();
                        excelWriter.write(curData,writeSheet);
                    }

                    excelWriter.finish();
                }
            }else{
                int curPageIndex=0;

                Class clazz=provider.getDataClass();

                ExcelWriter excelWriter=EasyExcel.write(tmpFile,clazz)
                        .build();

                while(true){
                    PageRequestData page=new PageRequestData(curPageIndex,pageSize);
                    List data= provider.getData(page);
                    int dsize=data.size();
                    if(data==null || dsize==0 ){
                        break;
                    }

                    WriteSheet writeSheet=EasyExcel.writerSheet(curPageIndex,sheetName+curPageIndex)
                            .build();
                    excelWriter.write(data,writeSheet);

                    curPageIndex++;

                    if(dsize<pageSize){
                        break;
                    }
                }

                excelWriter.finish();
            }
            if(file.exists()){
                file.delete();
            }
            parentDir=file.getParentFile();
            if(!parentDir.exists()){
                parentDir.mkdirs();
            }
            tmpFile.renameTo(file);
            System.out.println("exportDone: "+tmpFile.getAbsolutePath()+"\n\t-> "+file.getAbsolutePath());
        }

    }
}
