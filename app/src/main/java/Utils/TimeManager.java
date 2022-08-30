package Utils;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeManager {

    public static String getCurrentDate(){
        Calendar now = Calendar.getInstance();
        Date today = now.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = format.format(today);

        return formattedDate;
    }


    public static int  getCurrentYear(){
        Calendar now = Calendar.getInstance();
        Date today = now.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = format.format(today);

        return Integer.parseInt(formattedDate.split("-")[0]);
    }

    public static int  getCurrentMonth(){
        Calendar now = Calendar.getInstance();
        Date today = now.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = format.format(today);

        return Integer.parseInt(formattedDate.split("-")[1]);
    }



    public static String getCurrentTime(){
        Calendar now = Calendar.getInstance();
        Date today = now.getTime();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String formatedTime = format.format(today);

        return formatedTime;
    }

    public static boolean isToday(String date){
        Calendar today = Calendar.getInstance();

        Calendar item_date = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            item_date.setTime(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return today.get(Calendar.YEAR) == item_date.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == item_date.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isYesterday(String date){
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);

        Calendar item_date = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            item_date.setTime(format.parse(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return yesterday.get(Calendar.YEAR) == item_date.get(Calendar.YEAR) &&
                yesterday.get(Calendar.DAY_OF_YEAR) == item_date.get(Calendar.DAY_OF_YEAR);
    }

    public static String getMonthFromInt(int num) {
        String month = "January";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

    public static String getMonthAndDayFromDate(String sdate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(sdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("MMM dd");

        return format.format(date);
    }

}
