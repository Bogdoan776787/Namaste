package com.via.namaste.databasehelper;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DataHelper extends SQLiteAssetHelper {

    static String DB_NAME = "gym_workout_db";
    static int DB_VERSION = 1;
    public DataHelper(Context context) {
        super(context, DB_NAME, null, null, DB_VERSION);
    }

}