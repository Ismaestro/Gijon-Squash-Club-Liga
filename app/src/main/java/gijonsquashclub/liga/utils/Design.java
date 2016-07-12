package gijonsquashclub.liga.utils;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import gijonsquashclub.liga.R;

public class Design {

    public static final String ROBOTO_REGULAR_FONT = "fonts/Roboto-Regular.ttf";

    public static void setStatusBarColor(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(activity.getResources().getColor(
                R.color.lightRed));
    }

    public static void setFont(View view) {
        Typeface typeface = Typeface.createFromAsset(view.getContext()
                .getAssets(), ROBOTO_REGULAR_FONT);
        ViewGroup allViews = (ViewGroup) view;
        int childCount = allViews.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = allViews.getChildAt(i);
            if (v instanceof TextView) {
                TextView textView = (TextView) v;
                textView.setTypeface(typeface);
            }
        }
    }
}
