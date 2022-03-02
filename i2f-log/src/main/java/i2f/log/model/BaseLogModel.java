package i2f.log.model;

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
    protected Date time;
    // 日志类型
    protected String type;
    // 日志级别，FITAL/DEBUG/WARN/INFO
    protected String level;

    // 日志位置
    protected String location;
    // 日志内容
    protected String content;
}
