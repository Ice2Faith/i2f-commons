package i2f.log.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/3/2 14:34
 * @desc
 */
@Data
@NoArgsConstructor
public class ExceptionLogModel extends SimpleBaseLogModel{
    // 异常类名
    protected String exceptionClassName;
    // 异常信息
    protected String exceptionMessage;
}
