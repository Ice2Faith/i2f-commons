package i2f.commons.core.utils.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtil {
    public static final String FMT_DATE_TIME_MILL="yyyy-MM-dd HH:mm:ss SSS";
    public static ThreadLocal<SimpleDateFormat> formatter=ThreadLocal.withInitial(()->{
        return new SimpleDateFormat(FMT_DATE_TIME_MILL);
    });
    public static String[] SUPPORT_PARSE_DATE_FORMATS={
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd",
            "yyyy-MM-dd HH:mm:ss SSS",
            "yyyy-MM",
            "yyyyMMdd",
            "yyyyMM",
            "yyyyMMddHH",
            "yyyy/MM/dd HH:mm:ss",
            "yyyy/MM/dd",
            "yyyy年MM月dd日 HH时mm分ss秒",
            "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd HH",
            "yyyy"
    };
    public static Date from(String date){
        for(String item : SUPPORT_PARSE_DATE_FORMATS){
            SimpleDateFormat fmt=new SimpleDateFormat(item);
            try{
                return fmt.parse(date);
            }catch(Exception e){

            }
        }
        return null;
    }
    public static Date from(String date,String patten){
        SimpleDateFormat fmt=new SimpleDateFormat(patten);
        Date ret=null;
        try{
            ret=fmt.parse(date);
        }catch(ParseException e){

        }
        return ret;
    }
    public static Date parse(String date) throws ParseException {
        return parse(date,FMT_DATE_TIME_MILL);
    }
    public static Date parse(String date,String patten) throws ParseException {
        SimpleDateFormat fmt=new SimpleDateFormat(patten);
        return fmt.parse(date);
    }
    public static String format(Date date){
        return formatter.get().format(date);
    }
    public static String format(Date date,String patten){
        SimpleDateFormat fmt=new SimpleDateFormat(patten);
        return fmt.format(date);
    }
    public static Date addMillSeconds(Date date,long millSeconds){
        long time=date.getTime();
        return new Date(time+millSeconds);
    }
    public static Date addSeconds(Date date,long seconds){
        long time=date.getTime();
        return new Date(time+seconds2(seconds));
    }
    public static Date addMinus(Date date,long minus){
        long time=date.getTime();
        return new Date(time+minus2(minus));
    }
    public static Date addHours(Date date,long hours){
        long time=date.getTime();
        return new Date(time+hours2(hours));
    }
    public static Date addDays(Date date,long days){
        long time=date.getTime();
        return new Date(time+days2(days));
    }
    public static Date addWeek(Date date,long weeks){
        long time=date.getTime();
        return new Date(time+week2(weeks));
    }
    public static Date addMonth(Date date,int months){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,months);
        return calendar.getTime();
    }
    public static Date addYear(Date date,int years){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR,years);
        return calendar.getTime();
    }
    public static Date add(Date date, long times, TimeUnit unit){
        long time=date.getTime();
        long fac=unit.toMillis(times);
        return new Date(time+fac);
    }
    public static long diff(Date date1,Date date2){
        return (date1.getTime()-date2.getTime());
    }
    public static long toSeconds(long millSeconds){
        return millSeconds/1000;
    }
    public static long toMinus(long millSeconds){
        return millSeconds/1000/60;
    }
    public static long toHours(long millSeconds){
        return millSeconds/1000/60/60;
    }
    public static long toDays(long millSeconds){
        return millSeconds/1000/60/60/24;
    }
    public static long seconds2(long seconds){
        return seconds*1000;
    }
    public static long minus2(long minus){
        return minus*1000*60;
    }
    public static long hours2(long hours){
        return hours*1000*60*60;
    }
    public static long days2(long days){
        return days*1000*60*60*24;
    }
    public static long week2(long weeks){
        return weeks*1000*60*60*24*7;
    }

    public static boolean isLeapYear(int year){
        if(year%400==0 || (year%100==0 && year%4!=0)){
            return true;
        }
        return false;
    }

    public static boolean isLegalDate(int year,int month,int day){
        if(month<1 || month>12){
            return false;
        }
        if(day<1||day>31){
            return false;
        }
        switch (month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                if(day>31){
                    return false;
                }
                break;
            case 2:
                if(isLeapYear(year)){
                    if(day>29){
                        return false;
                    }
                }else{
                    if(day>28){
                        return false;
                    }
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                if(day>30){
                    return false;
                }
                break;
        }
        return true;
    }
}
