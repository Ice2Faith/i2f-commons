package i2f.commons.component.excel.easyexcel.util.core;

import java.util.List;

/**
 * @author ltb
 * @date 2021/10/19
 */
public interface IDataProvider {
    void preProcess();
    boolean supportPage();
    List getData(PageRequestData page);
    Class getDataClass();
}
