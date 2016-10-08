package studio.crazybt.adventure.utils;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.text.util.Linkify;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Brucelee Thanh on 09/10/2016.
 */

public class TextUtil {
    public static boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }
    public static int getWordCount(String str) {
        int i = 0;
        for (int j = 0; j < str.length(); j++) {
            int k = Character.codePointAt(str, j);
            if ((k > 0) && (k <= 255)) {
                i++;
            } else {
                i += 2;
            }
        }
        return i;
    }
    public static String parseTimestampToYMD(String input) {
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = sdf1.parse(input);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            String finalDate = sdf2.format(date);
            return finalDate;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String parseTimestampToMMMDY(String input) {
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = sdf1.parse(input);
            SimpleDateFormat sdf2 = new SimpleDateFormat("MMMM dd, yyyy");
            String finalDate = sdf2.format(date);
            return finalDate;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String getStringTimeFromLong(long date, String format) {
        SimpleDateFormat formater = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        return formater.format(cal.getTime());
    }
    public static Spannable linkifyHtml(String html, int linkifyMask) {
        Spanned text = Html.fromHtml(html);
        URLSpan[] currentSpans = text.getSpans(0, text.length(), URLSpan.class);

        SpannableString buffer = new SpannableString(text);
        Linkify.addLinks(buffer, linkifyMask);

        for (URLSpan span : currentSpans) {
            int end = text.getSpanEnd(span);
            int start = text.getSpanStart(span);
            buffer.setSpan(span, start, end, 0);
        }
        return buffer;
    }
}
