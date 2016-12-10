package com.example.b00ks.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Anand on 17/11/2016.
 */

public class Utility {

    @SuppressWarnings("deprecation")
    public static String removeHtmlTags(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY).toString().trim();
        }
        else {
            return Html.fromHtml(source).toString().trim();
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
