package com.via.namaste;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.via.namaste.R.drawable.ic_action_name;

public class MainActivity extends AppCompatActivity {

    //Initialize variable
    MeowBottomNavigation bottomNavigation;



    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load the home fragment when the app starts
//        getSupportFragmentManager().beginTransaction().replace(R.layout.fragment_home, new HomeFragment()).commit();

        bottomNavigation = findViewById(R.id.bottom_navigation);

        //Add menu item
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_baseline_home_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_baseline_add_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_action_name));
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener()



        {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                //Initialize fragment
                Fragment fragment = null;
                //Check condition

                switch (item.getId()) {
                    case 1:
                        //When id is 1
                        // Initialize home fragment
                        fragment = new HomeFragment();
                        break;

                    case 2:
                        //When id is 2
                        // Initialize add fragment
                        fragment = new AddFragment();
                        break;
                    case 3:
                        //When id is 3
                        // Initialize notifications fragment
                        fragment = new ProgressFragment();
                        break;
                    default:
                        break;
                }


                //Load fragment
                loadFragment(fragment);

            }
        });


        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

                //Display Toast



            }
        });
    }

    private void loadFragment(Fragment fragment) {
        //Replace fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();

    }
}