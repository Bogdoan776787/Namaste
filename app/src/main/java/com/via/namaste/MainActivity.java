package com.via.namaste;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;
import static com.via.namaste.Config.SEND_ID;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.via.namaste.Views.FabBottomNavigationView;
import com.via.namaste.repository.MainActivity2;

import org.jetbrains.annotations.NotNull;

import static com.via.namaste.R.drawable.ic_action_name;

public class MainActivity extends AppCompatActivity {
    String TAG = "my_MainActivity";
    FrameLayout frameContainer;
    FloatingActionButton fabCustomWorkout;
    FabBottomNavigationView bottomNav;
    int getIds;
    ViewPager pagerContainer;

       @Override
       protected void onCreate(@Nullable Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_main);

           getIds = getIntent().getIntExtra(SEND_ID, R.id.navHome);
           init();
           fabCustomWorkout.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(getApplicationContext(), ActivityCustomWorkout.class);
                   startActivity(intent);
               }
           });
           setNotificationsReminder();
           MyPagerAdapters myPagerAdapters = new MyPagerAdapters(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
           pagerContainer.setAdapter(myPagerAdapters);
           pagerContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
               @Override
               public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

               }

               @Override
               public void onPageSelected(int position) {
                   setNavigationItem(position);
               }

               @Override
               public void onPageScrollStateChanged(int state) {

               }
           });
           bottomNav.setSelectedItemId(getIds);

       }

    private void setNavigationItem(int position) {
        switch (position) {
            case 0:
                bottomNav.getMenu().getItem(0).setChecked(true);
                break;
            case 1:
                bottomNav.getMenu().getItem(1).setChecked(true);
                break;
            case 2:
                bottomNav.getMenu().getItem(3).setChecked(true);
                break;
            case 3:
                bottomNav.getMenu().getItem(4).setChecked(true);
                break;
        }
    }

    public static class MyPagerAdapters extends FragmentPagerAdapter {

        public MyPagerAdapters(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NotNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return new AllExerciseListFragment();
                case 2:
                    return new ReportFragment();
                case 3:
                    return new SettingFragment();
                default:
                    return new HomeFragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    public void setNotificationsReminder() {
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 100, pendingIntent);

        ComponentName componentName = new ComponentName(getApplicationContext(), AlarmReceiver.class);
        PackageManager packageManager = getPackageManager();
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }


    private void init() {
        pagerContainer = findViewById(R.id.pagerContainer);
        fabCustomWorkout = findViewById(R.id.fabCustomWorkout);
        frameContainer = findViewById(R.id.frameContainer);
        bottomNav = findViewById(R.id.bottomNav);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void clickHome(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navHome:
                pagerContainer.setCurrentItem(0);
                bottomNav.getMenu().getItem(0).setChecked(true);
                break;
            case R.id.navExercise:
                pagerContainer.setCurrentItem(1);
                bottomNav.getMenu().getItem(1).setChecked(true);
                break;
            case R.id.navReport:
                pagerContainer.setCurrentItem(2);
                bottomNav.getMenu().getItem(3).setChecked(true);
                break;
            case R.id.navSetting:
                pagerContainer.setCurrentItem(3);
                bottomNav.getMenu().getItem(4).setChecked(true);
                break;
        }
    }
}
}