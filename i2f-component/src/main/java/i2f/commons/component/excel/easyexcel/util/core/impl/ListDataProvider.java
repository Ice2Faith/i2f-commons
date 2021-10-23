package i2f.commons.component.excel.easyexcel.util.core.impl;


import i2f.commons.component.excel.easyexcel.util.core.PageRequestData;

import java.util.List;

/**
 * @author ltb
 * @date 2021/10/19
 */
public class ListDataProvider extends AbsDataProviderAdapter{
    private List data;
    private Class clazz;

    @Override
    public boolean supportPage() {
        return false;
    }

    public ListDataProvider(List data, Class clazz){
        this.data=data;
        this.clazz=clazz;
    }
    @Override
    public List getData(PageRequestData page) {
        return data;
    }

    @Override
    public Class getDataClass() {
        return clazz;
    }
}
