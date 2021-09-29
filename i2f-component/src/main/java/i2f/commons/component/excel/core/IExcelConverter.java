package i2f.commons.component.excel.core;


import i2f.commons.component.excel.core.data.ExcelColumnMeta;

import java.io.File;
import java.util.List;

/**
 * @author ltb
 * @date 2021/9/17
 */
public interface IExcelConverter {

    <T> File write(List<T> list, List<ExcelColumnMeta> metas, File saveFile);

    <T> List<T> read(File readFile, List<ExcelColumnMeta> metas);
}
