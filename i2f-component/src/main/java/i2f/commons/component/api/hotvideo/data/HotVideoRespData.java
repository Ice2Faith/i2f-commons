package i2f.commons.component.api.hotvideo.data;

import i2f.commons.component.api.hotvideo.data.detail.HotVideoDetailItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class HotVideoRespData {
    private int error_code;
    private String reason;
    private List<HotVideoDetailItem> result;
}
