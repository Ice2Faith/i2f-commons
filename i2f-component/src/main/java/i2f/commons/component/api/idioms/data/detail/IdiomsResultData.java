package i2f.commons.component.api.idioms.data.detail;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class IdiomsResultData {
    private String name;
    private String pinyin;
    private List<String> jbsy;
    private List<String> xxsy;
    private String chuchu;
    private String liju;
    private List<String> jyc;
    private List<String> fyc;
}
