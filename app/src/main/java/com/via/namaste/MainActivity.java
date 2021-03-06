package com.via.namaste;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.via.namaste.Views.FabBottomNavigationView;

import org.jetbrains.annotations.NotNull;

import static com.via.namaste.Config.SEND_ID;

public class MainActivity extends AppCompatActivity {

    String TAG = "my_MainActivity";
    FrameLayout frameContainer;
    FabBottomNavigationView bottomNav;
    int getIds;
    ViewPager pagerContainer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getIds = getIntent().getIntExtra(SEND_ID, R.id.navHome);
        init();

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




    private void init() {
        pagerContainer = findViewById(R.id.pagerContainer);
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

            case R.id.navSetting:
                pagerContainer.setCurrentItem(1);
                bottomNav.getMenu().getItem(1).setChecked(true);
                break;
        }
    }
}
