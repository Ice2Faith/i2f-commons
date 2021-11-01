package i2f.commons.component.sftp.data;

import java.util.Properties;

/**
 * @author ltb
 * @date 2021/11/1
 */
public interface ISftpMeta {
    String getHost();
    int getPort();
    String getUserName();
    //密码验证
    String getPassword();
    //秘钥验证
    String getPrivateKey();

    //其他配置
    Properties getConfig();
}
