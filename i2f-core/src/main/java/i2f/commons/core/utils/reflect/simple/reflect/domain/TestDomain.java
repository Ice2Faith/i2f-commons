package i2f.commons.core.utils.reflect.simple.reflect.domain;

import lombok.Data;

/**
 * @author ltb
 * @date 2022/2/16 19:17
 * @desc
 */
@Data
public class TestDomain extends BasicDomain{
    private String configId;

    private String groupKey;
    private String groupName;

    private String typeKey;
    private String typeName;

    private String entryId;
    private String entryKey;
    private String entryName;
    private String entryDesc;

    private String entryOrder;


}
