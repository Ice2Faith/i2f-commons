package i2f.commons.component.kuaidi100.data;

import i2f.commons.component.kuaidi100.data.detail.Kuaidi100MailItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Kuaidi100RespData {
    private String state;
    private String status;
    private String message;
    private String ischeck;
    private String condition;
    private String com;
    private String nu;
    private List<Kuaidi100MailItem> data;
}
