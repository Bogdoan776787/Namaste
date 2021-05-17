package com.via.namaste;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.via.namaste.repository.MainActivity2;

import static com.via.namaste.R.drawable.ic_action_name;

public class MainActivity extends AppCompatActivity {
    //implements NavigationView.OnNavigationItemSelectedListener
    //Initialize variable
    MeowBottomNavigation bottomNavigation;
    NavigationView navigationDrawer;
    Toolbar toolbar;
    MainActivityViewModel mainActivityViewModel;
    Button morningButton;
    private static boolean isFirstTIme = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        // load the home fragment when the app starts
//        getSupportFragmentManager().beginTransaction().replace(R.layout.fragment_home, new HomeFragment()).commit();

        bottomNavigation = findViewById(R.id.bottom_navigation);

        //Add menu item
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_baseline_home_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_baseline_add_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_action_name));

        setupNavigation();

       FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
       transaction.replace(R.id.frame_layout, new HomeFragment());
        transaction.addToBackStack(null);
        transaction.commit();

        navigationDrawer = findViewById(R.id.navigation_drawer);

        navigationDrawerMenuItemsOnClickListeners();



//            morningButton= findViewById(R.id.morning_yoga_button);
//            morningButton.setOnClickListener(v -> {
//         FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//           transaction.replace(R.id.container, new MorningYogaFragment()).commit();
//            });




    }


    private void navigationDrawerMenuItemsOnClickListeners() {
        MenuItem signOut = navigationDrawer.getMenu().findItem(R.id.nav_sign_out);
        signOut.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
              mainActivityViewModel.signOut();
        goToSignInActivity();
                return true;
            }
        });
    }

    private void goToSignInActivity()

    {

        startActivity(new Intent(this, SignInActivity.class));
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.openDrawer(GravityCompat.START);
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    private void setupNavigation() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_home_24);

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
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
