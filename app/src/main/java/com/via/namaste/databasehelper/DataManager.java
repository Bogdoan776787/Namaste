package com.via.namaste.databasehelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.via.namaste.Config;
import com.via.namaste.models.ModelCategoryData;
import com.via.namaste.models.ModelCustomExercise;
import com.via.namaste.models.ModelCustomExerciseRest;
import com.via.namaste.models.ModelExerciseDetail;
import com.via.namaste.models.ModelHistory;
import com.via.namaste.models.ModelMainCategory;
import com.via.namaste.models.ReminderModel;

import java.util.ArrayList;
import java.util.List;


public class DataManager {

    public static DataManager dataManager;
    SQLiteDatabase database;
    DataHelper dataHelper;


    public DataManager(Context database) {
        dataHelper = new DataHelper(database);
    }

    public static DataManager getInstance(Context context) {
        if (dataManager == null) {
            dataManager = new DataManager(context);
        }
        return dataManager;
    }


    public ModelExerciseDetail getExerciseDetailById(int i) {
        database = dataHelper.getWritableDatabase();

        @SuppressLint("Recycle") Cursor cursor = database.rawQuery("SELECT * FROM tbl_exercise_detail WHERE id=" + i, null);
        ModelExerciseDetail modelExerciseDetail = new ModelExerciseDetail();
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    modelExerciseDetail.id = cursor.getInt(cursor.getColumnIndex("id"));
                    modelExerciseDetail.exercise_detail = cursor.getString(cursor.getColumnIndex("exercise_detail"));
                    modelExerciseDetail.exercise_img = cursor.getString(cursor.getColumnIndex("exercise_img"));
                    modelExerciseDetail.exercise_name = cursor.getString(cursor.getColumnIndex("exercise_name"));
                    modelExerciseDetail.duration_type = cursor.getString(cursor.getColumnIndex("duration_type"));

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return modelExerciseDetail;
    }

    private int checkReminder(String s) {
        database = dataHelper.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = database.rawQuery("SELECT * FROM tbl_reminder WHERE time='" + s + "'", null);
        int count = cursor.getCount();
        cursor.close();
        return count;

    }

