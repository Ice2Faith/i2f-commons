package i2f.commons.core.utils.reflect.simple.reflect.domain;

import lombok.Data;

/**
 * @author ltb
 * @date 2022/2/16 19:21
 * @desc
 */
@Data
public class BasicDomain {
    private String status;

    private String validTime;
    private String invalidTime;

    private String createTime;
    private String createUser;
    private String modifyTime;
    private String modifyUser;
}
