package com.app.medicfarma.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.app.medicfarma.R;

import gr.net.maroulis.library.EasySplashScreen;

public class WelcomeScreen extends AppCompatActivity {
    private static int WELCOME_TIMEOUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main2);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent welcome = new Intent(WelcomeScreen.this, StartActivity.class);
                startActivity(welcome);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_in);
                finish();
            }
        },WELCOME_TIMEOUT);

        EasySplashScreen config = new EasySplashScreen(WelcomeScreen.this)
                .withFullScreen()
                //    .withTargetActivity(Slide.class)
                .withSplashTimeOut(2500)
                .withBackgroundColor(R.color.blanco)
                .withLogo(R.drawable.logo_new)
                .withHeaderText("")
                .withFooterText("")
                .withBeforeLogoText("")
                .withAfterLogoText("");

        //Set color text
        config.getHeaderTextView().setTextColor(Color.BLACK);
        config.getFooterTextView().setTextColor(Color.BLACK);
        config.getAfterLogoTextView().setTextColor(Color.BLACK);
        config.getBeforeLogoTextView().setTextColor(Color.BLACK);

        //set to view
        View view = config.create();

        //set view to content view
        setContentView(view);

    }
}
