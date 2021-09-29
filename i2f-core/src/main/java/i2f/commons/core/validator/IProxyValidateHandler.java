package i2f.commons.core.validator;


import i2f.commons.core.validator.annotations.Vproxy;
import i2f.commons.core.validator.core.IProxyHandler;
import i2f.commons.core.validator.exception.ValidateException;

/**
 * 普通验证接口，你需要实现该接口，
 * 当验证不通过时，应该抛出一个异常
 *
 * 并且你应该保证该实现子类拥有默认构造方法
 */
public interface IProxyValidateHandler extends IProxyHandler {
    void test(Vproxy validator, Object val) throws ValidateException;
}
