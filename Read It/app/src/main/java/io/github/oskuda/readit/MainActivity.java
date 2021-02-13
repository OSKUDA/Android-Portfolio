package io.github.oskuda.readit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
/*
CLASS DESCRIPTION: This is the launcher activity.
                    It is used to show SPLASH screen containing companylogo, appName, and creator credits
                    This screen is shown for 4.5sec.
                    After the time is elapsed, dashboard activity is launched and this activity is killed.
FEATURES: Uses animation 'slide_up&slide_down' defined on res/anim/slide_up.xml & res/anim/slide_down.xml
          Uses Handler class with .postDelayed(Runnable,delay) to implement delay before launching dashboard activity
 */

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
        ImageView splash_screen_company_logo = findViewById(R.id.splash_screen_company_logo);
        TextView splash_screen_app_name = findViewById(R.id.splash_screen_app_name);
        TextView splash_screen_creator = findViewById(R.id.splash_screen_creator);

        //assign animation to view components
        splash_screen_company_logo.setAnimation(slideDown);
        splash_screen_creator.setAnimation(slideUp);
        splash_screen_app_name.setAnimation(slideUp);

        //using handler to delay and intent to dashboard
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, Dashboard.class);
            startActivity(intent);
            finish();
        },SPLASH_SCREEN_DELAY);

        Log.d(TAG, "onCreate: ends");

    }
}