package i2f.commons.core.utils.reflect.simple.reflect.model;

import i2f.commons.core.utils.db.annotations.DBColumn;
import i2f.commons.core.utils.db.annotations.DBTable;
import lombok.Data;

/**
 * @author ltb
 * @date 2022/2/16 19:27
 * @desc
 */
@Data
@DBTable(schema = "test",table = "basic")
public class BasicModel {
    private String createTime;
    private String createUser;
    private String modifyTime;
    @DBColumn(name = "modify_user")
    private String modifyUser;
}
