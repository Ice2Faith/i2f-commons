package i2f.commons.core.utils.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2021/11/4
 */
@Data
@NoArgsConstructor
public class DateMeta{
    public int year=-1;
    public int month=-1;
    public int day=-1;
    public int week=-1; // 星期
    public int cnShu=-1; // 年属相，1-12，鼠牛虎兔。。。
    public int cnYear=-1; // 中国黄历年，1-60，甲子。。。
    public int cnMonth=-1; // 中国黄历月，1-60，甲子。。。
    public int cnDay=-1; // 中国黄历日，1-60，甲子。。。

    public String weekDesc; // 星期
    public String cnShuDesc; // 年属相，1-12，鼠牛虎兔。。。
    public String cnYearDesc; // 中国黄历年，1-60，甲子。。。


    public DateMeta(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public DateMeta(int year, int month, int day, int week) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.week = week;
    }
}
