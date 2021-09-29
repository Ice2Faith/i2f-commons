package i2f.commons.component.excel.jexcel.core;

import jxl.*;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Excel数据表格规范
 * 表名称，表索引
 *
 * 表名、表标题
 * 表头
 * 表数据体
 *
 * 整个操作区域开始索引坐标
 *      行 tableTitleIndex 列 tableColumnIndex
 *      其中，
 *          标题索引就是操作区块索引
 *          表头索引
 *              行 tableHeadIndex 列 tableColumnIndex
 *          表数据索引
 *              行 tableBodyIndex 列 tableColumnIndex
 * 标准表格形式：
 * |-----------------------------
 * |标题 0索引行
 * |------------------------------
 * |表头 1索引行
 * |------------------------------
 * |表数据 2索引行开始
 * |------------------------------
 * 通过自定义索引坐标，可以实现如下
 *      标题之前，可以有其他行
 *      标题和表头之间可以有其他行
 *      表头和表数据之间可以有其他行
 *      列可以不从第一列开始
 */
@Data
@NoArgsConstructor
public class SheetData {
    public Cell[][] rawData;    //整张表格的完整原始数据
    public Integer sheetIndex;  //操作表格的索引
    public String sheetName;    //操作表格的名称

    public Integer tableColumnIndex;    //操作区块的开始列索引

    public String[] tableTitle;     //表格标题
    public Integer tableTitleIndex; //表格标题行索引

    public Integer tableHeadIndex;  //表格表头行索引
    public Integer tableBodyIndex;  //表格表数据体开始行索引

    public String[] tableHead;  //表头列集合
    public List<List<Object>> tableBody;    //数据体数据集合

    public SheetData setTitle(String str){
        if(tableTitle==null || tableTitle.length==0){
            tableTitle=new String[]{str};
        }else{
            for(int i=0;i<tableTitle.length;i++){
                if(i==0){
                    tableTitle[i]=str;
                }else{
                    tableTitle[i]="";
                }
            }
        }
        return this;
    }
    public String getTitle(){
        StringBuilder builder=new StringBuilder();
        for(int i=0;i<tableTitle.length;i++){
            builder.append(tableTitle[i].trim());
        }
        return builder.toString();
    }

    public SheetData resetOffsetOnly(){
        tableColumnIndex=0;
        tableTitleIndex=0;
        tableHeadIndex=1;
        tableBodyIndex=2;
        return this;
    }
    public SheetData resetAttrs(){
        rawData=new Cell[][]{};
        sheetIndex=0;
        sheetName="sheet1";
        tableColumnIndex=0;
        tableTitle=new String[]{"vacant"};
        tableTitleIndex=0;
        tableHeadIndex=1;
        tableBodyIndex=2;
        tableHead=new String[]{};
        tableBody=new ArrayList<List<Object>>();
        return this;
    }

    private SheetData checkAttrs(){
        tableTitleIndex=(tableTitleIndex==null||tableTitleIndex<0)?0:tableTitleIndex;
        tableHeadIndex=(tableHeadIndex==null||tableHeadIndex<0)?1:tableHeadIndex;
        tableBodyIndex=(tableBodyIndex==null||tableBodyIndex<0)?2:tableBodyIndex;
        tableColumnIndex=(tableColumnIndex==null||tableColumnIndex<0)?0:tableColumnIndex;
        sheetIndex=(sheetIndex==null||sheetIndex<0)?0:sheetIndex;

        sheetName=(sheetName==null||"".equals(sheetName))?"sheet1":sheetName;
        rawData=(rawData==null)?new Cell[][]{}:rawData;
        tableTitle=(tableTitle==null)?new String[]{}:tableTitle;
        tableHead=(tableHead==null)?new String[]{}:tableHead;
        tableBody=(tableBody==null)?new ArrayList<List<Object>>():tableBody;

        if(tableTitle.length==0 && tableBody.size()==0){
            tableTitle=new String[]{"vacant"};
        }
        return this;
    }

    public SheetData parse(InputStream is,  //Excel文件的输入流，读取完成之后将会自动关闭流
                           Integer sheetIndex,   // 表格索引，值为null即为0
                           Integer tableTitleIndex,  //表格的标题行索引，值为null时即为0
                           Integer tableHeadIndex,  //表格的表头行索引，值为null即为1
                           Integer tableBodyIndex,  //表格的表数据体开始行索引，值为null即为2
                           Integer tableColumnIndex    //表格的表数据开始列索引，值为null即为0
    ) throws IOException, BiffException {
        this.tableTitleIndex=tableTitleIndex;
        this.tableHeadIndex=tableHeadIndex;
        this.tableBodyIndex=tableBodyIndex;
        this.tableColumnIndex=tableColumnIndex;
        this.sheetIndex=sheetIndex;
        checkAttrs();

        Workbook workbook=Workbook.getWorkbook(is);

        Sheet sheet=workbook.getSheet(sheetIndex);
        sheetName=sheet.getName();
        parseData(sheet);
        workbook.close();
        is.close();
        return this;
    }

