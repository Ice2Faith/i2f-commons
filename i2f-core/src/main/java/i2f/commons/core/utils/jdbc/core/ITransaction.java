package i2f.commons.core.utils.jdbc.core;

import java.sql.Connection;

public interface ITransaction{
    //这个返回值将会是执行directDoTransaction时的返回值
    void doTrans(Connection conn, Object... params) throws Exception;
}
