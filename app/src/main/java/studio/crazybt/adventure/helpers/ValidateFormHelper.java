package studio.crazybt.adventure.helpers;

import android.widget.TextView;

/**
 * Created by Brucelee Thanh on 10/10/2016.
 */

public class ValidateFormHelper {

    public static boolean isEmptyTextView(TextView textView, String msg) {
        if (textView.getText().toString() == "") {
            textView.setError(msg);
            return true;
        }
        return false;
    }
}
