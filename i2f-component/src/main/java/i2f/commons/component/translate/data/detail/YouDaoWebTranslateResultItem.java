package i2f.commons.component.translate.data.detail;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class YouDaoWebTranslateResultItem {
    private String key;
    private List<String> value;
}
