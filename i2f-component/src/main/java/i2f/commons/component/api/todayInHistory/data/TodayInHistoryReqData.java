package i2f.commons.component.api.todayInHistory.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
public class TodayInHistoryReqData {
    public static final String URL="http://v.juhe.cn/todayOnhistory/queryEvent.php";
    public static final String METHOD="GET";
    public static final String DATE_FMT="M/d";
    public void toReqData(Date date){
        SimpleDateFormat fmt=new SimpleDateFormat(DATE_FMT);
        this.date=fmt.format(date);
    }
    public String key;
    public String date;
}
