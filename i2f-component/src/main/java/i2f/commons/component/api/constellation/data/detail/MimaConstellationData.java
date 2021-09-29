package i2f.commons.component.api.constellation.data.detail;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MimaConstellationData {
    private String info;
    private List<String> text;

}
