package com.cristiandrami.football365.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.cristiandrami.football365.R;
import com.cristiandrami.football365.model.utilities.UtilitiesStrings;
import com.cristiandrami.football365.ui.login.LoginActivity;
/**
 * This class is used as a Controller for the Splash Activity
 *
 * @author Cristian D. Dramisino
 *
 */
public class SplashActivity extends AppCompatActivity {

    private Animation topAnimation, bottomAnimation;
    private ImageView logoImage;
    private TextView appName, descriptionText;


    //This variable sets the delay time before pass on login activity
    //In this case we wait 3 seconds
    private static int SPLASH_SCREEN = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);


        graphicalComponentsBind();
        setAnimations();

        switchToLoginActivity();





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
    }

    private void graphicalComponentsBind() {
        logoImage= findViewById(R.id.app_logo_splash);
        appName=findViewById(R.id.app_name_splash);
        descriptionText=findViewById(R.id.description_splash_text);
    }

}