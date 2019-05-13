package com.example.kyrillosnagywadieyas.currencyconverter;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

public class InputFilterEditText implements  InputFilter {
    public static Context context;
    private int min, max;

    public InputFilterEditText(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public InputFilterEditText(String min, String max) {
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe) { }
        Toast.makeText(context, "Please enter a value between 0 and "+ Integer.MAX_VALUE ,Toast.LENGTH_LONG).show();
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
