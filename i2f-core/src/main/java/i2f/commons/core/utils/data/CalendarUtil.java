package i2f.commons.core.utils.data;

/**
 * @author ltb
 * @date 2021/11/4
 */
public class CalendarUtil {
    public static final int CRITERION_YEAR = 2021;
    public static final int CRITERION_MONTH = 11;
    public static final int CRITERION_DAY = 4;
    public static final int CRITERION_WEEK = 4;
    public static final String[] weekIdxDesc = {null, "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
    public static final int CRITERION_CN_SHU = 2;
    public static final String[] cnShuIdxDesc = {null, "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
    public static final int CRITERION_CN_YEAR = 38;

    public static final String[] gzIdxDesc = {null
            , "甲子", "乙丑", "丙寅", "丁卯", "戊辰", "己巳", "庚午", "辛未", "壬申", "癸酉"
            , "甲戌", "乙亥", "丙子", "丁丑", "戊寅", "己卯", "庚辰", "辛巳", "壬午", "癸未"
            , "甲申", "乙酉", "丙戌", "丁亥", "戊子", "己丑", "庚寅", "辛卯", "壬辰", "癸巳"
            , "甲午", "乙未", "丙申", "丁酉", "戊戌", "己亥", "庚子", "辛丑", "壬寅", "癸卯"
            , "甲辰", "乙巳", "丙午", "丁未", "戊申", "己酉", "庚戌", "辛亥", "壬子", "癸丑"
            , "甲寅", "乙卯", "丙辰", "丁巳", "戊午", "己未", "庚申", "辛酉", "壬戌", "癸亥"};


    public static DateMeta getCriterionDate() {
        return new DateMeta(CRITERION_YEAR, CRITERION_MONTH, CRITERION_DAY, CRITERION_WEEK);
    }

    /**
     * 获取年有多少天
     *
     * @param year
     * @return
     */
    public static long getDaysOnYear(int year) {
        if (DateUtil.isLeapYear(year)) {
            return 366;
        }
        return 365;
    }

    /**
     * 获取月有多少天
     *
     * @param year
     * @param month
     * @return
     */
    public static long getDaysOnMonth(int year, int month) {
        long ret = 0;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                ret = 31;
                break;
            case 2:
                if (DateUtil.isLeapYear(year)) {
                    ret = 29;
                } else {
                    ret = 28;
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                ret = 30;
                break;
        }
        return ret;
    }

    /**
     * 获取此日期是这年的多少天
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static long getDaysInYear(int year, int month, int day) {
        long days = 0;
        for (int i = 1; i < month; i++) {
            long pday = getDaysOnMonth(year, i);
            days += pday;
        }
        days += day;
        return days;
    }

    public static long getDiffDays(DateMeta date1, DateMeta date2) {
        long indays = getDaysInYear(date1.year, date1.month, date1.day);
        long crdays = getDaysInYear(date2.year, date2.month, date2.day);
        int yearstep = date2.year - date1.year;
        if (yearstep >= 0) {
            yearstep = 1;
        } else {
            yearstep = -1;
        }
        long days = 0;
        for (int i = date1.year; i != date2.year; i += yearstep) {
            days += getDaysOnYear(i);
        }
        return days + indays - crdays;
    }

    public static long getDiffDaysRefCriterion(int year, int month, int day) {
        DateMeta date1 = new DateMeta(year, month, day);
        DateMeta date2 = getCriterionDate();
        return getDiffDays(date1, date2);
    }

    public static int getWeek(int year, int month, int day) {
        return getProxyLoopDayValue(year, month, day, CRITERION_WEEK, 7);
    }

    public static int getCnShu(int year) {
        return getProxyLoopYearValue(year, CRITERION_CN_SHU, 12);
    }


    public static int getCnYear(int year) {
        return getProxyLoopYearValue(year, CRITERION_CN_YEAR, 60);
    }

    public static int getCnDay(int year, int month, int day) {
        return -1;
    }

    private static int getProxyLoopYearValue(int year, int criterion, int loopSize) {
        long diffCriDays = getDiffYearsRefCriterion(year);
        int lp = getLoopValue(diffCriDays,criterion, loopSize);
        return lp;
    }

    private static long getDiffYearsRefCriterion(int year) {
        return (long)(year-CRITERION_YEAR);
    }

    private static int getProxyLoopMonthValue(int year, int month, int criterion, int loopSize) {
        long diffCriDays = getDiffMonthsRefCriterion(year, month);
        int lp = getLoopValue(diffCriDays,criterion, loopSize);
        return lp;
    }

    private static long getDiffMonthsRefCriterion(int year, int month) {
        int yearstep = year - CRITERION_YEAR;
        if (yearstep >= 0) {
            yearstep = 1;
        } else {
            yearstep = -1;
        }
        long days = 0;
        for (int i = year; i != CRITERION_YEAR; i += yearstep) {
            days += 12;
        }
        return days + month - CRITERION_MONTH;
    }

    private static int getProxyLoopDayValue(int year, int month, int day, int criterion, int loopSize) {
        long diffCriDays = getDiffDaysRefCriterion(year, month, day);
        int lp = getLoopValue(diffCriDays,criterion, loopSize);
        return lp;
    }

    private static int getLoopValue(long diff,int criterion, int loopSize) {
        long lvp = diff % loopSize;
        int lp = (int) (criterion + lvp);
        if (lp < 1) {
            lp += loopSize;
        }
        if (lp > loopSize) {
            lp -= loopSize;
        }
        return lp;
    }

    public static DateMeta getDate(int year, int month, int day) {
        DateMeta date = new DateMeta(year, month, day);
        date.week = getWeek(year, month, day);
        date.cnShu = getCnShu(year);
        date.cnYear = getCnYear(year);
        if (date.week >= 1 && date.week <= 7) {
            date.weekDesc = weekIdxDesc[date.week];
        }
        if (date.cnShu >= 1 && date.cnShu <= 12) {
            date.cnShuDesc = cnShuIdxDesc[date.cnShu];
        }
        if (date.cnYear >= 1 && date.cnYear <= 60) {
            date.cnYearDesc = gzIdxDesc[date.cnYear];
        }

        return date;
    }

    public static DateMeta nextDay(DateMeta date) {
        int year = date.year;
        int month = date.month;
        int day = date.day;
        long mdays = getDaysOnMonth(year, month);
        if (day == mdays) {
            day = 1;
            if (month == 12) {
                month = 1;
                year++;
            } else {
                month++;
            }
        } else {
            day++;
        }
        return getDate(year, month, day);
    }

    public static DateMeta previousDay(DateMeta date) {
        int year = date.year;
        int month = date.month;
        int day = date.day;
        if (day == 1) {
            if (month == 1) {
                day = 31;
                month = 12;
                year--;
            } else {
                month--;
                long mdays = getDaysOnMonth(year, month);
                day = (int) mdays;
            }
        } else {
            day--;
        }
        return getDate(year, month, day);
    }

    public static DateMeta addDays(DateMeta date, long days) {
        DateMeta ret = getDate(date.year, date.month, date.day);
        if (days == 0) {
            return ret;
        }
        long step = days >= 0 ? 1 : -1;
        for (long i = 0; i != days; i += step) {
            if (step > 0) {
                ret = nextDay(ret);
            } else {
                ret = previousDay(ret);
            }
        }
        return ret;
    }

    public static DateMeta addWeeks(DateMeta date, long weeks) {
        long days = weeks * 7;
        return addDays(date, days);
    }

    public static DateMeta nextMonth(DateMeta date) {
        int year = date.year;
        int month = date.month;
        int day = date.day;
        if (month == 12) {
            month = 1;
            year++;
        } else {
            month++;
            long mdays = getDaysOnMonth(year, month);
            if (day > mdays) {
                month++;
                day = (int) ((long) day - mdays);
            }
        }
        return getDate(year, month, day);
    }

    public static DateMeta previousMonth(DateMeta date) {
        int year = date.year;
        int month = date.month;
        int day = date.day;
        if (month == 1) {
            month = 12;
            year--;
        } else {
            month--;
            long mdays = getDaysOnMonth(year, month);
            if (day > mdays) {
                month++;
                day = (int) ((long) day - mdays);
            }
        }
        return getDate(year, month, day);
    }

    public static DateMeta addMonths(DateMeta date, long months) {
        DateMeta ret = getDate(date.year, date.month, date.day);
        if (months == 0) {
            return ret;
        }
        long step = months >= 0 ? 1 : -1;
        for (long i = 0; i != months; i += step) {
            if (step == 1) {
                ret = nextMonth(ret);
            } else {
                ret = previousMonth(ret);
            }
        }
        return ret;
    }

    public static DateMeta addYears(DateMeta date, long years) {
        int year = date.year;
        int month = date.month;
        int day = date.day;
        year += (int) years;
        if (month == 2) {
            long mdays = getDaysOnMonth(year, month);
            if (day > mdays) {
                month++;
                day = (int) ((long) day - mdays);
            }
        }
        return getDate(year, month, day);
    }

    public static DateMeta inflateDate(int year, int month, int day) {
        DateMeta date = getDate(year, 1, 1);
        month = month - 1;
        day = day - 1;
        date = addMonths(date, month);
        date = addDays(date, day);
        return date;
    }
}
