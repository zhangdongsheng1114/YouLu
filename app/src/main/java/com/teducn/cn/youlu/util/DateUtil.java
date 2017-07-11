package com.teducn.cn.youlu.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by tarena on 2017/7/10.
 */

public class DateUtil {
    public static int dayDiff(long stamp) {
        //获得当前系统的时间
        // long currentTimeMillis=System.currentTimeMillis();
        //获得当前系统的日历对象
        Calendar calendar1 = Calendar.getInstance();
        //Date date=new Date();
        //获得通话时间的日历对象
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(stamp);
        int diff = calendar1.get(Calendar.DAY_OF_YEAR) - calendar2.get(Calendar.DAY_OF_YEAR);
        return diff;
    }

    public static String formatDate(long stamp) {
        String dateStr = "";
        int daydiff = dayDiff(stamp);
        if (daydiff == 0) {
            //通话时间是当天
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            dateStr = dateFormat.format(new Date(stamp));
        } else if (daydiff <= 1) {
            //通话时间是昨天
            SimpleDateFormat dateFormat = new SimpleDateFormat("昨天 HH:mm:ss");
            dateStr = dateFormat.format(new Date(stamp));
        } else if (daydiff <= 7) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(stamp);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            switch (dayOfWeek) {
                case Calendar.MONDAY:
                    dateStr = "星期一";
                    break;
                case Calendar.TUESDAY:
                    dateStr = "星期二";
                    break;
                case Calendar.WEDNESDAY:
                    dateStr = "星期三";
                    break;
                case Calendar.THURSDAY:
                    dateStr = "星期四";
                    break;
                case Calendar.FRIDAY:
                    dateStr = "星期五";
                    break;
                case Calendar.SATURDAY:
                    dateStr = "星期六";
                    break;
                case Calendar.SUNDAY:
                    dateStr = "星期日";
                    break;
            }
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateStr = dateFormat.format(stamp);
        }
        return dateStr;
    }

    public static String formatDate2(long stamp) {
        String dateStr = "";
        int diff = dayDiff(stamp);
        if (diff == 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            dateStr = dateFormat.format(new Date(stamp));
        } else if (diff == 1) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("昨天 HH:mm:ss");
            dateStr = dateFormat.format(new Date(stamp));
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateStr = dateFormat.format(new Date(stamp));
        }
        return dateStr;
    }
}
