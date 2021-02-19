package io.github.oskuda.readit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class NewsArticleDetail extends AppCompatActivity implements GetJsonById.OnDataAvailable {
    private static final String TAG = "NewsArticleDetail";
    //UI components
    ImageView detailImage;
    TextView txtHeadlineDetail;
    TextView txtDescriptionDetail;
    TextView txtPublicationDate;

    //KEY for sharedPreference
    static final String NEWS_ARTICLE_TRANSFER = "PHOTO_TRANSFER";

    private NewsArticle mNewsArticle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article_detail);

        //Toolbar setup
        //add back_to_parent action
        Toolbar toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();

        //Enable the Up button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        //hooks
        detailImage = findViewById(R.id.detailImage);
        txtHeadlineDetail = findViewById(R.id.txtHeadlineDetail);
        txtDescriptionDetail = findViewById(R.id.txtDescriptionDetail);
        txtPublicationDate = findViewById(R.id.txtPublicationDate);

        //custom fonts
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Medium.ttf");
        Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Light.ttf");

        //add custom fonts to views
        txtHeadlineDetail.setTypeface(MMedium);
        txtDescriptionDetail.setTypeface(MLight);
        txtPublicationDate.setTypeface(MLight);

        //get intent
        Intent intent = getIntent();
        mNewsArticle = (NewsArticle) intent.getSerializableExtra(NEWS_ARTICLE_TRANSFER);

        GetJsonById getJsonById = new GetJsonById(this, mNewsArticle.getId());
        String url = getJsonById.urlGenerator();
        getJsonById.execute(url);


    }

    @Override
    public void onDataAvailable(String bodyString, DownloadStatus downloadStatus) {
        Log.d(TAG, "onDataAvailable: starts");
        if (downloadStatus == DownloadStatus.OK) {
            txtHeadlineDetail.setText(mNewsArticle.getHeadline());
            txtDescriptionDetail.setText(bodyString);
            txtPublicationDate.setText(mNewsArticle.dateFormatter());
            Picasso.get()
                    .load(mNewsArticle.getImageUri())
                    .resize(800, 650)
                    .centerCrop()
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(detailImage);
        }
        Log.d(TAG, "onDataAvailable: ends");
    }
}