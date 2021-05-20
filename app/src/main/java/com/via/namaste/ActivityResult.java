package com.via.namaste;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.via.namaste.Views.CustomSeekBar;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.via.namaste.Config.SEND_DURATION;
import static com.via.namaste.Config.SEND_EXERCISE;
import static com.via.namaste.Config.SEND_POS;

public class ActivityResult extends AppCompatActivity {
    public String[] stringList = {"18.5", "25", "30", "40"};
    public float totalSpan = 40;
    public float first = 18f;
    public float second = 7f;
    public float third = 5f;
    public float fourth = 10f;
    Button btnHome, btnShare, btnCalBmi;
    Toolbar toolbar;
    TextView tvExercise, tvDuration, btnKg, btnPound, btnCm, btnIn;
    EditText  edtKg;
    long GetTime;
    int getExercise;

    TextView  testing;
    boolean interstitialCanceled;
    int getPos = 0;
    LinearLayout layoutMainBg;

    public static String PlaceZeroIFNeed(int number) {
        return (number >= 10) ? Integer.toString(number) : String.format("0%s", number);
    }

    public void setBackground(Context context, TextView tv1, TextView tv2) {
        tv1.setBackgroundResource(R.drawable.gradient2);
        tv1.setTextColor(context.getResources().getColor(R.color.white));
        tv2.setBackground(context.getResources().getDrawable(R.drawable.rect_layout_border));
        tv2.setTextColor(context.getResources().getColor(R.color.txt_light_color));
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        GetTime = getIntent().getLongExtra(SEND_DURATION, 0);
        getExercise = getIntent().getIntExtra(SEND_EXERCISE, 8);
        getPos = getIntent().getIntExtra(SEND_POS, 0);
        init();


        tvExercise.setText(String.valueOf(getExercise));
        tvDuration.setText(String.valueOf(LongToTime(GetTime)));

    }







    public String LongToTime(long duration) {
        int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(duration);
        int seconds = (int) (TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes));
        String mm = PlaceZeroIFNeed(minutes);
        String ss = PlaceZeroIFNeed(seconds);
        return mm + ":" + ss;
    }

    @SuppressLint("SetTextI18n")
    private void init() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.congrats));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        layoutMainBg = findViewById(R.id.layoutMainBg);
        tvExercise = findViewById(R.id.tv_exercise);
        tvDuration = findViewById(R.id.tv_duration);
        testing = findViewById(R.id.testing);

    }

    @Override
    public void onBackPressed() {
        sendToHome();
    }

    private void sendToHome() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }


    @Override
    public void onPause() {
        interstitialCanceled = true;
        super.onPause();
    }
}
