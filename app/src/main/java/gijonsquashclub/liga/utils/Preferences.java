package gijonsquashclub.liga.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import gijonsquashclub.liga.R;

public class Preferences {
    public static void saveNumberOfGroupInPreferences(Activity activity,
                                                      int numberOfGroup) {
        SharedPreferences sharedPref = activity.getSharedPreferences(
                activity.getString(R.string.preferenceFileKey),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (numberOfGroup != 0) {
            editor.putInt(activity.getString(R.string.group), numberOfGroup);
            editor.apply();
        }
    }

    public static int getNumberOfGroupFromPreferences(Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(
                activity.getString(R.string.preferenceFileKey),
                Context.MODE_PRIVATE);
        return sharedPref.getInt(activity.getString(R.string.group), 0);
    }
}
