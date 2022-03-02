package i2f.log.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/3/2 14:42
 * @desc
 */
@Data
@NoArgsConstructor
public class InvocationLogModel extends ExceptionLogModel{
    // 调用类型，前置/后置/环绕
    protected String invokeType;
    // 调用参数
    protected Object invokeArgs;
    // 返回值
    protected Object returnValue;

    // 进入时间
    protected String beginDate;
    // 结束时间
    protected String endDate;
}
