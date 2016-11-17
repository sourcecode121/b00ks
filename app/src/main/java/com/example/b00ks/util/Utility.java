package com.example.b00ks.util;

import android.os.Build;
import android.text.Html;

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
}
