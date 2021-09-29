package i2f.commons.component.api.jock.data.detail;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JockDetailItem {
    private String hashId;
    private long unixtime;
    private String updatetime;
    private String content;
}
