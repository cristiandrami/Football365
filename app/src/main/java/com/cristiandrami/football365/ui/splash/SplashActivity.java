package com.cristiandrami.football365.ui.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cristiandrami.football365.R;
import com.cristiandrami.football365.model.AppUtilities;
import com.cristiandrami.football365.model.detailed_match.comment.CommentItemsListTest;
import com.cristiandrami.football365.model.detailed_match.line_up.PlayersListTest;
import com.cristiandrami.football365.model.likedNews.LikedNewsUtilities;
import com.cristiandrami.football365.model.utilities.UtilitiesStrings;
import com.cristiandrami.football365.model.utilities.matches_utilities.CompetitionsUtilities;
import com.cristiandrami.football365.ui.login.LoginActivity;
/**
 * This class is used as a Controller for the Splash Activity
 *
 * @author Cristian D. Dramisino
 *
 */
public class SplashActivity extends AppCompatActivity {

    private Animation topAnimation;
    private Animation   bottomAnimation;
    private ImageView logoImage;
    private TextView appName;
    private TextView descriptionText;
    private ProgressBar progressBar;


    //This variable sets the delay time before pass on login activity
    //In this case we wait 3 seconds
    private static int SPLASH_SCREEN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.getSupportActionBar().hide();
        setupAllApp();

        graphicalComponentsBind();
        setAnimations();

        switchToLoginActivity();

    }

    private void setupAllApp() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String actualTheme=preferences.getString(AppUtilities.THEME_PREFERENCE_KEY, "");
        if(actualTheme.equals("")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        CompetitionsUtilities.getInstance();
        PlayersListTest.getInstance();
        CommentItemsListTest.getInstance();
        LikedNewsUtilities.getInstance();

    }

    private void switchToLoginActivity() {
        new Handler().postDelayed(()-> {

                Intent switchingIntent= new Intent(SplashActivity.this, LoginActivity.class);

                Pair[] pairs= new Pair[2];
                pairs[0] = new Pair<View, String> (logoImage, UtilitiesStrings.SPLASH_TRANSACTION_LOGO_NAME);
                pairs[1] = new Pair<View, String> (descriptionText, UtilitiesStrings.SPLASH_TRANSACTION_TEXT_NAME);

                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, pairs);
                startActivity(switchingIntent, options.toBundle());

                // finish is used to don't allow user to go back on splash activity from login
                finish();

        }, SPLASH_SCREEN);
    }

    private void setAnimations() {
        topAnimation= AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation= AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        logoImage.setAnimation(topAnimation);
        appName.setAnimation(bottomAnimation);
        descriptionText.setAnimation(bottomAnimation);
        progressBar.setAnimation(bottomAnimation);
    }

    private void graphicalComponentsBind() {
        logoImage= findViewById(R.id.app_logo_splash);
        appName=findViewById(R.id.app_name_splash);
        descriptionText=findViewById(R.id.description_splash_text);
        progressBar= findViewById(R.id.activity_splash_progressBar);
    }

}