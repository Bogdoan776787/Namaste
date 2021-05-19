package com.via.namaste;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.via.namaste.adapters.AdapterStepProgress;
import com.via.namaste.databasehelper.DataManager;
import com.via.namaste.models.ModelCategoryData;
import com.via.namaste.models.ModelExerciseDetail;
import com.via.namaste.models.ModelMainCategory;

import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static android.speech.tts.TextToSpeech.QUEUE_FLUSH;
import static com.via.namaste.Config.SEND_DURATION;
import static com.via.namaste.Config.SEND_EXERCISE;
import static com.via.namaste.Config.TTS_TIME;

public class ActivityWorkoutDetail extends AppCompatActivity implements TextToSpeech.OnInitListener {
    String TAG = "ActivityWorkoutDetail";
    public static long activityTime = 0;
    public TextToSpeech tts;
    int countDownVal = 0, restVal = 0;
    CountDownTimer countDownTimer;
    boolean isCountDown = false, isPlay = true;
    CountDownTimer skipTimer;
    CountDownTimer progressTimer;
    int skip = 0, pause = 0;
    RelativeLayout layoutTvCount;
    LinearLayout layoutReadyImg, layoutReady, layoutBottomExercise, layoutSkip, layoutMain, layoutHorizontalProgress, layoutProgress, layoutDialog, layoutSkipDialog;
    TextView tvReadyTimer, tvReadyCountDown, tvReadyExercise, tvExercise, tvSkipTime, tvSkipName, tvHorizontalExerciseName, tvDialogDes, tvHorizontalProgress, tvDialogTitle, tvTakeRest;
    ImageView imgReady, imgExercise, btnDone, btnNext, imgSkip, imgPauseHorizontal, imgSkipHorizontal, imgNext, btnPlay, btnDetail, btnDialogClose, imgDialogMain;
    TextView btnReadySkip;
    TextView btn_skip_timer;
    DataManager manager;
    int pos = 0, mm = 0;
    int getId;
    int pause_length = 0;
    Animation animationSlideUp, animationSlideDown;
    ProgressBar progressBar;
    RoundedHorizontalProgressBar horizontalProgress;
    Handler handler;
    String startTime;
    boolean isSkipTimer = false, isHorizontalTimer = false;
    int resumeSkip = 0;
    MediaPlayer mediaPlayer;
    TextView tvType;
    Calendar calendar;
    Format format;
    String date;
    int countDownTime = 0;
    DataManager dataManager;
    boolean interstitialCanceled;
    List<ModelExerciseDetail> modelExerciseDetail;
    List<ModelCategoryData> modelExerciseList;
    AdapterStepProgress adapterStepProgress;
    RecyclerView recStepCount;

    LinearLayout dialogBottomLayout;

    TextView tvNext;
    MediaPlayer mediaPlayerProgress = null;
    ModelMainCategory modelCategoryData;
    TextView tvTitle;
    ImageView imgBack;


    public static String placeZeroIfNeed(int number) {
        return (number >= 10) ? Integer.toString(number) : String.format("0%s", Integer.toString(number));
    }

