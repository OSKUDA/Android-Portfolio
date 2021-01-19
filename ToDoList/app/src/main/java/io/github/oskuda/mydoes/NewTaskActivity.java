package io.github.oskuda.mydoes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

public class NewTaskActivity extends AppCompatActivity {
    TextView titlePage, addTitle, addDescription, addDate;
    EditText titleDoes, descDoes, dateDoes;
    Button btnSaveTask, btnCancel;
    DatabaseReference reference;
    String keyDoes;

    private static final String USERNAME_KEY="user_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        //hooks
        titlePage = findViewById(R.id.titlePage);
        addTitle = findViewById(R.id.addTile);
        titleDoes = findViewById(R.id.titleDoes);
        addDescription = findViewById(R.id.addDescription);
        addDate = findViewById(R.id.addDate);
        descDoes = findViewById(R.id.descDoes);
        dateDoes = findViewById(R.id.dateDoes);
        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnCancel = findViewById(R.id.btnCancel);

        //sharedPreferences code
        //SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("alternate_db", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString(USERNAME_KEY,"NULL");


        //assign unique UUID
        keyDoes = UUID.randomUUID().toString();

        //add on click listener
        btnSaveTask.setOnClickListener(view -> {
            //check for valid input
            if(titleDoes.getText().toString().equals("") || descDoes.getText().toString().equals("") || dateDoes.getText().toString().equals("")){
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            }else{
                // insert data to database
                reference = FirebaseDatabase.getInstance().getReference()
                        .child("DoesApp")
                        .child(userName)
                        .child("Does"+ keyDoes);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().child("titleDoes").setValue(titleDoes.getText().toString());
                        snapshot.getRef().child("descDoes").setValue(descDoes.getText().toString());
                        snapshot.getRef().child("dateDoes").setValue(dateDoes.getText().toString());
                        snapshot.getRef().child("keyDoes").setValue(keyDoes);

                        //intent to required activity
                        Intent intent = new Intent(NewTaskActivity.this, Dashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(NewTaskActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btnCancel.setOnClickListener(view -> {
            //function to finish current activity and move to previous one
            onBackPressed();
        });

        //import font
        Typeface MLight = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Light.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Medium.ttf");

        // customize font
        titlePage.setTypeface(MMedium);

        addTitle.setTypeface(MLight);
        titleDoes.setTypeface(MMedium);

        addDescription.setTypeface(MLight);
        descDoes.setTypeface(MMedium);

        addDate.setTypeface(MLight);
        dateDoes.setTypeface(MMedium);

        btnSaveTask.setTypeface(MMedium);
        btnCancel.setTypeface(MLight);



    }
}