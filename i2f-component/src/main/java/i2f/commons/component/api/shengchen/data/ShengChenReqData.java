package i2f.commons.component.api.shengchen.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
public class ShengChenReqData {
    public static final String URL="http://apis.juhe.cn/birthEight/query";
    public static final String METHOD="GET";
    private String key;
    private String year;
    private String month;
    private String day;
    private String hour;
    public void parseDate(Date date){
        year=new SimpleDateFormat("yyyy").format(date);
        month=new SimpleDateFormat("M").format(date);
        day=new SimpleDateFormat("d").format(date);
        hour=new SimpleDateFormat("H").format(date);
    }
}
