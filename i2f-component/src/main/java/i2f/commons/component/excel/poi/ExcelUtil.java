package i2f.commons.component.excel.poi;


import i2f.commons.component.excel.poi.core.SheetWorker;
import i2f.commons.component.excel.poi.data.SheetMeta;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 基于POI的Excel导入导出工具类
 */
public class ExcelUtil {
    /**
     * 按照指定的meta信息，读取file的Excel文件到一个Map的List集合中
     * @param file
     * @param meta
     * @return
     * @throws IOException
     */
    public static List<Map<String,Object>> read(File file, SheetMeta meta) throws IOException {
        List<Map<String,Object>> saver=new LinkedList<Map<String,Object>>();
        return SheetWorker.read(saver,file,meta);
    }

    /**
     * 将数据data按照meta指定的信息写入到file指定的Excel文件中
     * @param data
     * @param meta
     * @param file
     * @throws IOException
     */
    public static void write(List<Map<String,Object>> data,SheetMeta meta,File file) throws IOException {
        SheetWorker.write(data,meta,file);
    }
}
