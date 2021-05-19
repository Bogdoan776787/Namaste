package com.via.namaste;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.via.namaste.adapters.AdapterSelecteWorkoutList;
import com.via.namaste.databasehelper.DataManager;
import com.via.namaste.models.ModelCategoryData;
import com.via.namaste.models.ModelExerciseDetail;
import com.via.namaste.models.ModelMainCategory;

import java.util.ArrayList;
import java.util.List;

import static com.via.namaste.Config.ASSET_GIF_PATH;
import static com.via.namaste.Config.SEND_CAT_ID;


public class ActivityWorkoutList extends AppCompatActivity implements AdapterSelecteWorkoutList.clickInterface {

    public static final String TAG = "ActivityWorkoutList";
    RecyclerView recWorkoutsList;
    int getId;
    List<ModelCategoryData> dailyExerciseList;
    DataManager manager;
    AdapterSelecteWorkoutList adapterSelecteWorkoutList;
    ImageView btnClose, imgExercise, btnPrev, btnNext;
    TextView tvTitle, tvDes, tvCurrent, tvTotal;
    LinearLayout dialogBottomBg;
    int pos = 0;

    ModelMainCategory modelMainCategory;
    ImageView imgBg;
    TextView tvTotalExercise, tvTotalTimes;
    FloatingActionButton fabStart;


    TextView tvName;
    ImageView imgBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);
        getId = getIntent().getIntExtra(SEND_CAT_ID, 1);
        dailyExerciseList = new ArrayList<>();
        manager = DataManager.getInstance(getApplicationContext());

        dailyExerciseList = manager.getAllCategoryData(getId);

        modelMainCategory = manager.getMainCategoryDataById(getId);

        init();



        if (!TextUtils.isEmpty(modelMainCategory.image)) {
            int drawableResourceId = getResources().getIdentifier(modelMainCategory.image.trim(), "drawable", getPackageName());
            imgBg.setImageResource(drawableResourceId);
        }

        tvName.setText(modelMainCategory.title);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (dailyExerciseList != null && dailyExerciseList.size() > 0) {
            int duration = manager.getAllCategoryDuration(getId);
            tvTotalExercise.setText(dailyExerciseList.size() + " " + getResources().getString(R.string.exercises));
            tvTotalTimes.setText(Config.convertToMinutes(duration) + " " + getResources().getString(R.string.minutes));
            adapterSelecteWorkoutList = new AdapterSelecteWorkoutList(dailyExerciseList, this);
            recWorkoutsList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
            recWorkoutsList.setAdapter(adapterSelecteWorkoutList);
            adapterSelecteWorkoutList.setListeners(this);
        }
    }

    private void sendDatas() {
        Intent intent = new Intent(getApplicationContext(), ActivityWorkoutDetail.class);
        intent.putExtra(SEND_CAT_ID, getId);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
        int countShowAds = HomeFragment.countShowAds;

    }

    @Override
    protected void onResume() {
        super.onResume();
    }




    private void init() {
        tvName = findViewById(R.id.tvName);
        imgBack = findViewById(R.id.imgBack);
        tvTotalExercise = findViewById(R.id.tvTotalExercise);
        tvTotalTimes = findViewById(R.id.tvTotalTimes);
        imgBg = findViewById(R.id.imgBg);
        recWorkoutsList = findViewById(R.id.rec_workouts_list);
        fabStart = findViewById(R.id.fabStart);
        fabStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDatas();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Config.sendToHomeExercise(getApplicationContext());
    }


    public void showExerciseDialog(int position) {
        pos = position;
        final Dialog dialog = new Dialog(ActivityWorkoutList.this, R.style.AlertDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exercise);
        dialog.setCanceledOnTouchOutside(true);
        btnClose = dialog.findViewById(R.id.btn_close);
        imgExercise = dialog.findViewById(R.id.img_exercise);
        btnPrev = dialog.findViewById(R.id.btn_prev);
        btnNext = dialog.findViewById(R.id.btn_next);
        tvTitle = dialog.findViewById(R.id.tv_title);
        tvDes = dialog.findViewById(R.id.tv_des);
        tvCurrent = dialog.findViewById(R.id.tv_current);
        dialogBottomBg = dialog.findViewById(R.id.dialogBottomBg);
        tvTotal = dialog.findViewById(R.id.tv_total);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        setDialogData(pos);
        tvDes.setMovementMethod(new ScrollingMovementMethod());
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pos < dailyExerciseList.size() - 1) {
                    pos = pos + 1;
                    setDialogData(pos);
                }
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pos > 0) {
                    pos = pos - 1;
                    setDialogData(pos);
                }
            }
        });
        dialog.show();
    }

    @SuppressLint("DefaultLocale")
    public void setDialogData(int pos) {
        ModelExerciseDetail modelExerciseDetail = manager.getExerciseDetail(dailyExerciseList.get(pos).exercise_id);
        tvTitle.setText(modelExerciseDetail.exercise_name);
        if (!TextUtils.isEmpty(modelExerciseDetail.exercise_detail)) {
            tvDes.setText(Html.fromHtml(modelExerciseDetail.exercise_detail.replace("\n", "<br/>").replace("\\n", "<br/>")));
        }


        if (!TextUtils.isEmpty(modelExerciseDetail.exercise_img)) {
            Glide.with(getApplicationContext()).asGif().load(ASSET_GIF_PATH + modelExerciseDetail.exercise_img).into(imgExercise);
        }
        tvCurrent.setText(String.format("%d", pos + 1));
        tvTotal.setText(String.format("/%d", dailyExerciseList.size()));
    }


    @Override
    public void onDialogShow(View view, int ids) {
        showExerciseDialog(ids);
    }
}
