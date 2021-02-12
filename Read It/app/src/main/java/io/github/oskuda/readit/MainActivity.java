package io.github.oskuda.readit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    //variables for splash screen delay
    private static final int SPLASH_SCREEN_DELAY=4500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //animation setup
        Animation slideUp = AnimationUtils.loadAnimation(this,R.anim.slide_up);
        Animation slideDown = AnimationUtils.loadAnimation(this,R.anim.slide_down);

        //hooks for view components
        ImageView splash_screen_company_logo = (ImageView) findViewById(R.id.splash_screen_company_logo);
        TextView splash_screen_app_name = (TextView) findViewById(R.id.splash_screen_app_name);
        TextView splash_screen_creator = (TextView) findViewById(R.id.splash_screen_creator);

        //assign animation to view components
        splash_screen_company_logo.setAnimation(slideDown);
        splash_screen_creator.setAnimation(slideUp);
        splash_screen_app_name.setAnimation(slideUp);

        //using handler to delay and intent to dashboard
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN_DELAY);

        Log.d(TAG, "onCreate: ends");

    }
}