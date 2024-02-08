package com.example.studenthub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private ImageView ic_logo;
    private TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ic_logo = findViewById(R.id.ic_logo);
        appName = findViewById(R.id.text_appName);

        Animation scaleAndfadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_and_fade_in);
        ic_logo.startAnimation(scaleAndfadeInAnimation);
        appName.startAnimation(scaleAndfadeInAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class); //n
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}