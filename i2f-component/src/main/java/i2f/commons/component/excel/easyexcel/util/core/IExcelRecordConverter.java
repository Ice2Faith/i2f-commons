package i2f.commons.component.excel.easyexcel.util.core;

public interface IExcelRecordConverter<T> {
    T convert(T bean);
}
