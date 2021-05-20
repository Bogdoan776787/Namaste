package com.via.namaste;

import android.content.Context;
import android.content.SharedPreferences;


public class PrefrenceData {

    static String MY_PREF = "myAppPref";
    private static String REST_TIME = "restTime";
    private static String COUNT_DOWN_TIME = "coutdDownTime";



    static int getRestTime(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(REST_TIME, 30);
    }


    static int getCountDownTime(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(COUNT_DOWN_TIME, 15);
    }


    static void setCountDownTime(Context context, int i) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(COUNT_DOWN_TIME, i);
        editor.apply();
    }

    static void setRestTime(Context context, int i) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(REST_TIME, i);
        editor.apply();
    }
}