    public SheetData parse(InputStream is,  //Excel文件的输入流，读取完成之后将会自动关闭流
                           String  sheetName,   // 表格名称
                           Integer titleLineIndex,  //表格的标题行索引，值为null时即为0
                           Integer tableHeadLineIndex,  //表格的表头行索引，值为null即为1
                           Integer tableBodyLineIndex,  //表格的表数据体开始行索引，值为null即为2
                           Integer tableColumnIndex    //表格的表数据开始列索引，值为null即为0
    ) throws IOException, BiffException {
        this.tableTitleIndex=titleLineIndex;
        this.tableHeadIndex=tableHeadLineIndex;
        this.tableBodyIndex=tableBodyLineIndex;
        this.tableColumnIndex=tableColumnIndex;
        this.sheetName=sheetName;
        checkAttrs();

        Workbook workbook=Workbook.getWorkbook(is);

        Sheet sheet=workbook.getSheet(sheetName);
        Sheet[] sheets=workbook.getSheets();
        for(int i=0;i<sheets.length;i++){
            if(sheets[i].getName().equals(sheetName)){
                this.sheetIndex=i;
                sheet=sheets[i];
                break;
            }
        }
        parseData(sheet);
        workbook.close();
        is.close();
        return this;
    }

    private void getTitleLineInfo(Sheet sheet, int rowCount, int colCount){
        tableTitle=new String[colCount-tableColumnIndex];
        for(int c=tableColumnIndex;c<colCount;c++) {
            Cell cell = sheet.getCell(c, tableTitleIndex);
            tableTitle[c-tableColumnIndex]= cell.getContents();
        }
    }
    private void getHeadLineInfo(Sheet sheet,int rowCount,int colCount){
        tableHead=new String[colCount-tableColumnIndex];
        for(int c=tableColumnIndex;c<colCount;c++) {
            Cell cell = sheet.getCell(c, tableHeadIndex);
            tableHead[c-tableColumnIndex]= cell.getContents();
        }
    }

    private void parseData(Sheet sheet) throws IOException, BiffException {

        int rowCount=sheet.getRows();
        int colCount= sheet.getColumns();

        getTitleLineInfo(sheet,rowCount,colCount);
        getHeadLineInfo(sheet,rowCount,colCount);
        rawData=new Cell[rowCount][colCount];

        tableBody=new ArrayList<List<Object>>();

        for(int r=0;r<rowCount;r++){
            int bodyR=r-tableBodyIndex;
            List<Object> line=null;
            if(bodyR>=0){
                line=new ArrayList<Object>();
            }
            for(int c=0;c<colCount;c++){
                Cell cell=sheet.getCell(c,r);
                rawData[r][c]=cell;

                if(r<tableBodyIndex){
                    continue;
                }

                if(c<tableColumnIndex){
                    continue;
                }

                int bodyC=c-tableColumnIndex;

                CellType cellType=cell.getType();
                Object val=null;

                if(cellType==CellType.LABEL){
                    LabelCell labelCell=(LabelCell)cell;
                    val=labelCell.getContents();
                }else if(cellType==CellType.NUMBER){
                    NumberCell numberCell=(NumberCell)cell;
                    val=numberCell.getValue();
                }else if(cellType==CellType.DATE){
                    DateCell dateCell=(DateCell)cell;
                    val=dateCell.getDate();
                }else if(cellType==CellType.BOOLEAN){
                    BooleanCell booleanCell=(BooleanCell)cell;
                    val=booleanCell.getValue();
                }else{
                    val=cell.getContents();
                }

                line.add(val);

            }
            if(bodyR>=0) {
                tableBody.add(line);
            }
        }

    }

    private void setRowHeightByFontSize(WritableSheet sheet, int row, int rowFontSize) throws RowsExceededException {
        sheet.setRowView(row,rowFontSize*32);
    }

