package io.github.oskuda.readit;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
CLASS DESCRIPTION: This class uses multi threading to download data form API on background thread.
                   It calls GetRawData with generated URL and receive it back on callBack method onDownloadComplete.
FUNCTION: To parse JSON data received from GetRawData into NewsArticle object.
          Generate valid URI.
 */
public class GetJsonByContent extends AsyncTask<String, Void, ArrayList<NewsArticle>> implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetJsonByContent";

    /*
    API KEY FOR GUARDIAN API
    PRIVATE KEY
     */
    private static final String API_KEY = "api-key=54a5bc5a-dfad-4ce8-b769-b7230b52f674";//TODO: Enter your API key here
    private static final String BASE_URL = "https://content.guardianapis.com/search";
    private static final String SHOW_FIELDS = "headline,thumbnail";

    private final String mSearchQuery;
    private final String mTag;
    private final String mPageNumber;
    private final String mPageSize;
    private final String mOrderBy;

    //callback variable
    OnDataAvailable mCallback;
    //List to hold newsArticle object
    private ArrayList<NewsArticle> mNewsArticleList;

    /*
    OnDataAvailable interface is used as callback method to calling object
    It helps perform task after Json data is parsed into mNewsArticleList
     */
    interface OnDataAvailable {
        void onDataAvailable(ArrayList<NewsArticle> newsArticleList, DownloadStatus downloadStatus);
    }

    public GetJsonByContent(OnDataAvailable mCallback, String searchQuery, String tag, String pageNumber, String pageSize, String orderBy) {
        this.mCallback = mCallback;
        mSearchQuery = searchQuery;
        mTag = tag;
        mPageNumber = pageNumber;
        mPageSize = pageSize;
        mOrderBy = orderBy;
    }

    @Override
    protected void onPostExecute(ArrayList<NewsArticle> newsArticles) {
        Log.d(TAG, "onPostExecute: starts");
        super.onPostExecute(newsArticles);
        if (mCallback != null) {
            mCallback.onDataAvailable(mNewsArticleList, DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected ArrayList<NewsArticle> doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: starts");
        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(strings[0]);

        Log.d(TAG, "doInBackground: ends");
        return mNewsArticleList;
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus mDownloadStatus) {
        Log.d(TAG, "onDownloadComplete: starts with status: " + mDownloadStatus);

        if (mDownloadStatus == DownloadStatus.OK) {
            mNewsArticleList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONObject jsonResponse = jsonObject.getJSONObject("response");
                JSONArray itemArray = jsonResponse.getJSONArray("results");

                for (int i = 0; i < itemArray.length(); i++) {
                    JSONObject newsArticle = itemArray.getJSONObject(i);

                    String id = newsArticle.getString("id");
                    Log.d(TAG, "onDownloadComplete: id" + id);
                    String publicationDate = newsArticle.getString("webPublicationDate");

                    JSONObject fields = newsArticle.getJSONObject("fields");
                    String headline = fields.getString("headline");
                    String imageUri = fields.getString("thumbnail");

                    NewsArticle newsArticleObj = new NewsArticle(id, publicationDate, headline, imageUri);
                    mNewsArticleList.add(newsArticleObj);

                    Log.d(TAG, "onDownloadComplete: added: " + newsArticle.toString());
                }
            } catch (JSONException e) {
                Log.e(TAG, "onDownloadComplete: JSONException caught: " + e.getMessage());
                mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
            }
        }
        Log.d(TAG, "onDownloadComplete: ends");
    }

    public String urlGenerator() {
        Log.d(TAG, "urlGenerator: starts");

        //generate uri
        StringBuilder resultUri = new StringBuilder();
        resultUri.append(BASE_URL).append("?q=").append(this.getSearchQuery()).append("&")
                .append("tag=").append(this.getTag()).append("&").append("page=").append(this.getPageNumber())
                .append("&").append("page-size=").append(this.getPageSize()).append("&format=json&")
                .append("order-by=").append(this.getOrderBy()).append("&").append("show-fields=").append(SHOW_FIELDS)
                .append("&").append(API_KEY);
        Log.d(TAG, "urlGenerator: returns with: " + resultUri.toString());
        return resultUri.toString();
    }

    private String getSearchQuery() {
        return mSearchQuery;
    }

    private String getTag() {
        return mTag;
    }

    private String getPageNumber() {
        return mPageNumber;
    }

    private String getPageSize() {
        return mPageSize;
    }

    private String getOrderBy() {
        return mOrderBy;
    }
}
