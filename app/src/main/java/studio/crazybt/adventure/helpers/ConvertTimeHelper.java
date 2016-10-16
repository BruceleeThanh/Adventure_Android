package studio.crazybt.adventure.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Brucelee Thanh on 17/10/2016.
 */

public class ConvertTimeHelper {

    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String UTC_FORMAT = "UTC";
    public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm";
    public static final String TIME_FORMAT = "HH:mm";

    public static final String TODAY = "Hôm nay, ";
    public static final String YESTERDAY = "Hôm qua, ";
    public static final String HOURS_LATER = " giờ trước";
    public static final String MINUTE_LATER = " phút trước";
    public static final String JUST_NOW = "Vừa xong";

    public ConvertTimeHelper() {

    }

    public String convertISODateToString(String timeStamp) {
        DateFormat isoFormat = new SimpleDateFormat(ISO_DATE_FORMAT);
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        isoFormat.setTimeZone(TimeZone.getTimeZone(UTC_FORMAT));
        try {
            Date date = isoFormat.parse(timeStamp);
            String newDate = dateFormat.format(date);
            return newDate;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public String convertISODateToPrettyTimeStamp(String timeStamp) {
        DateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateString = this.convertISODateToString(timeStamp);
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date nowDate = new Date();
        int compareDays = getDateDiff(removeTime(date), removeTime(nowDate), TimeUnit.DAYS);
        if (compareDays == 0) {
            int compareHours = getDateDiff(date, nowDate, TimeUnit.HOURS);
            if(compareHours == 0){
                int compareMinutes = getDateDiff(date, nowDate, TimeUnit.MINUTES);
                if(compareMinutes == 0 || compareMinutes == 1){
                    return JUST_NOW;
                }
                return compareMinutes + MINUTE_LATER;
            }else{
                return compareHours + HOURS_LATER;
            }
        } else {
            if (compareDays == 1) {
                String time = timeFormat.format(date);
                return YESTERDAY + time;
            }
        }
        return dateString;
    }

    /**
     * Get a diff between two dates
     *
     * @param date1    the oldest date
     * @param date2    the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public int getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return (int) timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
