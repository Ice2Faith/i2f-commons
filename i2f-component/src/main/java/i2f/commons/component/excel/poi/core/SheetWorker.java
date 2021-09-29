package i2f.commons.component.excel.poi.core;


import i2f.commons.component.excel.poi.data.SheetMeta;
import i2f.commons.core.utils.str.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 扁平Excel读写工具类
 * 支持格式：
 * 第一行为表头，由SheetMeta中定义，详情见SheetMeta中的定义
 * 从第二行开始为表数据
 */
public class SheetWorker {
    /**
     * 检查是否是Excel文件，弱检查，文件存在并且后缀符合
     * @param file
     * @return
     */
    public static boolean isLegalExcelFile(File file){
        if(file==null || !file.exists() || !file.isFile()){
            return false;
        }
        String suffix= StringUtil.getExtension(file.getName());
        if(!".xls".equalsIgnoreCase(suffix) && !".xlsx".equalsIgnoreCase(suffix)){
            return false;
        }
        return true;
    }

    /**
     * 打开指定的Excel文件
     * @param file
     * @return
     * @throws IOException
     */
    public static Workbook open(File file) throws IOException {
        if(!isLegalExcelFile(file)){
            return null;
        }
        Workbook workbook=WorkbookFactory.create(file);
        return workbook;
    }

    /**
     * 根据meta指定的表名获取指定的表，优先按照索引，索引未指定时，才按照名称获取
     * @param workbook
     * @param meta
     * @return
     */
    public static Sheet getSheet(Workbook workbook, SheetMeta meta){
        if(meta.sheetIndex!=null && meta.sheetIndex>=0){
            return workbook.getSheetAt(meta.sheetIndex);
        }
        if(meta.sheetName!=null && !"".equals(meta.sheetName)){
            return workbook.getSheet(meta.sheetName);
        }
        return null;
    }


    /**
     * 获取表头行的meta信息进行填充
     * @param metaRow
     * @param meta
     * @return
     */
    public static SheetMeta parseIndexMeta(Row metaRow,SheetMeta meta){
        SheetMeta rvMeta=new SheetMeta();
        for(String item : meta.keySet()){
            rvMeta.put(meta.get(item),item);
        }
        meta.keySortList.clear();
        int colsCount=metaRow.getLastCellNum();
        for(int i=0;i<colsCount;i+=1){
            Cell cell= metaRow.getCell(i);
            cell.setCellType(CellType.STRING);
            String title= cell.getStringCellValue();
            if(rvMeta.containsKey(title)){
                meta.keySortList.add(rvMeta.get(title));
            }else{
                meta.keySortList.add(null);
            }
        }
        return meta;
    }

    /**
     * 获取单元格数据，转换为合适的类型
     * @param cell
     * @return
     */
    public static Object getCellData(Cell cell){
        CellType type=cell.getCellType();
        if(type==CellType.NUMERIC){
            return cell.getNumericCellValue();
        }
        if(type==CellType.BOOLEAN){
            return cell.getBooleanCellValue();
        }
        return cell.getStringCellValue();
    }

    /**
     * 读取Excel数据到指定的List中
     * @param ret
     * @param file
     * @param meta
     * @return
     * @throws IOException
     */
    public static List<Map<String,Object>> read(List<Map<String,Object>> ret,File file, SheetMeta meta) throws IOException {
        Workbook workbook= open(file);
        if(workbook==null){
            return null;
        }
        Sheet sheet=getSheet(workbook,meta);
        int rowCount=sheet.getLastRowNum()+1;
        if(rowCount<1){
            return ret;
        }
        Row metaRow=sheet.getRow(0);
        meta=parseIndexMeta(metaRow,meta);
        int maxColCount=metaRow.getLastCellNum();
        for(int i=1;i<rowCount;i+=1){
            Row row= sheet.getRow(i);
            Map<String,Object> line=new HashMap<String,Object>();
            for(int j=0;j<maxColCount;j+=1){
                Cell cell=row.getCell(j);
                Object data=getCellData(cell);
                String pk=meta.keySortList.get(j);
                if(pk!=null){
                    line.put(pk,data);
                }
            }
            ret.add(line);
        }
        workbook.close();
        return ret;
    }

    /**
     * 根据文件后缀，创建对应的Excel类型
     * @param file
     * @return
     */
    public static Workbook createWorkbook(File file){
        String suffix=StringUtil.getExtension(file.getName());
        if(".xls".equalsIgnoreCase(suffix)){
            return new HSSFWorkbook();
        }
        return new XSSFWorkbook();
    }

    /**
     * 根据指定的信息，创建对应的表格
     * @param workbook
     * @param meta
     * @return
     */
    public static Sheet createSheet(Workbook workbook,SheetMeta meta){
        if(meta.sheetName!=null && !"".equals(meta.sheetName)){
            return workbook.createSheet(meta.sheetName);
        }
        return workbook.createSheet();
    }

    /**
     * 写入表头信息，并返回开始写入数据的行索引
     * @param sheet
     * @param meta
     * @return
     */
    public static int writeMeta(Sheet sheet,SheetMeta meta){
        int rowIndex=0;
        Row metaRow=sheet.createRow(rowIndex);
        for (int j = 0; j < meta.keySortList.size(); j++) {
            Cell cell= metaRow.createCell(j);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(meta.get(meta.keySortList.get(j)));
        }
        return rowIndex+1;
    }

    /**
     * 向单元格写入数据，并转换为合适的单元格类型
     * @param cell
     * @param obj
     */
    public static void setCellValue(Cell cell,Object obj){
        // 判断object的类型
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (obj instanceof Double) {
            cell.setCellValue((Double) obj);
        } else if (obj instanceof Date) {
            String time = simpleDateFormat.format((Date) obj);
            cell.setCellValue(time);
        } else if (obj instanceof Calendar) {
            Calendar calendar = (Calendar) obj;
            String time = simpleDateFormat.format(calendar.getTime());
            cell.setCellValue(time);
        } else if (obj instanceof Boolean) {
            cell.setCellValue((Boolean) obj);
        } else {
            if (obj != null) {
                cell.setCellValue(obj.toString());
            }
        }
    }

    /**
     * 将数据直接写入到Excel文件
     * @param data
     * @param meta
     * @param file
     * @throws IOException
     */
    public static void write(List<Map<String,Object>> data,SheetMeta meta,File file) throws IOException {
        Workbook workbook=createWorkbook(file);
        Sheet sheet=createSheet(workbook,meta);
        int dataBeginRow=writeMeta(sheet,meta);
        for(int i=0;i<data.size();i+=1){
            Map<String,Object> line= data.get(i);
            int si=dataBeginRow+i;
            Row dataRow=sheet.createRow(si);
            for (int j = 0; j < meta.keySortList.size(); j++) {
                Cell cell= dataRow.createCell(j);
                setCellValue(cell,line.get(meta.keySortList.get(j)));
            }
        }
        File pfile=file.getParentFile();
        if(!pfile.exists()){
            pfile.mkdirs();
        }
        OutputStream os=new FileOutputStream(file);
        workbook.write(os);
        workbook.close();
        os.close();
    }
}
