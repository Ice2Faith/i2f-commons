package i2f.commons.component.excel.easyexcel.util.core;

public interface IExcelRecordFilter<T> {
    boolean pass(T bean);
}
