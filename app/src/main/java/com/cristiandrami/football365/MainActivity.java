package com.cristiandrami.football365;

import android.os.Bundle;

import com.cristiandrami.football365.ui.bets.BetsFragment;
import com.cristiandrami.football365.ui.matches.MatchesFragment;
import com.cristiandrami.football365.ui.news.NewsFragment;
import com.cristiandrami.football365.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cristiandrami.football365.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //replaceFragment(new NewsFragment());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_news, R.id.navigation_matches, R.id.navigation_bets, R.id.navigation_profile)
                .build();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        NavController navController = navHostFragment.getNavController();
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment);
        fragmentTransaction.commit();
    }

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