package i2f.commons.component.api.jock.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class JockReqData {
    public static final String URL="http://v.juhe.cn/joke/content/list.php";
    public static final String METHOD="GET";
    private String key;
    private String sort;
    private int page;
    private int pagesize;
    private String time;
    public void previous(){
        sort="desc";
    }
    public void next(){
        sort="asc";
    }
    public void toReqTime(Date date){
        this.time=String.valueOf(date.getTime()/1000);
    }
}
