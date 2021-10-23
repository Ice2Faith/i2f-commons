package i2f.commons.core.data.interfaces.impl;

import i2f.commons.core.data.interfaces.IExecute;

/**
 * @author ltb
 * @date 2021/10/18
 */
public class SysPrintlnExecutor<T extends Object> implements IExecute<T> {
    @Override
    public void exec(T val) {
        System.out.println(val);
    }
}
