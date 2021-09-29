package test.wrapper;

import i2f.commons.core.utils.db.annotations.DBColumn;
import i2f.commons.core.utils.db.annotations.DBTable;
import i2f.commons.core.utils.reflect.annotations.BeanTable;
import i2f.commons.core.utils.reflect.annotations.FieldColumn;
import i2f.commons.core.utils.reflect.annotations.FieldPrimaryKey;
import lombok.Data;

/**
 * @author ltb
 * @date 2021/9/28
 */
@Data
@BeanTable("user")
@DBTable(table = "user",schema = "singlenotedb",underScore = true,comment = "用户表")
public class UserBean {
    @FieldColumn
    @FieldPrimaryKey(isAuto = true)
    @DBColumn(underScore = true,primaryKey = true,autoIncrement = true,autoIncrementBeginNumber = 100,comment = "ID",order = 0)
    private Integer userId;
    @FieldColumn
    @DBColumn(underScore = true,notNull = true,unique = true,comment = "用户名",order = 1)
    private String userName;
    @FieldColumn
    @DBColumn(underScore = true,notNull = true,defaultRt = "0",comment = "年龄",order = 2)
    private Integer userAge;
    @FieldColumn
    @DBColumn(underScore = true,text = true,comment = "简介",order = 4)
    private String userIntro;
    @FieldColumn
    @DBColumn(underScore = true,type = "CHAR(1)",check = "user_sex in (1,2)",comment = "性别：1 男 2 女",order = 3)
    private Integer userSex;
}