    private void stopPlayer() {
        try {
            if (mediaPlayerProgress != null) {
                mediaPlayerProgress.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startPlayer() {
        mediaPlayerProgress = MediaPlayer.create(getApplicationContext(), R.raw.td_tick);
                    mediaPlayerProgress.start();



    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);
        Log.d(TAG, "onCreate: ");
        getId = getIntent().getIntExtra(Config.SEND_CAT_ID, 1);
        pos = 0;
        dataManager = DataManager.getInstance(getApplicationContext());
        modelExerciseList = new ArrayList<>();
        mediaPlayer = new MediaPlayer();
        modelCategoryData = dataManager.getMainCategoryDataById(getId);
        manager = DataManager.getInstance(getApplicationContext());
        modelExerciseList = dataManager.getAllDetailCategoryData(getId);
        if (modelExerciseList.size() > 0) {
            calendar = Calendar.getInstance();
            format = new SimpleDateFormat("dd/MM/yyyy");
            date = format.format(calendar.getTime());
            modelExerciseDetail = new ArrayList<>();
            init();
            tvTitle.setText(modelCategoryData.title);

            clickListeners();
            tts = new TextToSpeech(this, this);


            startTime = new SimpleDateFormat("h:mm a").format(new Date());
            countDownVal = PrefrenceData.getCountDownTime(getApplicationContext());
            int time = 0;
            if (modelExerciseList != null && modelExerciseList.size() > 0) {
                for (int i = 0; i < modelExerciseList.size(); i++) {
                    modelExerciseDetail.add(dataManager.getExerciseDetail(modelExerciseList.get(i).exercise_id));
                }
            }
            if (pos >= modelExerciseDetail.size()) {
                pos = 0;
            }
            setImageData(imgReady);
            tvReadyExercise.setText(modelExerciseDetail.get(pos).exercise_name);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, TTS_TIME);
            setCountDownTimer(countDownVal);
            Log.d(TAG, "onCreate: ");
            setStepDatas(pos);
            setThemeColorData();

        }else {
            Toast.makeText(this, "Data not available, Please check documentation for verify your product", Toast.LENGTH_LONG).show();
        }
    }




    private void setThemeColorData() {

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(Color.WHITE);
        Drawable drawable1 = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_play_arrow_black_24dp);
        drawable1.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
//        drawable1.setColorFilter(Color.parseColor(modelMultiListData.color1), PorterDuff.Mode.SRC_IN);
        btnPlay.setBackground(drawable);
        btnPlay.setImageDrawable(drawable1);

        btnReadySkip.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            horizontalProgress.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)));
        }
    }

    private void setImageData(ImageView imgReadya) {
        try {
            if (!TextUtils.isEmpty(modelExerciseDetail.get(pos).exercise_img)) {
                Glide.with(getApplicationContext()).asGif().load(Config.ASSET_GIF_PATH + modelExerciseDetail.get(pos).exercise_img).into(imgReadya);
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mediaPlayer = new MediaPlayer();
    }

    private void init() {
        animationSlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationSlideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        layoutReady = findViewById(R.id.layout_ready);
        tvNext = findViewById(R.id.tvNext);
        tvTitle = findViewById(R.id.tvName);
        imgBack = findViewById(R.id.imgBack);
        //dialogBottomLayout = findViewById(R.id.dialogBottomLayout);
        layoutReadyImg = findViewById(R.id.layout_ready_img);
        layoutSkip = findViewById(R.id.layout_skip);
        layoutProgress = findViewById(R.id.layout_progress);
        layoutMain = findViewById(R.id.layout_main);
        layoutDialog = findViewById(R.id.layout_dialog);
        layoutSkipDialog = findViewById(R.id.layout_skip_dialog);
        layoutHorizontalProgress = findViewById(R.id.layout_horizontal_progress);
        tvReadyTimer = findViewById(R.id.tv_ready_timer);
        tvReadyCountDown = findViewById(R.id.tv_ready_count_down);
        layoutTvCount = findViewById(R.id.layout_tv_count);
        tvReadyExercise = findViewById(R.id.tv_ready_exercise);
        tvTakeRest = findViewById(R.id.tv_take_rest);
        tvSkipName = findViewById(R.id.tv_skip_name);
        tvDialogTitle = findViewById(R.id.tv_dialog_title);
        tvDialogDes = findViewById(R.id.tv_dialog_des);
        tvSkipTime = findViewById(R.id.tv_skip_time);
        tvHorizontalProgress = findViewById(R.id.tv_horizontal_progress);
        tvHorizontalExerciseName = findViewById(R.id.tv_horizontal_exercise_name);
        imgReady = findViewById(R.id.img_ready);
        imgDialogMain = findViewById(R.id.img_dialog_main);
        imgPauseHorizontal = findViewById(R.id.img_pause_horizontal);
        imgNext = findViewById(R.id.img_next);
        btnDetail = findViewById(R.id.btn_detail);
        btnDialogClose = findViewById(R.id.btn_dialog_close);
        imgExercise = findViewById(R.id.img_exercise);
        btnPlay = findViewById(R.id.btn_play);
        imgSkip = findViewById(R.id.img_skip);
        tvType = findViewById(R.id.tv_type);
        recStepCount = findViewById(R.id.recStepCount);
        btnReadySkip = findViewById(R.id.btn_ready_skip);
        btn_skip_timer = findViewById(R.id.skip_timer);
        progressBar = findViewById(R.id.progressBar);
        horizontalProgress = findViewById(R.id.horizontal_progress);
        layoutBottomExercise = findViewById(R.id.layout_bottom_exercise);
        tvExercise = findViewById(R.id.tv_exercise);
        tvReadyExercise = findViewById(R.id.tv_ready_exercise);
        btnDone = findViewById(R.id.btn_done);
        btnNext = findViewById(R.id.btn_next);

    }

    private void setStepDatas(int iPos) {
        adapterStepProgress = new AdapterStepProgress(modelExerciseDetail.size(), ActivityWorkoutDetail.this, iPos);
        recStepCount.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        recStepCount.setAdapter(adapterStepProgress);
        recStepCount.smoothScrollToPosition(pos);
        adapterStepProgress.notifyDataSetChanged();
    }

    public void playMediaPlayer(int id) {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer = new MediaPlayer();
            }


        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("SetTextI18n")
    public void showDetailDialog() {
        try {
            layoutDialog.setVisibility(View.VISIBLE);
            layoutDialog.startAnimation(animationSlideUp);
            tvDialogTitle.setText(modelExerciseDetail.get(pos).exercise_name);
            tvDialogDes.setText(Html.fromHtml(modelExerciseDetail.get(pos).exercise_detail));
            setImageData(imgDialogMain);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    private void resumeData() {
        try {
            mediaPlayer = new MediaPlayer();

            if (isSkipTimer) {
                restVal = PrefrenceData.getRestTime(getApplicationContext());
                restVal = restVal - skip;
                setSkipTimer(restVal);
            }

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        final int delay = 1000;
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                activityTime = activityTime + 1000;
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (modelExerciseList.size() > 0) {
            resumeData();
        }
        interstitialCanceled = false;
    }

    @SuppressLint("SetTextI18n")
    public void setSkip(boolean isFromPause) {
        layoutMain.setVisibility(View.GONE);
        layoutSkip.setVisibility(View.VISIBLE);
        if (isFromPause) {
            layoutProgress.setVisibility(View.GONE);
            btnPlay.setVisibility(View.VISIBLE);
            tvTakeRest.setVisibility(View.GONE);
            tvNext.setVisibility(View.INVISIBLE);
            tvSkipName.setText(modelExerciseDetail.get(pos).exercise_name);
            setImageData(imgSkip);
        } else {
            tvNext.setVisibility(View.VISIBLE);
            btnPlay.setVisibility(View.GONE);
            tvTakeRest.setVisibility(View.VISIBLE);
            layoutProgress.setVisibility(View.VISIBLE);
            restVal = PrefrenceData.getRestTime(getApplicationContext());
            Log.e("restTime==", "" + restVal);
            skip = 0;
            Log.d(TAG, "setSkip: " + restVal);
            progressBar.setMax(restVal);
            setSkipTimer(restVal);
            tvSkipName.setText("" + (pos + 1) + "/" + modelExerciseList.size() + " " + modelExerciseDetail.get(pos).exercise_name);
            setImageData(imgSkip);
        }
    }

    public void setNextData(boolean ab) {
        try {
            if (pos < modelExerciseList.size() - 1) {
                pos = pos + 1;
                setSkip(ab);
                setStepDatas(pos);
            } else {
                setStepDatas(pos + 1);

                Intent intent = new Intent(getApplicationContext(), ActivityResult.class);
                intent.putExtra(SEND_DURATION, activityTime);
                intent.putExtra(SEND_EXERCISE, modelExerciseList.size());
                startActivity(intent);
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }


    public void cancelTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (skipTimer != null) {
            skipTimer.cancel();
        }
        if (isHorizontalTimer) {
            progressTimer.cancel();
            setSkip(true);
        }
        mediaPlayer.release();
        handler.removeCallbacksAndMessages(null);

    }

    @Override
    public void onBackPressed() {

        if (modelExerciseList.size() > 0){
            if (layoutDialog.getVisibility() == View.VISIBLE) {
                btnDialogClose.performClick();
            } else {
                showExitDialog();
            }
        }else {
            onBackPressed();
        }

    }

    public void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityWorkoutDetail.this);
        builder.setTitle(getResources().getString(R.string.exit_exercise_msg));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopTTS();
                cancelTimer();
                Intent intent = new Intent(getApplicationContext(), ActivityWorkoutList.class);
                intent.putExtra(Config.SEND_CAT_ID, getId);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    @Override
    protected void onStop() {
        cancelTimer();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        cancelTimer();
        stopPlayer();
        stopTTS();
        super.onDestroy();
    }

    private void stopTTS() {
        try {
            if (tts != null) {
                tts.stop();
                tts.shutdown();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void clickListeners() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnReadySkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideReadyView();
                setExerciseData(pos);
            }
        });


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("getposcomplete===", "---" + pos);

                try {
                    playMediaPlayer(1);

                    setNextData(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btnDialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modelExerciseList.size() > 0) {
                    resumeData();
                    layoutDialog.startAnimation(animationSlideDown);
                    layoutDialog.setVisibility(View.GONE);
                }

            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNextData(false);

            }
        });

        btn_skip_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlay = true;
                imgPauseHorizontal.setImageDrawable(getDrawable(R.drawable.ic_pause));

                skipTimer.cancel();
                playMediaPlayer(2);
                setExerciseData(pos);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutSkip.setVisibility(View.GONE);
                layoutMain.setVisibility(View.VISIBLE);
                Log.e("pauseLength===", "" + pause_length);
                setProgressTimer(pause_length);
            }
        });

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDetailDialog();
            }
        });


        tvSkipName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layoutDialog.getVisibility() != View.VISIBLE) {
                    showDetailDialog();
                }
            }
        });

        imgPauseHorizontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (progressTimer != null) {
                    progressTimer.cancel();
                }
                //setSkip(true);

                if (isPlay) {
                    Log.d(TAG, "isPlay: " + isPlay + " - " + pause_length);
                    imgPauseHorizontal.setImageDrawable(getDrawable(R.drawable.ic_play_2));
                    isPlay = false;
                } else {
                    Log.d(TAG, "isPlay: " + isPlay + " - " + pause_length);
                    imgPauseHorizontal.setImageDrawable(getDrawable(R.drawable.ic_pause));
                    setProgressTimer(pause_length);
                    isPlay = true;
                }
            }
        });


        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalProgress.setProgress(mm);
                setNextData(false);
                if (progressTimer != null) {
                    progressTimer.cancel();
                }
            }
        });
    }

    public void setProgressTimer(final int time) {
        progressTimer = new CountDownTimer(time * 1000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long l) {
                String tick = onTicks(((mm * 1000) - l) + 1000);
                tvHorizontalProgress.setText("    " + tick + "/" + onTicks(mm * 1000));
                pause = pause + 1;
                pause_length = (int) (l / 1000);


                horizontalProgress.setProgress(horizontalProgress.getProgress() + 1);
                if (pause_length <= 3) {
                    stopPlayer();
                    if (pause_length > 0) {
                    }
                } else {
                    startPlayer();
                }
                isHorizontalTimer = true;
                isSkipTimer = false;

            }

            @Override
            public void onFinish() {
                stopPlayer();
                horizontalProgress.setProgress(mm);
                setNextData(false);
                progressTimer.cancel();
                isHorizontalTimer = false;

            }
        }.start();
    }

    public String onTicks(long millisUntilFinished) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date(millisUntilFinished);
        return dateFormat.format(date);
    }

    public void setCountDownTimer(int time) {
        countDownTimer = new CountDownTimer(time * 1000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long l) {
                countDownTime = 1 / 1000;
                isCountDown = true;
                int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(l);
                int seconds = (int) (TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(minutes));
                String mm = placeZeroIfNeed(minutes);
                String ss = placeZeroIfNeed(seconds);

                tvReadyTimer.setText(mm + ":" + ss);
                int i = (int) (l / 1000);
                if (i <= 3) {
                    String s = String.valueOf(l / 1000);
                    if (i > 0) {
                        tvReadyCountDown.setText(s);
                    }
                    layoutTvCount.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFinish() {
                countDownTime = 0;
                isCountDown = false;
                activityTime = 0;

                hideReadyView();
                setExerciseData(pos);
            }
        }.start();
    }

    public void setSkipTimer(int skipTime) {
        try {
            if (skipTimer != null) {
                skipTimer.cancel();

            }
            skipTimer = new CountDownTimer(skipTime * 1000, 1000) {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTick(long l) {
                    skip = skip + 1;
                    int i = (int) (l / 1000);
                    tvSkipTime.setText("" + i);
                    progressBar.setProgress(skip);
                    isSkipTimer = true;
                    isHorizontalTimer = false;
                    resumeSkip = (int) (l / 1000);


                }

                @Override
                public void onFinish() {
                    isSkipTimer = false;
                    playMediaPlayer(2);
                    setExerciseData(pos);
                }
            }.start();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    public void pauseData() {
        try {
            if (skipTimer != null) {
                skipTimer.cancel();
            }
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            if (isHorizontalTimer) {
                imgPauseHorizontal.performClick();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        pauseData();
        stopPlayer();
        interstitialCanceled = true;
        super.onPause();

    }

    public void showCompleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityWorkoutDetail.this);
        builder.setTitle(getResources().getString(R.string.complete_workout));
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.finish_workout));
        builder.setPositiveButton(getResources().getString(R.string.repeat), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), ActivityWorkoutDetail.class);
                intent.putExtra(Config.SEND_CAT_ID, getId);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.finish), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.show();


    }


    public void hideReadyView() {
        countDownTimer.cancel();
        layoutReady.setVisibility(View.GONE);
        layoutReadyImg.setVisibility(View.GONE);
        tvReadyTimer.setVisibility(View.GONE);
        imgExercise.setVisibility(View.VISIBLE);
    }


    public void showNotification(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            importance = NotificationManager.IMPORTANCE_HIGH;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.congrats))
                .setContentText(getResources().getString(R.string.done_workout))
                .setAutoCancel(true);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        assert notificationManager != null;
        notificationManager.notify(notificationId, mBuilder.build());
    }


    @SuppressLint("SetTextI18n")
    public void setExerciseData(int pos) {
        Log.d(TAG, "setExerciseData: " + pos);
        layoutSkip.setVisibility(View.GONE);
        layoutMain.setVisibility(View.VISIBLE);

        if (Config.timeInSteps(modelExerciseList.get(pos).duration)) {
            String s = modelExerciseList.get(pos).duration.replace("x", "");
            layoutHorizontalProgress.setVisibility(View.GONE);
            layoutBottomExercise.setVisibility(View.VISIBLE);
            setImageData(imgExercise);
            tvExercise.setText("" + modelExerciseDetail.get(pos).exercise_name);
            tvType.setText(getResources().getString(R.string.perform) + " " + modelExerciseList.get(pos).duration);
            Log.d(TAG, "setExerciseData: " + getResources().getString(R.string.perform) + " - " + modelExerciseList.get(pos).duration);
        } else {
            setImageData(imgExercise);
            layoutBottomExercise.setVisibility(View.GONE);
            layoutHorizontalProgress.setVisibility(View.VISIBLE);
            String time = modelExerciseList.get(pos).duration;

            tvHorizontalExerciseName.setText(modelExerciseDetail.get(pos).exercise_name);
            mm = Integer.parseInt(time);

            horizontalProgress.setMax(mm);
            horizontalProgress.setProgress(0);
            setProgressTimer(mm);
        }
    }



    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int ttsLang = tts.setLanguage(Locale.US);
            if (ttsLang == TextToSpeech.LANG_MISSING_DATA || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {

            } else {
            }
        } else {
        }
    }
}
