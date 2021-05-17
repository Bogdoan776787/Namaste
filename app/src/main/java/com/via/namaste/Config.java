package com.via.namaste;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.EditText;

import com.via.namaste.models.ModelTour;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class Config {
    static int TOTAL_DAYS = 28;
    public static int TOTAL_WEEK = 4;
    public static int TOTAL_WEEK_DAYS = 7;
    public static String ASSET_PATH = "file:///android_asset/exercise_img/";
    public static String ASSET_MAIN_CAT_PATH = "file:///android_asset/catimages/";
    static String SEND_DAY = "sendDays";
    static String SEND_POS = "sendPos";
    static String SEND_WEEK = "sendWeeks";
    static int DEFAULT_TIME = 2;
    static int TTS_TIME = 1500;
    static String SELECT_EDT_ID = "selected_id";
    public static String DURATION_REPS = "x";
    static String FROM_SETTINGS = "fromSettings";
    static String SEND_ID = "sendId";
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.US);
    static String DATE_FORMAT = "dd/MM/yyyy";
    public static String ASSET_GIF_PATH = ASSET_PATH;
    //    public static String ASSET_GIF_PATH = "file:///android_asset/images/";
    public static String SEND_CAT_ID = "sendCatId";
    public static String SEND_DURATION = "sendDuration";
    public static String SEND_EXERCISE = "sendExerciseCount";

    public static boolean SHOW_ADS = true;
    public static String REMOVE_ADS_12_MONTHS = "remove_ads_12_months";


    public static List<Integer> getDrawableList() {
        List<Integer> integerList = new ArrayList<>();
        integerList.add(R.drawable.card_1);
        integerList.add(R.drawable.card_2);
        integerList.add(R.drawable.card_4);
        integerList.add(R.drawable.card_5);
        integerList.add(R.drawable.card_3);
        integerList.add(R.drawable.card_1);
        integerList.add(R.drawable.card_2);
        integerList.add(R.drawable.card_4);
        integerList.add(R.drawable.card_5);
        integerList.add(R.drawable.card_3);
        integerList.add(R.drawable.card_1);
        integerList.add(R.drawable.card_2);

        return integerList;
    }

    static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean timeInSteps(String s) {
        return s.contains("x");
    }


    public static String convertToMinutes(int seconds) {

        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;

        int minutes = seconds / SECONDS_IN_A_MINUTE;
        seconds -= minutes * SECONDS_IN_A_MINUTE;

        int hours = minutes / MINUTES_IN_AN_HOUR;
        minutes -= hours * MINUTES_IN_AN_HOUR;

        return String.valueOf(minutes);
//        return hours + " hours " + minutes + " minutes " + seconds + " seconds";
    }


    public static String getHtmlFormattedTxts(String s) {
        return s.replace("\n", "<br/>").replace("\\n", "<br/>");
    }


    public static void sendToHomeExercise(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
       // intent.putExtra(SEND_ID, R.id.navHome);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    static List<String> getReminderDaysList() {
        List<String> stringList = new ArrayList<>();
        stringList.add("Mon");
        stringList.add("Tue");
        stringList.add("Wed");
        stringList.add("Thu");
        stringList.add("Fri");
        stringList.add("Sat");
        stringList.add("Sun");
        return stringList;
    }


    public static List<ModelTour> getAllTourData(Context context) {
        List<ModelTour> modelTours = new ArrayList<>();

        ModelTour tour = new ModelTour();
       // tour.tourBgimg = R.drawable.splash_1;
        tour.tourBtnColor = context.getResources().getColor(R.color.colorAccent);
        tour.tourDesc = context.getResources().getString(R.string.tour_des_1);
        tour.tourTitle = context.getResources().getString(R.string.tour_title_1);
        tour.tourimg = R.drawable.preview_1;

        modelTours.add(tour);


        tour = new ModelTour();
       // tour.tourBgimg = R.drawable.splash_2;
        tour.tourBtnColor = context.getResources().getColor(R.color.colorAccent);
        tour.tourDesc = context.getResources().getString(R.string.tour_des_3);
        tour.tourTitle = context.getResources().getString(R.string.tour_title_3);
        tour.tourimg = R.drawable.preview_2;
        modelTours.add(tour);

        tour = new ModelTour();
      //  tour.tourBgimg = R.drawable.splash_1;
        tour.tourBtnColor = context.getResources().getColor(R.color.colorAccent);
        tour.tourDesc = context.getResources().getString(R.string.tour_des_2);
        tour.tourTitle = context.getResources().getString(R.string.tour_title_2);
        tour.tourimg = R.drawable.preview_3;
        modelTours.add(tour);


        return modelTours;
    }

    static void shareApp(Activity context) {
        String url = "https://play.google.com/store/apps/details?id=" + context.getPackageName();
        String type = "text/plain";
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType(type);
        share.putExtra(Intent.EXTRA_TEXT, url);
        share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(share, context.getResources().getString(R.string.share_to)));
    }

    public static int getPercent(int progress, int total_exercise) {
        return (100 * progress / total_exercise);
    }

    static float meterToCm(float meter) {
        return meter * 100;
    }

    static float poundToKg(float pound) {
        return (float) (pound / 2.205);
    }

    static float feetAndInchToMeter(int feet, int inch) {

        float meter;
        float f1 = (float) (feet / 3.281);
        float i1 = (float) (inch / 39.37);
        meter = f1 + i1;
        return meter;
    }

    static float cmToMeter(float v) {
        return v / 100;
    }


    static void meterToInchAndFeet(float meter, EditText tv_feet, EditText tv_inch, boolean isTrue) {
        float in = (float) (meter * 39.37);
        float ft1 = in / 12;
        int n = (int) ft1;
        float in1 = ft1 - n;
        float in2;
        in2 = in1 * 12;
        if (isTrue) {
            tv_feet.setText(String.format(Locale.US, "%d", n));
            tv_inch.setText(String.format("%s", new DecimalFormat("##", new DecimalFormatSymbols(Locale.ENGLISH)).format(in2)));
        }
    }


    public static String convertSecondsToHMmSs(long seconds) {
        long totalSecs = seconds / 1000;
        long hours = (totalSecs / 3600);
        long mins = (totalSecs / 60) % 60;
        long secs = totalSecs % 60;
        String minsString = (mins == 0)
                ? "00"
                : ((mins < 10)
                ? "0" + mins
                : "" + mins);
        String secsString = (secs == 0)
                ? "00"
                : ((secs < 10)
                ? "0" + secs
                : "" + secs);
        if (hours > 0)
            return hours + ":" + minsString + ":" + secsString;
        else if (mins > 0)
            return mins + ":" + secsString;
        else return "00:" + secsString;

    }

    static float kgToPound(float kg) {
        return (float) (kg * 2.205);
    }

    static boolean checkTimeInSteps(String s) {
        return s.contains("x");
    }


}
