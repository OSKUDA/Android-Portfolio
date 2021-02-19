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
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity implements GetJsonByContent.OnDataAvailable, RecyclerItemClickListener.OnRecyclerClickListener {
    private static final String TAG = "Dashboard";

    NewsArticleRecyclerViewAdapter mNewsArticleRecyclerViewAdapter;

    //userInput data holder
    String searchQuery, region, orderBy;

    //KEY for sharedPreference
    private static final String SEARCH_QUERY = "SEARCH_QUERY";
    private static final String REGION = "REGION";
    private static final String ORDER_BY = "ORDER_BY";
    static final String NEWS_ARTICLE_TRANSFER = "PHOTO_TRANSFER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        RecyclerView recyclerView = findViewById(R.id.main_content_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));

        mNewsArticleRecyclerViewAdapter = new NewsArticleRecyclerViewAdapter(this, new ArrayList<NewsArticle>());

        recyclerView.setAdapter(mNewsArticleRecyclerViewAdapter);

        Log.d(TAG, "onCreate: ends");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();

        //get data from sharedPreference
        SharedPreferences sharedPreferences = getSharedPreferences("local_db", Context.MODE_PRIVATE);

        searchQuery = sharedPreferences.getString(SEARCH_QUERY, null);
        region = sharedPreferences.getString(REGION, null);
        orderBy = sharedPreferences.getString(ORDER_BY, null);

        if (searchQuery != null && region != null && orderBy != null) {
            GetJsonByContent getJsonByContent = new GetJsonByContent(this, searchQuery, region, "1", "50", orderBy);
            String url = getJsonByContent.urlGenerator();
            getJsonByContent.execute(url);
        } else {
            Log.d(TAG, "onResume: default query loaded");
            //testing
            GetJsonByContent getJsonByContent = new GetJsonByContent(this, "covid", "world/world"
                    , "1", "50", "newest");
            String url = getJsonByContent.urlGenerator();
            getJsonByContent.execute(url);
        }

        Log.d(TAG, "onResume: ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.app_bar_search) {
            Intent intent = new Intent(Dashboard.this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataAvailable(ArrayList<NewsArticle> newsArticleList, DownloadStatus downloadStatus) {
        Log.d(TAG, "onDataAvailable: starts");

        if (downloadStatus == DownloadStatus.OK) {
            mNewsArticleRecyclerViewAdapter.loadNewData(newsArticleList);
        } else {
            //download or processing failed
            Log.e(TAG, "onDataAvailable: failed with status: " + downloadStatus);
        }
        Log.d(TAG, "onDataAvailable: ends");
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: starts");
        Toast.makeText(Dashboard.this, "Hold to open!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.d(TAG, "onItemLongClick: starts");
        Intent intent = new Intent(Dashboard.this, NewsArticleDetail.class);
        intent.putExtra(NEWS_ARTICLE_TRANSFER, mNewsArticleRecyclerViewAdapter.getNewsArticle(position));
        startActivity(intent);
    }
}