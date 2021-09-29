package i2f.commons.component.api.todayInHistory.data;

import i2f.commons.component.api.todayInHistory.data.detail.TodayInHistoryDetailItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TodayInHistoryRespData {
    private int error_code;
    private String reason;
    private List<TodayInHistoryDetailItem> result;
}
