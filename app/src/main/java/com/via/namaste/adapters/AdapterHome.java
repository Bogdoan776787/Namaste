package com.via.namaste.adapters;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.via.namaste.Config;
import com.via.namaste.R;
import com.via.namaste.databasehelper.DataManager;
import com.via.namaste.models.ModelMainCategory;

import java.util.List;

import static com.via.namaste.Config.convertToMinutes;


public class AdapterHome extends RecyclerView.Adapter<AdapterHome.MyViewHolder> {

    DataManager dataManager;
    private List<ModelMainCategory> modelMultiListData;
    private Activity context;
    private clickInterfaces interfaces;

    public AdapterHome(List<ModelMainCategory> modelMultiListData, Activity context) {
        this.modelMultiListData = modelMultiListData;
        this.context = context;
        dataManager = DataManager.getInstance(context);

    }

    public void setListeners(clickInterfaces listeners) {
        interfaces = listeners;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.relWorkout.setBackgroundResource(Config.getDrawableList().get(0));
        int i = dataManager.getTotalWorkoutCount(modelMultiListData.get(position).id);
        int i1 = dataManager.getAllCategoryDuration(modelMultiListData.get(position).id);

        holder.tvTotalDuration.setText(convertToMinutes(i1) + " " + context.getResources().getString(R.string.minutes));
        holder.tvWorkout.setText(modelMultiListData.get(position).title);
        holder.tvTotalWorkout.setText(i + " " + context.getResources().getString(R.string.workouts));
        int drawableResourceId = 0;
        if (!TextUtils.isEmpty(modelMultiListData.get(position).image)) {
            drawableResourceId = context.getResources().getIdentifier(modelMultiListData.get(position).image.trim(), "drawable", context.getPackageName());
        }

        if (position % 2 != 0) {
            holder.imgLeft.setVisibility(View.GONE);
            holder.imgRight.setVisibility(View.VISIBLE);
            holder.imgRight.setImageResource(drawableResourceId);
        } else {

            holder.imgLeft.setVisibility(View.VISIBLE);
            holder.imgRight.setVisibility(View.GONE);
            holder.imgLeft.setImageResource(drawableResourceId);
        }
    }

    @Override
    public int getItemCount() {
        return modelMultiListData.size();
    }

    public interface clickInterfaces {
        void onRecItemClick(int i, ModelMainCategory modelMultiListData);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout relWorkout;
        ImageView imgLeft, imgRight;
        TextView tvTotalWorkout;
        TextView tvWorkout;
        TextView tvTotalDuration;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTotalDuration = itemView.findViewById(R.id.tvTotalDuration);
            imgRight = itemView.findViewById(R.id.imgRight);
            imgLeft = itemView.findViewById(R.id.imgLeft);
            tvWorkout = itemView.findViewById(R.id.tvWorkout);
            tvTotalWorkout = itemView.findViewById(R.id.tvTotalWorkout);
            relWorkout = itemView.findViewById(R.id.relWorkout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (interfaces != null) {
                interfaces.onRecItemClick(getAdapterPosition(), modelMultiListData.get(getAdapterPosition()));
            }
        }
    }
}
