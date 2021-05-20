package com.via.namaste.adapters;

import android.app.Activity;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.via.namaste.Config;
import com.via.namaste.R;
import com.via.namaste.databasehelper.DataManager;
import com.via.namaste.models.ModelCategoryData;
import com.via.namaste.models.ModelExerciseDetail;

import java.util.List;

import static com.via.namaste.Config.getHtmlFormattedTxts;


public class AdapterSelecteWorkoutList extends RecyclerView.Adapter<AdapterSelecteWorkoutList.MyViewHolder> {
    private List<ModelCategoryData> exerciseList;
    private DataManager manager;
    private Activity context;
    private clickInterface anInterface;

    public AdapterSelecteWorkoutList(List<ModelCategoryData> exerciseList, Activity contexts) {
        this.exerciseList = exerciseList;
        context = contexts;
        manager = DataManager.getInstance(context);
    }

    @NonNull
    @Override
    public AdapterSelecteWorkoutList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_selected_rec_list, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSelecteWorkoutList.MyViewHolder myViewHolder, final int i) {
        ModelExerciseDetail modelExercise = manager.getExerciseDetail(exerciseList.get(i).exercise_id);
        if (!TextUtils.isEmpty(modelExercise.exercise_img)) {
            Glide.with(context).load(Uri.parse(Config.ASSET_PATH + modelExercise.exercise_img)).into(myViewHolder.img_exercise);
        }
        myViewHolder.tv_exercise.setText(modelExercise.exercise_name);
        myViewHolder.tv_des.setText(Html.fromHtml(getHtmlFormattedTxts(modelExercise.exercise_detail)));

        if (Config.timeInSteps(exerciseList.get(i).duration)) {
            myViewHolder.tv_rest_time.setText(exerciseList.get(i).duration);
        } else {
            myViewHolder.tv_rest_time.setText(String.format("%s %s", exerciseList.get(i).duration, context.getResources().getString(R.string.txt_second)));
        }

    }

    public void setListeners(clickInterface listeners) {
        anInterface = listeners;

    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }


    public interface clickInterface {

        void onDialogShow(View view, int ids);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RoundedImageView img_exercise;
        TextView tv_exercise, tv_rest_time, tv_des;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_exercise = itemView.findViewById(R.id.img_exercise);
            tv_exercise = itemView.findViewById(R.id.tv_exercise);
            tv_rest_time = itemView.findViewById(R.id.tv_rest_time);
            tv_des = itemView.findViewById(R.id.tv_des);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (anInterface != null) {
                anInterface.onDialogShow(v, getAdapterPosition());
            }
        }
    }
}
