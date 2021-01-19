package io.github.oskuda.mydoes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class EditTaskDetails extends AppCompatActivity {

    //variable for view components
    EditText titleDoes, descDoes, dateDoes;
    Button btnSaveUpdate,btnDelete;
    DatabaseReference reference;

    private static final String USERNAME_KEY="user_name";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task_details);

        //hooks
        titleDoes = findViewById(R.id.titleDoes);
        descDoes = findViewById(R.id.descDoes);
        dateDoes = findViewById(R.id.dateDoes);
        btnSaveUpdate = findViewById(R.id.btnSaveUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        //get value from previous activity
        titleDoes.setText(getIntent().getStringExtra("titleDoes"));
        descDoes.setText(getIntent().getStringExtra("descDoes"));
        dateDoes.setText(getIntent().getStringExtra("dateDoes"));

        String keyDoes = getIntent().getStringExtra("keyDoes");
        dateDoes.setText(getIntent().getStringExtra("dateDoes"));


        //sharedPreferences code
        //SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("alternate_db", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString(USERNAME_KEY,"NULL");

        //add event listener for button
        btnSaveUpdate.setOnClickListener(view -> {
            //reference our database
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
                            Intent intent = new Intent(EditTaskDetails.this, Dashboard.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(EditTaskDetails.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            });
        });
        

       
        

        btnDelete.setOnClickListener(view -> {
            //reference our database
            reference = FirebaseDatabase.getInstance().getReference()
                    .child("DoesApp")
                    .child(userName)
                    .child("Does"+ keyDoes);

            reference.removeValue().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(EditTaskDetails.this, "Deleted", Toast.LENGTH_SHORT).show();

                            //intent to required activity
                            Intent intent = new Intent(EditTaskDetails.this, Dashboard.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);



                }else{
                    Toast.makeText(EditTaskDetails.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            });
        });


    }
}