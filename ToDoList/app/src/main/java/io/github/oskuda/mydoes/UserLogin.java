package io.github.oskuda.mydoes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserLogin extends AppCompatActivity {

    //variable for view component
    EditText txtUserName;
    Button btnSaveUserName;

    //firebase variables
    DatabaseReference reference;

    //array to store registered username
    ArrayList<String> userNames;

    private static final String USERNAME_KEY="user_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        //hooks
        txtUserName = findViewById(R.id.txtUserName);
        btnSaveUserName = findViewById(R.id.btnSaveUserName);

        //shared preferences code
        //SharedPreferences to store userName locally
        SharedPreferences sharedPreferences = getSharedPreferences("alternate_db", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //firebase code
        reference = FirebaseDatabase.getInstance().getReference().child("DoesApp");
        userNames = new ArrayList<>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //set code to retrieve data and replace layout
                for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                    userNames.add(childSnapshot.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //do nothing
            }
        });



        //add event listener to button
        btnSaveUserName.setOnClickListener(view -> {
            //check for valid input
            if(txtUserName.getText().toString().equals("")){
                Toast.makeText(this, "Invalid username", Toast.LENGTH_SHORT).show();
            }else if(userNames.contains(txtUserName.getText().toString())){
                Toast.makeText(this, "Username already taken", Toast.LENGTH_LONG).show();
            }else{
                editor.putString(USERNAME_KEY,txtUserName.getText().toString());
                editor.apply();
                Toast.makeText(UserLogin.this, txtUserName.getText().toString()+" registered!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserLogin.this,Dashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });





    }
}