package com.via.namaste;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


import com.congle7997.google_iap.BillingSubs;
import com.congle7997.google_iap.CallBackBilling;
import com.firebase.ui.auth.AuthUI;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;




public class SettingFragment extends Fragment {
    static String TAG = "my_SettingFragment";

    private LinearLayout layoutRest, layoutCountDown, layoutFeedback,layoutSignOut;
    private TextView tvRestTime, tvCountDownTime;
    private int pos = 10;
    View view;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        init();
        clickListeners();
        tvRestTime.setText(String.format(Locale.US, "%d secs", PrefrenceData.getRestTime(Objects.requireNonNull(getContext()))));
        tvCountDownTime.setText(String.format(Locale.US, "%d secs", PrefrenceData.getCountDownTime(getContext())));


        return view;


    }

    @SuppressLint("DefaultLocale")
    private void init() {


        layoutRest = view.findViewById(R.id.layout_rest);
        layoutCountDown = view.findViewById(R.id.layout_count_down);
        layoutFeedback = view.findViewById(R.id.layout_feedback);
        tvRestTime = view.findViewById(R.id.tv_rest_time);
        tvCountDownTime = view.findViewById(R.id.tv_count_down_time);
        layoutSignOut=view.findViewById(R.id.layout_signOut);

    }




    private void clickListeners() {


        layoutRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRestDialog(true, 180, 10);
            }
        });

        layoutCountDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRestDialog(false, 15, 10);
            }
        });




        layoutFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback(Objects.requireNonNull(getActivity()));
            }
        });

            layoutSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AuthUI.getInstance()
                            .signOut(view.getContext());
                    goToSignIn();
                }
            });

    }



    @SuppressLint("SetTextI18n")
    private void showRestDialog(final boolean isRest, final int max, final int min) {
        ImageView btn_prev, btn_next;
        final TextView tv_time;
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.AlertDialogCustom);
        builder.setTitle(getResources().getString(R.string.set_duration) + " (" + min + " ~ " + max + getResources().getString(R.string.sec) + " )");
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_time, null);
        builder.setView(view);
        btn_next = view.findViewById(R.id.btn_next);
        btn_prev = view.findViewById(R.id.btn_prev);
        tv_time = view.findViewById(R.id.tv_time);
        if (isRest) {
            pos = PrefrenceData.getRestTime(Objects.requireNonNull(getContext()));
        } else {
            pos = PrefrenceData.getCountDownTime(Objects.requireNonNull(getContext()));
        }
        tv_time.setText("" + pos);
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos > min) {
                    pos = pos - 1;
                    tv_time.setText("" + pos);
                }
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos < max) {
                    pos = pos + 1;
                    tv_time.setText("" + pos);
                }
            }
        });
        builder.setCancelable(true);
        builder.setPositiveButton(getResources().getString(R.string.set), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int set_val = Integer.parseInt(tv_time.getText().toString());
                if (isRest) {
                    PrefrenceData.setRestTime(Objects.requireNonNull(getContext()), set_val);
                    tvRestTime.setText("" + set_val + " " + getResources().getString(R.string.sec));
                } else {
                    tvCountDownTime.setText("" + set_val + " " + getResources().getString(R.string.sec));
                    PrefrenceData.setCountDownTime(Objects.requireNonNull(getContext()), set_val);
                }
            }
        }).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static void sendFeedback(Activity activity) {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + activity.getResources().getString(R.string.mail_feedback_email))); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, activity.getResources().getString(R.string.mail_feedback_email));
            intent.putExtra(Intent.EXTRA_TEXT, "\n\n----------------------------------\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + BuildConfig.VERSION_NAME + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER + "\n" + "Feedback : " + activity.getResources().getString(R.string.feedback_msg));
            activity.startActivity(intent);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            Toast.makeText(activity, activity.getResources().getString(R.string.no_email_available), Toast.LENGTH_SHORT).show();

        }
    }

    private void goToSignIn()
    {
        startActivity(new Intent(view.getContext(), SignInActivity.class));

    }
}
