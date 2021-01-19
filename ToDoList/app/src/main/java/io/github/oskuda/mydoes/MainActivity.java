package io.github.oskuda.mydoes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    //variables for splash screen delay
    private static final int SPLASH_SCREEN_DELAY=4500;
    private static final String USERNAME_KEY="user_name";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //animation init
        //variables for animations
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);

        //hooks
        //view components
        ImageView companyLogo = findViewById(R.id.imageCompanyLogo);
        TextView mainAppName = findViewById(R.id.mainAppName);
        TextView developerCredit = findViewById(R.id.developerCredit);

        //assign animation to view components
        companyLogo.setAnimation(slideDown);
        mainAppName.setAnimation(slideUp);
        developerCredit.setAnimation(slideUp);

        //make sure data is saved locally hence data is persisted even when internet connection is lost
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //sharedPreferences code
        //SharedPreferences to store userName locally
        SharedPreferences sharedPreferences = getSharedPreferences("alternate_db", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString(USERNAME_KEY,"NULL");

        //using handler to delay and intent to dashboard
        new Handler().postDelayed(() -> {
            //intent to required activity

            Intent intent;
            if(userName.equals("NULL")){
                intent = new Intent(MainActivity.this, UserLogin.class);
            }else{
                intent = new Intent(MainActivity.this, Dashboard.class);
                //remove splash screen intent to prevent coming back to this activity by pressing back key
            }
            startActivity(intent);
            finish();
        },SPLASH_SCREEN_DELAY);
    }
}