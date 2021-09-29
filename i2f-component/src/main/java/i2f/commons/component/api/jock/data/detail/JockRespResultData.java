package i2f.commons.component.api.jock.data.detail;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class JockRespResultData {
    private List<JockDetailItem> data;
}
