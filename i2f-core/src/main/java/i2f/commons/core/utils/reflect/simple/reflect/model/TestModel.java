package i2f.commons.core.utils.reflect.simple.reflect.model;

import lombok.Data;

/**
 * @author ltb
 * @date 2022/2/16 19:18
 * @desc
 */

@Data
public class TestModel extends BasicModel{
    private String entryId;
    private String entryKey;
    private String entryName;
    private String entryDesc;

    private String entryOrder;

}
