package i2f.log.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/3/2 14:36
 * @desc
 */
@Data
@NoArgsConstructor
public class SimpleBaseLogModel extends BaseLocationLogModel{
    // 系统名
    protected String system;
    // 模块名
    protected String module;
    // 标签
    protected String label;
}