    public List<String> getReminderTimeList() {
        database = dataHelper.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = database.rawQuery("SELECT time FROM tbl_reminder", null);
        List<String> reminderModelList = new ArrayList<>();
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    reminderModelList.add(cursor.getString(cursor.getColumnIndex("time")));
                }
                while (cursor.moveToNext());
            }
        }
        cursor.close();
        return reminderModelList;
    }


    public void deleteReminder(int id) {
        database = dataHelper.getWritableDatabase();
        try {
            String EXECSql = "DELETE FROM tbl_reminder WHERE id=" + id;
            database.execSQL(EXECSql);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("db_err==", "" + e.getMessage());
        }
    }


    public void insertReminder(String time, String repeat, boolean isOn) {
        database = dataHelper.getWritableDatabase();
        int i = checkReminder(time);
        if (i == 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("time", time);
            contentValues.put("repeat", repeat);
            contentValues.put("ison", isOn);
            database.insert("tbl_reminder", null, contentValues);
        }
    }


    public List<ReminderModel> getReminderData() {
        database = dataHelper.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = database.rawQuery("SELECT * FROM tbl_reminder", null);
        List<ReminderModel> reminderModelList = new ArrayList<>();
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    ReminderModel reminderModel = new ReminderModel();
                    reminderModel.id = cursor.getInt(cursor.getColumnIndex("id"));
                    reminderModel.time = cursor.getString(cursor.getColumnIndex("time"));
                    reminderModel.repeat = cursor.getString(cursor.getColumnIndex("repeat"));
                    reminderModel.ison = cursor.getString(cursor.getColumnIndex("ison"));
                    reminderModelList.add(reminderModel);
                }
                while (cursor.moveToNext());
            }
        }
        cursor.close();
        return reminderModelList;
    }

    public void updateIsON(int id, String value) {
        try {
            database = dataHelper.getWritableDatabase();
            String ExecSql = "UPDATE tbl_reminder SET ison=" + value + " WHERE id=" + id;
            database.execSQL(ExecSql);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("db_err==", "" + e.getMessage());
        }
    }


    public double addDailyWorkoutData(String titles, String descr) {
        database = dataHelper.getWritableDatabase();
        double ids;
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", titles);
        contentValues.put("description", descr);
        ids = database.insert("tbl_custom_workout", null, contentValues);
        return ids;
    }

    public ModelCustomExerciseRest getRestByExercise(int exerc_id, int workoutId) {
        database = dataHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM tbl_custom_exercise_duration WHERE exercise_id=" + exerc_id + " AND workout_id=" + workoutId, null);
        ModelCustomExerciseRest exerciseRest = new ModelCustomExerciseRest();
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    exerciseRest.id = cursor.getInt(cursor.getColumnIndex("id"));
                    exerciseRest.exercise_id = cursor.getInt(cursor.getColumnIndex("exercise_id"));
                    exerciseRest.duration = cursor.getString(cursor.getColumnIndex("duration"));
                    exerciseRest.workout_id = cursor.getInt(cursor.getColumnIndex("workout_id"));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return exerciseRest;

    }

    public ModelCustomExerciseRest getRestByRestIds(int rest_id) {
        database = dataHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM tbl_custom_exercise_duration WHERE id=" + rest_id, null);
        ModelCustomExerciseRest exerciseRest = new ModelCustomExerciseRest();
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    exerciseRest.id = cursor.getInt(cursor.getColumnIndex("id"));
                    exerciseRest.exercise_id = cursor.getInt(cursor.getColumnIndex("exercise_id"));
                    exerciseRest.duration = cursor.getString(cursor.getColumnIndex("duration"));
                    exerciseRest.workout_id = cursor.getInt(cursor.getColumnIndex("workout_id"));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return exerciseRest;

    }

    public void updateRestData(int id, int rest) {
        database = dataHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        String where = "id=" + id;
        contentValues.put("duration", rest);
        database.update("tbl_custom_exercise_duration", contentValues, where, null);
    }


    public ModelCustomExercise getCustomExerciseById(int id) {
        database = dataHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM tbl_custom_workout WHERE id=" + id, null);
        ModelCustomExercise modelDailyExercise = new ModelCustomExercise();
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    modelDailyExercise.exercise = cursor.getString(cursor.getColumnIndex("exercise"));
                    modelDailyExercise.id = cursor.getInt(cursor.getColumnIndex("id"));
                    modelDailyExercise.title = cursor.getString(cursor.getColumnIndex("title"));
                    modelDailyExercise.description = cursor.getString(cursor.getColumnIndex("description"));
                    modelDailyExercise.duration = cursor.getString(cursor.getColumnIndex("duration"));

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return modelDailyExercise;
    }

    public void updateRestTime(int exe_id, int rest_time) {
        database = dataHelper.getWritableDatabase();

        String where = "id=" + exe_id;
        ContentValues contentValues = new ContentValues();
        contentValues.put("duration", rest_time);
        database.update("tbl_custom_workout", contentValues, where, null);
    }

    public void updateTitle(int exe_id, String gettitle) {
        database = dataHelper.getWritableDatabase();

        String where = "id=" + exe_id;
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", gettitle);
        database.update("tbl_custom_workout", contentValues, where, null);
    }


    public void deleteSelectedWorkout(int id) {
        database = dataHelper.getWritableDatabase();

        try {
            String sqls = "DELETE FROM tbl_custom_workout WHERE id=" + id;
            database.execSQL(sqls);
            String sqls_second = "DELETE FROM tbl_custom_exercise_duration WHERE exercise_id=" + id;
            database.execSQL(sqls_second);
        } catch (SQLException ignored) {

        }

    }

    public void addSelectedExerciseById(int ids, String sdata) {
        database = dataHelper.getWritableDatabase();

        String where = "id=" + ids;
        ContentValues contentValues = new ContentValues();
        contentValues.put("exercise", sdata);
        database.update("tbl_custom_workout", contentValues, where, null);

    }

    public void addRestData(int exer_id, int rest, int workout_ids) {
        database = dataHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("exercise_id", exer_id);
        contentValues.put("duration", rest);
        contentValues.put("workout_id", workout_ids);
        database.insert("tbl_custom_exercise_duration", null, contentValues);
    }

    public List<ModelExerciseDetail> getAllExercise() {
        database = dataHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM tbl_exercise_detail", null);
        List<ModelExerciseDetail> modelExerciseDetails = new ArrayList<>();
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    ModelExerciseDetail modelExerciseDetail = new ModelExerciseDetail();
                    modelExerciseDetail.id = cursor.getInt(cursor.getColumnIndex("id"));
                    modelExerciseDetail.exercise_detail = cursor.getString(cursor.getColumnIndex("exercise_detail"));
                    modelExerciseDetail.exercise_name = cursor.getString(cursor.getColumnIndex("exercise_name"));
                    modelExerciseDetail.exercise_img = cursor.getString(cursor.getColumnIndex("exercise_img"));
                    modelExerciseDetail.duration_type = cursor.getString(cursor.getColumnIndex("duration_type"));
                    modelExerciseDetails.add(modelExerciseDetail);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return modelExerciseDetails;
    }

    public List<ModelHistory> getAllHistory(String getDate) {
        database = dataHelper.getWritableDatabase();

        @SuppressLint("Recycle") Cursor cursor = database.rawQuery("SELECT * FROM tbl_history WHERE date='" + getDate + "'", null);
        List<ModelHistory> historyList = new ArrayList<>();
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    ModelHistory history = new ModelHistory();
                    history.id = cursor.getInt(cursor.getColumnIndex("id"));
                    history.date = cursor.getString(cursor.getColumnIndex("date"));
                    history.day = cursor.getInt(cursor.getColumnIndex("day"));
                    history.exercise_id = cursor.getInt(cursor.getColumnIndex("exercise_id"));
                    history.week = cursor.getInt(cursor.getColumnIndex("week"));
                    historyList.add(history);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return historyList;
    }

    public void deleteRestDataByExercise(int id) {
        database = dataHelper.getWritableDatabase();
        String querys = "DELETE FROM tbl_custom_exercise_duration WHERE exercise_id=" + id;
        database.execSQL(querys);
    }

    public void updateWorkoutRestData(int rest, int exer_id, int workout_ids) {
        database = dataHelper.getWritableDatabase();
        String wheres = "exercise_id=" + exer_id + " AND workout_id=" + workout_ids;
        ContentValues contentValues = new ContentValues();
        contentValues.put("duration", rest);
        database.update("tbl_custom_exercise_duration", contentValues, wheres, null);
    }

    public void deleteRestDataById(int id) {
        database = dataHelper.getWritableDatabase();
        String querys = "DELETE FROM tbl_custom_exercise_duration WHERE id=" + id;
        database.execSQL(querys);
    }

    public List<ModelCustomExercise> getAllDailyWorkouts() {
        database = dataHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM tbl_custom_workout", null);
        List<ModelCustomExercise> dailyExerciseList = new ArrayList<>();
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    ModelCustomExercise modelDailyExercise = new ModelCustomExercise();
                    modelDailyExercise.id = cursor.getInt(cursor.getColumnIndex("id"));
                    modelDailyExercise.title = cursor.getString(cursor.getColumnIndex("title"));
                    modelDailyExercise.description = cursor.getString(cursor.getColumnIndex("description"));
                    modelDailyExercise.exercise = cursor.getString(cursor.getColumnIndex("exercise"));
                    modelDailyExercise.duration = cursor.getString(cursor.getColumnIndex("duration"));
                    dailyExerciseList.add(modelDailyExercise);

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return dailyExerciseList;
    }


    public ModelExerciseDetail getExerciseDetail(int i) {
        database = dataHelper.getWritableDatabase();
        ModelExerciseDetail modelExerciseDetail = new ModelExerciseDetail();
        Cursor cursor = database.rawQuery("SELECT * FROM tbl_exercise_detail WHERE id=" + i, null);
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    modelExerciseDetail.id = cursor.getInt(cursor.getColumnIndex("id"));
                    modelExerciseDetail.exercise_detail = cursor.getString(cursor.getColumnIndex("exercise_detail"));
                    modelExerciseDetail.exercise_img = cursor.getString(cursor.getColumnIndex("exercise_img"));
                    modelExerciseDetail.exercise_name = cursor.getString(cursor.getColumnIndex("exercise_name"));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return modelExerciseDetail;

    }

    public ModelMainCategory getMainCategoryDataById(int i) {
        database = dataHelper.getWritableDatabase();
        ModelMainCategory modelMainCategory = new ModelMainCategory();
        Cursor cursor = database.rawQuery("SELECT * FROM tbl_main_category WHERE id=" + i, null);
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    modelMainCategory.id = cursor.getInt(cursor.getColumnIndex("id"));
                    modelMainCategory.image = cursor.getString(cursor.getColumnIndex("image"));
                    modelMainCategory.title = cursor.getString(cursor.getColumnIndex("title"));
                    char c[] = cursor.getString(cursor.getColumnIndex("title")).toCharArray();
                    for (int j = 0; j < c.length; j++) {
                        if (c[j] ==  ' ') {
                            c[j + 1] = (char) (c[j + 1] - 32);
                        }
                    }
                    modelMainCategory.title = String.valueOf(c);
                } while (cursor.moveToNext());

            }
        }
        cursor.close();
        return modelMainCategory;

    }

    public List<ModelMainCategory> getAllMainCategory() {
        database = dataHelper.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = database.rawQuery("SELECT * FROM tbl_main_category", null);
        List<ModelMainCategory> historyList = new ArrayList<>();
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    ModelMainCategory history = new ModelMainCategory();
                    history.id = cursor.getInt(cursor.getColumnIndex("id"));
                    char c[] = cursor.getString(cursor.getColumnIndex("title")).toCharArray();
                    for (int i = 0; i < c.length; i++) {
                        if (c[i] ==  ' ') {
                            c[i + 1] = (char) (c[i + 1] - 32);
                        }
                    }
                    history.title = String.valueOf(c);
                    history.image = cursor.getString(cursor.getColumnIndex("image"));

                    historyList.add(history);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return historyList;
    }


    public int getTotalWorkoutCount(int cal) {
        database = dataHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM tbl_category_data WHERE cat_id=" + cal, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    public List<ModelCategoryData> getAllCategoryData(int ids) {
        database = dataHelper.getWritableDatabase();
        List<ModelCategoryData> categoryDataList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM tbl_category_data WHERE cat_id=" + ids, null);
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    ModelCategoryData modelCategoryData = new ModelCategoryData();
                    modelCategoryData.id = cursor.getInt(cursor.getColumnIndex("id"));
                    modelCategoryData.cat_id = cursor.getInt(cursor.getColumnIndex("cat_id"));
                    modelCategoryData.exercise_id = cursor.getInt(cursor.getColumnIndex("exercise_id"));
                    modelCategoryData.duration = cursor.getString(cursor.getColumnIndex("duration"));
                    categoryDataList.add(modelCategoryData);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return categoryDataList;
    }

    public List<ModelCategoryData> getAllDetailCategoryData(int ids) {
        database = dataHelper.getWritableDatabase();
        List<ModelCategoryData> categoryDataList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM tbl_category_data WHERE cat_id=" + ids, null);
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    ModelCategoryData modelCategoryData = new ModelCategoryData();
                    modelCategoryData.id = cursor.getInt(cursor.getColumnIndex("id"));
                    modelCategoryData.cat_id = cursor.getInt(cursor.getColumnIndex("cat_id"));
                    modelCategoryData.exercise_id = cursor.getInt(cursor.getColumnIndex("exercise_id"));
                    modelCategoryData.duration = cursor.getString(cursor.getColumnIndex("duration"));

                    categoryDataList.add(modelCategoryData);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return categoryDataList;
    }

    public int getAllCategoryDuration(int ids) {
        int time = 0;
        database = dataHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT duration FROM tbl_category_data WHERE cat_id=" + ids, null);
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    ModelCategoryData modelCategoryData = new ModelCategoryData();
                    modelCategoryData.duration = cursor.getString(cursor.getColumnIndex("duration"));
                    if (!Config.timeInSteps(modelCategoryData.duration)) {
                        time = time + Integer.parseInt(modelCategoryData.duration);
                    }
                } while (cursor.moveToNext());
            }
        }

        Log.e("time===", "--" + time);
        cursor.close();
        return time;
    }


}
