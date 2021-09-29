package i2f.commons.component.api.idioms.data.detail;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class IdiomsNextResultData {
    private String last_word;
    private Integer total_count;
    private List<String> data;
}
