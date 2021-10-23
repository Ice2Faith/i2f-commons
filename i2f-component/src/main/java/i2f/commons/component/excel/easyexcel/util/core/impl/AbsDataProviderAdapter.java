package i2f.commons.component.excel.easyexcel.util.core.impl;


import i2f.commons.component.excel.easyexcel.util.core.IDataProvider;

/**
 * @author ltb
 * @date 2021/10/19
 */
public abstract class AbsDataProviderAdapter implements IDataProvider {
    private Object[] params;
    public AbsDataProviderAdapter(Object ... args){
        this.params=args;
    }
    public Object[] getParams(){
        return params;
    }
    @Override
    public void preProcess() {

    }

}
