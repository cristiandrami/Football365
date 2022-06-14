package com.cristiandrami.football365;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.cristiandrami.football365.databinding.ActivityMainBinding;
import com.cristiandrami.football365.model.AppUtilities;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();



        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setThemeByPreferences();



        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_news, R.id.navigation_matches, R.id.navigation_liked_news, R.id.navigation_profile)
                .build();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);

        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        binding.navView.setBackgroundColor(getResources().getColor(R.color.background_color));

        binding.getRoot().setBackgroundColor(getResources().getColor(R.color.background_color));

    }

    private void setThemeByPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String actualTheme=preferences.getString(AppUtilities.THEME_PREFERENCE_KEY, "");
        if(actualTheme.equals(AppUtilities.DARK_THEME)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


    }

    /*
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment);
        fragmentTransaction.commit();
    }
    */


    /*@Override
    public void onStart(){
        super.onStart();


        binding.navView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.navigation_news:
                    replaceFragment(new NewsFragment());
                    break;
                case R.id.navigation_bets:
                    replaceFragment(new BetsFragment());
                    break;
                case R.id.navigation_matches:
                    replaceFragment(new MatchesFragment());
                    break;
                case R.id.navigation_profile:
                    replaceFragment(new ProfileFragment());
                    break;

                default:
                    break;


            }
            return true;
        });

    }*/


}