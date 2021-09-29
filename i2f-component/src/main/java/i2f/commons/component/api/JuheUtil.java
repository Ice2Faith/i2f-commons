package i2f.commons.component.api;


import i2f.commons.component.api.constellation.ConstellationUtil;
import i2f.commons.component.api.constellation.data.DayConstellationRespData;
import i2f.commons.component.api.constellation.data.MonthConstellationRespData;
import i2f.commons.component.api.constellation.data.WeekConstellationRespData;
import i2f.commons.component.api.constellation.data.YearConstellationRespData;
import i2f.commons.component.api.hotvideo.HotVideoUtil;
import i2f.commons.component.api.hotvideo.data.HotVideoRespData;
import i2f.commons.component.api.idioms.IdiomsUtil;
import i2f.commons.component.api.idioms.data.IdiomsRespData;
import i2f.commons.component.api.jock.JockUtil;
import i2f.commons.component.api.jock.data.JockRespData;
import i2f.commons.component.api.laohauangli.LaoHuangLiUtil;
import i2f.commons.component.api.laohauangli.data.LaoHuangLiRespData;
import i2f.commons.component.api.shengchen.ShengChenUtil;
import i2f.commons.component.api.shengchen.data.ShengChenRespData;
import i2f.commons.component.api.telnum.TelNumUtil;
import i2f.commons.component.api.telnum.data.TelNumRespData;
import i2f.commons.component.api.todayInHistory.TodayInHistoryUtil;
import i2f.commons.component.api.todayInHistory.data.TodayInHistoryRespData;
import i2f.commons.component.api.xinhua.dict.XinHuaDictUtil;
import i2f.commons.component.api.xinhua.dict.data.XinHuaDictRespData;

import java.io.IOException;
import java.util.Date;

public class JuheUtil {
    public static XinHuaDictRespData getCharInXinHuaDict(char ch) throws IOException {
        return XinHuaDictUtil.getCharInXinHuaDict(ch);
    }
    public static TodayInHistoryRespData getTodayInHistory() throws IOException {
        return TodayInHistoryUtil.getTodayInHistory();
    }
    public static TodayInHistoryRespData getDateInHistory(Date date) throws IOException {
        return TodayInHistoryUtil.getDateInHistory(date);
    }
    public static LaoHuangLiRespData getTodayLaoHuangLi() throws IOException {
        return LaoHuangLiUtil.getTodayLaoHuangLi();
    }
    public static LaoHuangLiRespData getDateLaoHuangLi(Date date) throws IOException {
        return LaoHuangLiUtil.getDateLaoHuangLi(date);
    }
    public static JockRespData getNowPreviousJock(int pageIndex, int pageSize) throws IOException {
        return JockUtil.getNowPreviousJock(pageIndex, pageSize);
    }
    public static JockRespData getDatePreviousJock(Date date,int pageIndex, int pageSize) throws IOException {
        return JockUtil.getDatePreviousJock(date, pageIndex, pageSize);
    }
    public static IdiomsRespData getIdiomsData(String idioms) throws IOException {
        return IdiomsUtil.getIdiomsData(idioms);
    }
    public static HotVideoRespData getHotVideos(int size) throws IOException {
        return HotVideoUtil.getHotVideos(size);
    }
    public static ShengChenRespData getNowShengChen() throws IOException {
        return ShengChenUtil.getNowShengChen();
    }

    public static ShengChenRespData getDateShengChen(Date date) throws IOException {
        return ShengChenUtil.getDateShengChen(date);
    }

    public static TelNumRespData getTelNumData(String tel) throws IOException {
        return TelNumUtil.getTelNumData(tel);
    }


    public static DayConstellationRespData getConstellationTodayData(String consName) throws IOException {
        return ConstellationUtil.getConstellationTodayData(consName);
    }

    public static DayConstellationRespData getConstellationTomorrowData(String consName) throws IOException {
        return ConstellationUtil.getConstellationTomorrowData(consName);
    }

    public static WeekConstellationRespData getConstellationWeekData(String consName) throws IOException {
        return ConstellationUtil.getConstellationWeekData(consName);
    }

    public static MonthConstellationRespData getConstellationMonthData(String consName) throws IOException {
        return ConstellationUtil.getConstellationMonthData(consName);
    }

    public static YearConstellationRespData getConstellationYearData(String consName) throws IOException {
        return ConstellationUtil.getConstellationYearData(consName);
    }
}
