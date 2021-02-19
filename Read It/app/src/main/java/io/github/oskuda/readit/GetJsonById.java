package io.github.oskuda.readit;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;

public class GetJsonById extends AsyncTask<String, Void, String> implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetJsonById";
    /*
    API KEY FOR GUARDIAN API
    PRIVATE KEY
     */
    private static final String API_KEY = "api-key=54a5bc5a-dfad-4ce8-b769-b7230b52f674";//TODO: Enter your API key here
    private static final String BASE_URL = "https://content.guardianapis.com/";
    private static final String SHOW_FIELDS = "body";

    private String mBodyStringHTML;
    private String mBodyString;
    private String mId;
    private OnDataAvailable mCallback;


    interface OnDataAvailable {
        void onDataAvailable(String bodyString, DownloadStatus downloadStatus);
    }

    public GetJsonById(OnDataAvailable callback, String id) {
        mId = id;
        mCallback = callback;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (mCallback != null) {
            mCallback.onDataAvailable(mBodyString, DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: starts");
        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(strings[0]);

        Log.d(TAG, "doInBackground: ends");
        return null;
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus mDownloadStatus) {
        Log.d(TAG, "onDownloadComplete: starts");
        if (mDownloadStatus == DownloadStatus.OK) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONObject jsonResponse = jsonObject.getJSONObject("response");
                JSONObject jsonContent = jsonResponse.getJSONObject("content");
                JSONObject jsonField = jsonContent.getJSONObject("fields");
                mBodyStringHTML = jsonField.getString("body");
            } catch (JSONException e) {
                Log.e(TAG, "onDownloadComplete: JSONException: " + e.getMessage());
            }
        }
        Log.d(TAG, "onDownloadComplete: HTML STRING is:" + mBodyStringHTML);
        mBodyString = Jsoup.parse(mBodyStringHTML.replace("</p>", "\n\n")).wholeText();
        Log.d(TAG, "onDownloadComplete: PARSED STRING IS:" + mBodyString);
    }

    public String urlGenerator() {
        return BASE_URL + this.getId() +
                "?" + "show-fields=" +
                SHOW_FIELDS + "&" +
                API_KEY;
    }

    public String getId() {
        return mId;
    }
}
