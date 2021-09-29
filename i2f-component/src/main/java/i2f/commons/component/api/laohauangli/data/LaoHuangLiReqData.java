package i2f.commons.component.api.laohauangli.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
public class LaoHuangLiReqData {
    public static final String URL="http://v.juhe.cn/laohuangli/d";
    public static final String METHOD="GET";
    public static final String DATE_FMT="yyyy-MM-dd";
    private String key;
    private String date;
    public void toReqDate(Date date){
        this.date=new SimpleDateFormat(DATE_FMT).format(date);
    }
}
