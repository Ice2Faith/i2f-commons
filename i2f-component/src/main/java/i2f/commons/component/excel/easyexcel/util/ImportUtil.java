package i2f.commons.component.excel.easyexcel.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import i2f.commons.component.excel.easyexcel.util.core.ObjectAnalysisEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ImportUtil {
    public static <T> List<T> readExcelFile(File file, Class<T> beanClass) throws IOException {
        ObjectAnalysisEventListener<T> listener=new ObjectAnalysisEventListener<T>();
        try(InputStream is=new FileInputStream(file)){
            readExcelFile(is,beanClass,listener);
        }
        return listener.getLegalData();
    }
    public static <T> List<T> readExcelFile(File file, Class<T> beanClass,ObjectAnalysisEventListener<T> listener) throws IOException {
        try(InputStream is=new FileInputStream(file)){
            readExcelFile(is,beanClass,listener);
        }
        return listener.getLegalData();
    }
    public static <T> List<T> readExcelFile(InputStream is,Class<T> beanClass){
        return readExcelFile(is,beanClass,new ObjectAnalysisEventListener<T>());
    }
    public static<T> List<T> readExcelFile(InputStream is, Class<T> beanClass, ObjectAnalysisEventListener<T> listener){
        ExcelReader excelReader = EasyExcel.read(is, beanClass, listener).build();
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        excelReader.read(readSheet);
        excelReader.finish();
        return listener.getLegalData();
    }
}
