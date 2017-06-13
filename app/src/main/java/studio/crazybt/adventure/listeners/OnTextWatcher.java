package studio.crazybt.adventure.listeners;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Brucelee Thanh on 14/03/2017.
 */

public abstract class OnTextWatcher implements TextWatcher{

    private int position;

    public void updatePosition(int position) {
        this.position = position;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
}
