package app.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by yejinbing on 15/10/23.
 */
public class ViewUtils {

    public static void setDrawingCacheRecursion(View v, boolean enabled) {
        v.setDrawingCacheEnabled(enabled);
        if (v instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) v).getChildCount(); i++) {
                setDrawingCacheRecursion(((ViewGroup) v).getChildAt(i), enabled);
            }
        }
    }

    public static void showSoftKeyboard(View view) {
        InputMethodManager imm =
                (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    public static void hideSoftKeyboard(View v) {
        InputMethodManager imm =
                (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}
