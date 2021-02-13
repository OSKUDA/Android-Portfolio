package io.github.oskuda.readit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Type;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";

    //variables for view components
    TextView txtSearchTitle,txtSearchQuery,txtRegion,txtOrderBy;
    EditText editTxtSearchQuery,editTxtRegion;
    Spinner spinnerOrderBy;
    Button btnSubmit;

    //userInput data holder
    String searchQuery,region,orderBy;

    //KEY for sharedPreference
    private static final String SEARCH_QUERY = "SEARCH_QUERY";
    private static final String REGION = "REGION";
    private static final String ORDER_BY = "ORDER_BY";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Toolbar setup
        //add back_to_parent action
        Toolbar toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();

        //Enable the Up button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        //hooks
        txtSearchTitle = findViewById(R.id.txtSearchTitle);
        txtSearchQuery = findViewById(R.id.txtSearchQuery);
        txtRegion = findViewById(R.id.txtRegion);
        txtOrderBy = findViewById(R.id.txtOrderBy);
        editTxtSearchQuery = findViewById(R.id.editTxtSearchQuery);
        editTxtRegion = findViewById(R.id.editTxtRegion);
        btnSubmit = findViewById(R.id.btnSubmit);
        spinnerOrderBy = findViewById(R.id.spinnerOrderBy);

        //custom fonts
        Typeface MMedium = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Medium.ttf");
        Typeface MLight = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Light.ttf");

        //assign fonts to view components
        txtSearchTitle.setTypeface(MMedium);
        txtSearchQuery.setTypeface(MMedium);
        txtRegion.setTypeface(MMedium);
        txtOrderBy.setTypeface(MMedium);
        editTxtSearchQuery.setTypeface(MLight);
        editTxtRegion.setTypeface(MLight);

        //spinner configuration
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.order_by_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrderBy.setAdapter(adapter);

        //submit btn event handler
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInput()){
                    searchQuery = formatQuery(editTxtSearchQuery.getText().toString());
                    Log.d(TAG, "onClick: searchQueryBuilt is ==>"+searchQuery);
                    region = formatRegion(editTxtRegion.getText().toString().toLowerCase());
                    Log.d(TAG, "onClick: regionBuilt is ==>"+region);
                    TextView selectionView = (TextView)spinnerOrderBy.getSelectedView();
                    orderBy = selectionView.getText().toString();
                    Log.d(TAG, "onClick: orderByBuilt is ==>"+orderBy);

                    SharedPreferences sharedPreferences = getSharedPreferences("local_db", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString(SEARCH_QUERY,searchQuery);
                    editor.putString(REGION,region);
                    editor.putString(ORDER_BY,orderBy);
                    editor.apply();

                    //kill the activity
                    finish();



                }
            }
        });

        Log.d(TAG, "onCreate: ends");
    }
    private boolean validateInput(){
        //check if all fields are filled
        if(editTxtSearchQuery.getText().toString().equals("") || editTxtRegion.getText().toString().equals("")){
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_LONG).show();
            return false;
        }
        //check if special characters are entered
        else if(hasSpecialCharacter(editTxtSearchQuery.getText().toString()) || hasSpecialCharacter(editTxtRegion.getText().toString())){
            Toast.makeText(this, "Don't enter special characters such as !,@,#,$...", Toast.LENGTH_LONG).show();
            return false;
        }
        //check if region has numbers
        else if(!hasRequiredRegion(editTxtRegion.getText().toString())){
            Toast.makeText(this, "Invalid region!", Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }
    private boolean hasSpecialCharacter(String data){
        for(int i=0;i<data.length();i++){
            int testChar = (int)data.charAt(i);
            if(!((testChar>=48 && testChar<=57) || (testChar==32) ||(testChar>=65 && testChar<=90) || (testChar>=97 && testChar<=122))){
                return true;
            }
        }
        return false;
    }
    private boolean hasRequiredRegion(String data){
        for(int i=0;i<data.length();i++){
            int testChar = (int)data.charAt(i);
            if(!((testChar>=65 && testChar<=90) || (testChar>=97 && testChar<=122))){
                return false;
            }
        }
        return true;
    }
    private String formatQuery(String data){
        StringBuilder result = new StringBuilder();
        for(int i=0;i<data.length();i++){
            if(((int)data.charAt(i))==32){
                result.append("%20");
            }else{
                result.append(data.charAt(i));
            }
        }
        return result.toString();
    }
    private String formatRegion(String data){
        return ("world/"+data);
    }
}