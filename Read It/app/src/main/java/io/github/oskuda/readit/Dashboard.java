package io.github.oskuda.readit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class Dashboard extends AppCompatActivity implements GetJsonByContent.OnDataAvailable {
    private static final String TAG = "Dashboard";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Log.d(TAG, "onCreate: ends");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();

        //testing
        GetJsonByContent getJsonByContent = new GetJsonByContent(this,"covid-nepal","world/nepal"
        ,"1","10","relevance");
        String url = getJsonByContent.urlGenerator();
        getJsonByContent.execute(url);

        Log.d(TAG, "onResume: ends");
    }

    @Override
    public void onDataAvailable(List<NewsArticle> newsArticleList, DownloadStatus downloadStatus) {
        //TODO
        Log.d(TAG, "onDataAvailable: reached");
    }
}