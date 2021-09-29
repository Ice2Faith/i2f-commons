package i2f.commons.component.excel.jexcel;

import i2f.commons.component.excel.jexcel.core.SheetData;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class ExcelUtil {
    public static SheetData loadRawData(InputStream is) throws IOException, BiffException {
        return new SheetData().parse(is,0,0,1,2,0);
    }
    public static List<List<Object>> loadDataOnly(InputStream is) throws IOException, BiffException {
        return loadRawData(is).tableBody;
    }
    public static List<Map<String,Object>> loadMaps(InputStream is) throws IOException, BiffException {
        return loadRawData(is).convert2();
    }
    public static void save(OutputStream os,List<List<Object>> data, String[] cols, String title, String sheetName) throws IOException, WriteException {
        SheetData sheetData=new SheetData();
        sheetData.tableBody=data;
        sheetData.tableHead=cols;
        sheetData.sheetName=sheetName;
        sheetData.setTitle(title);
        sheetData.save(os);
    }
    public static void save(OutputStream os,List<Map<String,Object>> data,String title,String sheetName) throws IOException, WriteException {
        SheetData sheetData=new SheetData();
        sheetData.convertFrom(data, title, sheetName);
        sheetData.save(os);
    }


}
