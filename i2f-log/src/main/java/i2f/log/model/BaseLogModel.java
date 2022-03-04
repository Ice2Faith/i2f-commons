package i2f.log.model;

import i2f.log.enums.LogLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author ltb
 * @date 2022/3/2 14:24
 * @desc
 */
@Data
@NoArgsConstructor
public class BaseLogModel {
    // 日志时间
    protected Date date;
    // 日志类型
    protected String type;
    // 日志级别
    protected LogLevel level;
    // 日志发生类
    protected String className;
    // 日志内容
    protected Object content;
}
