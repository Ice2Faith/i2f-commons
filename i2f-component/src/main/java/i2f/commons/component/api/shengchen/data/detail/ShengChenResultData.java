package i2f.commons.component.api.shengchen.data.detail;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShengChenResultData {
    private String year;
    private String month;
    private String day;
    private String Animal;
    private String ImonthCn;
    private String IDayCn;
    private String cYear;
    private String cMonth;
    private String cDay;
    private String gzYear;
    private String gzMonth;
    private String gzDay;
    private boolean isLeap;
    private String ncWeek;
    private boolean isTerm;
    private String Term;
    private String astro;
    private BaZiData eightAll;
    private WuXingData fiveAll;
}
