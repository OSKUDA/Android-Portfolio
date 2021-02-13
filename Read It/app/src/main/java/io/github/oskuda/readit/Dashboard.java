package io.github.oskuda.readit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity implements GetJsonByContent.OnDataAvailable {
    private static final String TAG = "Dashboard";

    NewsArticleRecyclerViewAdapter mNewsArticleRecyclerViewAdapter;

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
        setContentView(R.layout.activity_dashboard);

        RecyclerView recyclerView = findViewById(R.id.main_content_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNewsArticleRecyclerViewAdapter = new NewsArticleRecyclerViewAdapter(this,new ArrayList<NewsArticle>());

        recyclerView.setAdapter(mNewsArticleRecyclerViewAdapter);

        Log.d(TAG, "onCreate: ends");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();

        //get data from sharedPreference
        SharedPreferences sharedPreferences = getSharedPreferences("local_db", Context.MODE_PRIVATE);

        searchQuery = sharedPreferences.getString(SEARCH_QUERY,null);
        region = sharedPreferences.getString(REGION,null);
        orderBy = sharedPreferences.getString(ORDER_BY,null);

        if(searchQuery!=null && region!=null && orderBy!=null){
            GetJsonByContent getJsonByContent = new GetJsonByContent(this,searchQuery,region,"1","50",orderBy);
            String url = getJsonByContent.urlGenerator();
            getJsonByContent.execute(url);
        }else{
            Log.d(TAG, "onResume: default query loaded");
            //testing
            GetJsonByContent getJsonByContent = new GetJsonByContent(this,"covid-nepal","world/nepal"
                    ,"1","50","relevance");
            String url = getJsonByContent.urlGenerator();
            getJsonByContent.execute(url);
        }



        Log.d(TAG, "onResume: ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.app_bar_search){
            Intent intent = new Intent(Dashboard.this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataAvailable(ArrayList<NewsArticle> newsArticleList, DownloadStatus downloadStatus) {
        Log.d(TAG, "onDataAvailable: starts");

        if(downloadStatus==DownloadStatus.OK){
            mNewsArticleRecyclerViewAdapter.loadNewData(newsArticleList);
        }else{
            //download or processing failed
            Log.e(TAG, "onDataAvailable: failed with status: "+downloadStatus);
        }
        Log.d(TAG, "onDataAvailable: ends");
    }
}