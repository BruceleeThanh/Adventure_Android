package studio.crazybt.adventure.utils;

import android.app.Activity;
import android.database.DatabaseUtils;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Brucelee Thanh on 09/10/2016.
 */

    public class StringUtil {

        public static final String DATE_FORMAT_1 = "yyyy-MM-dd";

        public static final String DATE_FORMAT_2 = "MMM yyyy";

        public static final String DATE_FORMAT_3 = "MMM dd, yyyy h:mm a";

        public static final String DATE_FORMAT_4 = "MMM dd, yyy";

        public static final String DATE_FORMAT_5 = "h:mm a";

        public static final String DATE_FORMAT_6 = "MMMM dd, yyyy";

        public static final String DATE_FORMAT_7 = "MM/dd/yyyy";

        public static final String DATE_FORMAT_8 = "dd MMM HH:mm";

        public static final String DATE_FORMAT_9 = "yyyy-MM-dd'T'HH:mm:ss.SSS";

        public static final String DATE_FORMAT_10 = "yyyy-MM-dd'T'HH:mm:ss";

        public static final String DATE_FORMAT_11 = "dd MMMM, 'kl' HH:mm";

        public static final String DATE_FORMAT_12 = "dd MMM";

        public static final String DATE_FORMAT_13 = "dd MMM, 'kl' HH:mm";

        public static final String DATE_FORMAT_14 = "dd MMM yyyy";

        public static final String DATE_FORMAT_15 = "d-MMM";

        public static final String DATE_FORMAT_16 = "hh:mm";

        public static final String DATE_FORMAT_17 = "dd MMMM";

        public static final String DATE_FORMAT_18 = "HH:mm";

        public static final String DATE_FORMAT_19 = "MMMM";

        public static final String DATE_FORMAT_20 = "MM";

        public static final String DATE_FORMAT_21 = "yyyy-MM-dd HH:mm:ss Z";

        public static final String DATE_FORMAT_22 = "MMMM yyyy";

        public static final String DATE_FORMAT_24 = "dd/MM";

        public static final String DATE_FORMAT_23 = "MM/yy";

        public static final String DATE_FORMAT_25 = "d MMM yyyy";

        public static final String DATE_FORMAT_26 = "EEEE dd MMMM";

        public static final String DATE_FORMAT_27 = "yyyy-MM-dd'T'HH:mm:ss";

        public static final String HTML_TAG_PATTERN = "<(\"[^\"]*\"|'[^']*'|[^'\">])*>";

        public static final String TIMESTAMP_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

        /**
         * Convert a date string to a date
         *
         * @param time
         * @return return date if time is right format, else return null
         */
        public static Date convertStringToDate(String time, String format) {
            SimpleDateFormat dateformat = new SimpleDateFormat(format);
            Date date = null;
            try {
                date = dateformat.parse(time);
                return date;
            } catch (ParseException e) {
            }
            return date;
        }

        /**
         * Convert date to string
         *
         * @param date
         * @param format
         * @return
         */
        public static String convertDateToString(Date date, String format) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format,
                    Locale.ENGLISH);
            String strDate = null;
            try {
                strDate = dateFormat.format(date);
            } catch (IllegalArgumentException e) {
            }
            return strDate;
        }

        /**
         * Convert date to string GMT
         *
         * @param date
         * @param format
         * @return
         */
        public static String convertDateToStringGMT(Date date, String format) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format,
                    Locale.ENGLISH);
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
            String strDate = null;
            try {
                strDate = dateFormat.format(date);
            } catch (IllegalArgumentException e) {
            }
            return strDate;
        }

        /**
         * Convert date to string
         *
         * @param date
         * @param format
         * @return
         */
        public static String convertDateToString(Date date, String format,
                                                 Locale locale) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, locale);
            String strDate = null;
            try {
                strDate = dateFormat.format(date);
            } catch (IllegalArgumentException e) {
            }
            return strDate;
        }

        /**
         * Convert string to calendar
         *
         * @param time
         * @return return calendar if time is right format, else return null
         */
        public static Calendar convertStringToCalendar(String time, String format) {
            Calendar calendar = null;
            SimpleDateFormat dateformat = new SimpleDateFormat(format);
            Date date = null;
            try {
                date = dateformat.parse(time);
                calendar = Calendar.getInstance();
                calendar.setTime(date);
            } catch (ParseException e) {
            }
            return calendar;
        }

        /**
         * Convert calendar to String
         *
         * @param calendar
         * @param format
         * @return
         */
        public static String convertCalendarToString(Calendar calendar,
                                                     String format) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            String strDate = null;
            try {
                strDate = dateFormat.format(calendar.getTime());
            } catch (IllegalArgumentException e) {

            }
            return strDate;
        }

        /**
         * Convert a time stamp string to a time stamp
         *
         * @param time
         * @return
         */
        public static Timestamp convertStringToTimeStamp(String time, String format) {
            SimpleDateFormat dateformat = new SimpleDateFormat(format);
            Date date = null;
            try {
                date = dateformat.parse(time);
                return new Timestamp(date.getTime());
            } catch (ParseException e) {
                return new Timestamp(Calendar.getInstance().getTime().getTime());
            }
        }

        /**
         * Convert current date time to string
         *
         * @return
         */
        public static String convertNowToString(String format) {
            SimpleDateFormat dateformat = new SimpleDateFormat(format);
            dateformat.setTimeZone(TimeZone.getTimeZone("GMT"));
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            return dateformat.format(calendar.getTime());
        }

        /**
         * Convert current date time to default babble name
         *
         * @return
         */
        public static String convertNowToTimeStampString(String format) {
            SimpleDateFormat dateformat = new SimpleDateFormat(format);
            return dateformat.format(Calendar.getInstance().getTime());
        }


        /**
         * Check Edit Text input string
         *
         * @param editText
         * @return
         */
        public static boolean isEmpty(EditText editText) {
            if (editText == null || editText.getEditableText() == null
                    || editText.getEditableText().toString().trim().length() == 0) {
                return true;
            }
            return false;
        }

        public static String sqlQuote(String string) {
            string = string.trim();
            string = DatabaseUtils.sqlEscapeString(string);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(string);
            return stringBuffer.toString();
        }

        /**
         * @param html
         * @return
         */
        public static String removeAllTagHtml(String html) {
            Pattern tags = Pattern.compile("</?[^>]+>");
            Matcher match = tags.matcher(html); // here you specify the string you
            // want to modify (HTML)
            String result = match.replaceAll("");
            return result;
        }

        /**
         * @param string
         * @return
         */
        public static String generateKey(String string) {
            char[] chars = string.toUpperCase().toCharArray();
            StringBuffer hex = new StringBuffer();
            for (int i = 0; i < chars.length; i++) {
                hex.append(Integer.toHexString((int) chars[i]));
            }
            return hex.toString();
        }

        /**
         * Read html file content
         *
         * @param activity
         * @param fileName
         * @return
         */
        public static CharSequence convertHtmlFileToString(Activity activity,
                                                           String fileName) {
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(activity.getAssets()
                        .open(fileName)));
                String line = "";
                StringBuffer buffer = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    buffer.append(line);
                }
                return buffer;
            } catch (IOException e) {
                return "";
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ex) {
                    }
                }
            }
        }

        /**
         * get last modified date of file
         *
         * @param file
         * @return
         */
        public static Date getLastModifiedDate(File file) {
            Date date = null;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(file.lastModified());
            date = calendar.getTime();
            return date;
        }

        /**
         * get current date setting
         *
         * @return
         */
        public static Date getCurrentDate() {
            Date date = null;
            Calendar calendar = Calendar.getInstance();
            date = calendar.getTime();
            return date;
        }

        /**
         * @param calendar
         * @return
         */
        public static String getNameOfMonth(Calendar calendar) {
            return new SimpleDateFormat("MMM").format(calendar.getTime());
        }

        /**
         * @param month
         * @return
         */
        public static String getMonth(int month) {
            return new DateFormatSymbols().getMonths()[month - 1];
        }

        /**
         * @return
         */
        public static String randomString() {
            String uuid = UUID.randomUUID().toString();
            return uuid;
        }

        /**
         * @param url
         * @return
         */
        public static String getFileNameInUrl(String url) {

            if (url.length() == 0 || !url.contains("/")) {
                return "";
            }

            return url.substring(url.lastIndexOf('/') + 1, url.length());
        }

        /**
         * @param url
         * @return
         */
        public static String getFileNameWithoutExtensionInUrl(String url) {

            if (url.length() == 0 || !url.contains("/")) {
                return "";
            }

            String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());

            return fileName.substring(0, fileName.lastIndexOf('.'));
        }

        /**
         * @param duration
         * @return
         */
        public static String getDurationTime(long duration) {
            StringBuilder builder = new StringBuilder();

            long days = TimeUnit.MILLISECONDS.toDays(duration);
            duration -= TimeUnit.DAYS.toMillis(days);

            long hours = TimeUnit.MILLISECONDS.toHours(duration);
            duration -= TimeUnit.HOURS.toMillis(hours);

            long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
            duration -= TimeUnit.MINUTES.toMillis(minutes);

            long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);

            if (days != 0) {
                builder.append(days + "d");
                builder.append(" ");
            }

            if (hours != 0) {
                builder.append(hours + "h");
                builder.append(" ");
            }

            if (minutes != 0) {
                builder.append(minutes + "m");
                builder.append(" ");
            }

            if (seconds != 0) {
            }

            return builder.toString();
        }

        public static String getCookie(String cookies, String cookieName) {
            String CookieValue = null;
            if (cookies == null)
                return null;

            String[] temp = cookies.split("(;)|(&)|(\")");

            for (String ar1 : temp) {
                if (ar1.contains(cookieName)) {
                    String[] temp1 = ar1.split("=");
                    CookieValue = temp1[1];
                }
            }
            return CookieValue;
        }
    }

