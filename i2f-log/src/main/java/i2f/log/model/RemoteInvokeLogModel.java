package i2f.log.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/3/2 14:49
 * @desc
 */
@Data
@NoArgsConstructor
public class RemoteInvokeLogModel extends InvocationLogModel{
    // 调用地址
    private String requestUrl;
    // 调用时间
    private String requestDate;
    // 调用方法
    private String requestMethod;
    // 调用参数
    private Object requestContent;

    // 响应时间
    private String responseDate;
    // 响应参数
    private Object responseContent;
}
