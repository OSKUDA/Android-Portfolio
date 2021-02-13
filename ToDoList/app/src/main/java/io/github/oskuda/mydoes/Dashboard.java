package io.github.oskuda.mydoes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {
    //variable for view components
    TextView titlePage, usernamePage, endPage;
    Button btnAddNew;

    //firebase variables
    DatabaseReference reference;

    RecyclerView ourDoes;
    ArrayList<MyDoes> list;
    DoesAdapter doesAdapter;


    private static final String USERNAME_KEY="user_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //hooks
        titlePage = findViewById(R.id.titlePage);
        usernamePage = findViewById(R.id.usernamePage);
        endPage = findViewById(R.id.endPage);
        btnAddNew = findViewById(R.id.btnAddNew);

        //import font
        Typeface MLight = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Light.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Medium.ttf");

        //customize font
        titlePage.setTypeface(MMedium);
        usernamePage.setTypeface(MLight);
        endPage.setTypeface(MLight);
        btnAddNew.setTypeface(MLight);

        //set username
        //sharedPreferences code
        //SharedPreferences to store userName locally
        SharedPreferences sharedPreferences = getSharedPreferences("alternate_db", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString(USERNAME_KEY,"NULL");
        usernamePage.setText(userName);

        //add onclick listener to add new task button
        btnAddNew.setOnClickListener(view -> {
            Intent intent = new Intent(Dashboard.this, NewTaskActivity.class);
            startActivity(intent);
        });

        //work with data
        ourDoes = findViewById(R.id.ourDoes);
        ourDoes.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        doesAdapter = new DoesAdapter(Dashboard.this,list);

        //get data from firebase
        reference = FirebaseDatabase.getInstance().getReference().child("DoesApp").child(userName);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //set code to retrieve data and replace layout

                for(DataSnapshot dataSnapshot1: snapshot.getChildren()){
                    MyDoes p = dataSnapshot1.getValue(MyDoes.class);
                    list.add(p);
                }
                ourDoes.setAdapter(doesAdapter);
                doesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}