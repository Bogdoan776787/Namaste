package com.via.namaste.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.via.namaste.R;


public class AdapterStepProgress extends RecyclerView.Adapter<AdapterStepProgress.MyViewHolder> {
    private int maxPos;
    private int currentPos;
    private Activity activity;


    public AdapterStepProgress(int maxPos, Activity activity, int current) {
        this.maxPos = maxPos;
        this.activity = activity;
        currentPos = current;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.view_step_progress_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (position == 0) {
            holder.viewStep.setVisibility(View.GONE);
        } else {
            holder.viewStep.setVisibility(View.VISIBLE);
        }
        int selectedColor = ContextCompat.getColor(activity, R.color.colorPrimary);
        int selectedColor2 = ContextCompat.getColor(activity, R.color.colorPrimary);

        int unselectedcolor = Color.parseColor("#E9E5E5");

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        if (position < currentPos || currentPos == maxPos) {
            holder.tvStep.setText(Html.fromHtml(activity.getResources().getString(R.string.check_icon)));
            holder.tvStep.setTextColor(Color.WHITE);
            drawable.setColors(new int[]{selectedColor, selectedColor2});
            holder.viewStep.setBackgroundColor(selectedColor);
        } else if (position == currentPos) {
            holder.tvStep.setText(String.valueOf(position + 1));
            holder.tvStep.setTextColor(Color.WHITE);
            drawable.setColors(new int[]{selectedColor, selectedColor2});
            holder.viewStep.setBackgroundColor(selectedColor);
        } else {
            holder.tvStep.setTextColor(selectedColor);
            holder.tvStep.setText(String.valueOf(position + 1));
            drawable.setColor(unselectedcolor);
            holder.viewStep.setBackgroundColor(unselectedcolor);
        }
        holder.relStepBg.setBackground(drawable);


    }

    @Override
    public int getItemCount() {
        return maxPos;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relStepBg;
        TextView tvStep;
        View viewStep;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            viewStep = itemView.findViewById(R.id.viewStep);
            relStepBg = itemView.findViewById(R.id.relStepBg);
            tvStep = itemView.findViewById(R.id.tvStep);

        }
    }
}