    private void setTitleInfo(WritableSheet sheet) throws WriteException {
        int colspan=tableHead.length;
        if(colspan>1) {
            sheet.mergeCells(tableColumnIndex, tableTitleIndex, tableColumnIndex + colspan - 1, tableTitleIndex);
        }
        setRowHeightByFontSize(sheet,tableTitleIndex,16);

        WritableFont font=new WritableFont(WritableFont.createFont("黑体"),
                16,WritableFont.BOLD,
                false,
                UnderlineStyle.NO_UNDERLINE,
                Colour.BLACK);
        WritableCellFormat format=new WritableCellFormat();
        format.setFont(font);
        format.setAlignment(Alignment.CENTRE);
        format.setVerticalAlignment(VerticalAlignment.CENTRE);
        format.setBackground(Colour.VERY_LIGHT_YELLOW);
        format.setWrap(true);

        Label title=new Label(tableColumnIndex,tableTitleIndex,getTitle(),format);

        sheet.addCell(title);
    }
    private void setHeadInfo(WritableSheet sheet) throws WriteException {
        setRowHeightByFontSize(sheet,tableHeadIndex,14);
        WritableFont font=new WritableFont(WritableFont.createFont("黑体"),
                14,WritableFont.NO_BOLD,
                false,
                UnderlineStyle.NO_UNDERLINE,
                Colour.BLACK);
        WritableCellFormat format=new WritableCellFormat();
        format.setFont(font);
        format.setAlignment(Alignment.CENTRE);
        format.setVerticalAlignment(VerticalAlignment.CENTRE);
        format.setBackground(Colour.GRAY_25);
        format.setWrap(true);

        for(int i=0;i<tableHead.length;i++){
            Label label=new Label(tableColumnIndex+i,tableHeadIndex,tableHead[i],format);
            sheet.addCell(label);
        }

    }
    private void setBodyInfo(WritableSheet sheet) throws WriteException{
        WritableFont sf=new WritableFont(WritableFont.createFont("黑体"),
                10,WritableFont.NO_BOLD,
                false,
                UnderlineStyle.NO_UNDERLINE,
                Colour.BLACK);
        WritableCellFormat sfmt=new WritableCellFormat();
        sfmt.setFont(sf);
        sfmt.setVerticalAlignment(VerticalAlignment.CENTRE);
        sfmt.setWrap(true);

        NumberFormat nf=new NumberFormat("#.####");
        WritableCellFormat nfmt=new WritableCellFormat(nf);
        nfmt.setVerticalAlignment(VerticalAlignment.CENTRE);

        NumberFormat inf=new NumberFormat("#");
        WritableCellFormat infmt=new WritableCellFormat(inf);
        infmt.setVerticalAlignment(VerticalAlignment.CENTRE);

        DateFormat df=new DateFormat("yyyy-MM-dd hh:mm:ss");
        WritableCellFormat dfmt=new WritableCellFormat(df);
        dfmt.setVerticalAlignment(VerticalAlignment.CENTRE);

        for(int r=0;r<tableBody.size();r++){
            List<Object> line=tableBody.get(r);
            int tr=tableBodyIndex+r;
            setRowHeightByFontSize(sheet,tr,10);
            for(int c=0;c<line.size();c++){
                Object val=line.get(c);
                int tc=tableColumnIndex+c;

                if(val instanceof Date){
                    DateTime dateTime=new DateTime(tc,tr,(Date)val,dfmt);
                    sheet.addCell(dateTime);
                }else if(val instanceof String){
                    Label label=new Label(tc,tr,(String)val,sfmt);
                    sheet.addCell(label);
                }else {
                    String sval = val + "";
                    try {
                        double num = Double.parseDouble(sval);
                        double facter = Math.abs(num - (long) num);
                        if (facter > 0.00009) {
                            Number number = new Number(tc, tr, num + 0.00005, nfmt);
                            sheet.addCell(number);
                        } else {
                            Number number = new Number(tc, tr, num, infmt);
                            sheet.addCell(number);
                        }

                    } catch (NumberFormatException e) {
                        Label label = new Label(tc, tr, sval, sfmt);
                        sheet.addCell(label);
                    }
                }

                Cell cell=sheet.getCell(tc,tr);
                int  wid=cell.getContents().getBytes().length+2;
                if(tr!=tableBodyIndex) {
                    int curWid = sheet.getColumnWidth(tc);
                    wid = wid > curWid ? wid : curWid;
                }
                sheet.setColumnView(tc,wid>80?80:wid);
                sheet.getColumnView(tc).getSize();
            }
        }

    }
    public SheetData save(OutputStream os) throws IOException, WriteException {
        checkAttrs();
        WritableWorkbook workbook=Workbook.createWorkbook(os);
        WritableSheet sheet= workbook.createSheet(sheetName,sheetIndex);

        setTitleInfo(sheet);
        setHeadInfo(sheet);
        setBodyInfo(sheet);

        workbook.write();
        workbook.close();
        os.close();

        return this;
    }

    public SheetData convertFrom(List<Map<String,Object>> data,String title,String sheetName){
        resetAttrs();
        setTitle(title);
        this.sheetName=sheetName;
        for (int i = 0; i < data.size(); i++) {
            Map<String,Object> map=data.get(i);
            if(i==0){
                this.tableHead=new String[map.size()];
                int pi=0;
                for(String key : map.keySet()){
                    this.tableHead[pi]=key;
                    pi++;
                }
            }
            List<Object> line=new ArrayList<Object>();
            for(int j=0;j<tableHead.length;j++){
                line.add(map.get(this.tableHead[j]));
            }
            tableBody.add(line);
        }
        return this;
    }

    public List<Map<String,Object>> convert2(){
        List<Map<String,Object>> ret=new ArrayList<Map<String, Object>>();
        for(List<Object> line : tableBody){
            Map<String,Object> map=new HashMap<String, Object>();
            for(int i=0;i<line.size();i++){
                map.put(this.tableHead[i],line.get(i));
            }
            ret.add(map);
        }
        return ret;
    }

    public SheetData convertFromWithHead(String[] headInfo,List<Map<String,Object>> data,String title,String sheetName){
        resetAttrs();
        setTitle(title);
        this.sheetName=sheetName;
        this.tableHead=headInfo;
        for(Map<String,Object> map : data){
            List<Object> line=new ArrayList<Object>();
            for(int j=0;j<tableHead.length;j++){
                line.add(map.get(this.tableHead[j]));
            }
            tableBody.add(line);
        }
        return this;
    }
}
