package i2f.log.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/3/2 15:14
 * @desc
 */
@Data
@NoArgsConstructor
public class BaseLocationLogModel extends BaseLogModel{
    // 日志发生行
    protected String line;
    // 日志发生方法
    protected String method;
    // 日志发生线程
    protected String thread;
    // 日志发生的文件
    protected String fileName;
}
