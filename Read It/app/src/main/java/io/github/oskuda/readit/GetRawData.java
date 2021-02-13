package io.github.oskuda.readit;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus{IDLE,PROCESSING,NOT_INITIALIZED,FAILED_OR_EMPTY,OK}
/*
CLASS DESCRIPTION: This class downloads data over the internet using HttpUrlConnection.
                   Checks for Exception and act accordingly.
                   Sends data downloaded back to calling object using callBack method OnDownloadComplete.
 */
public class GetRawData {
    private static final String TAG = "GetRawData";

    DownloadStatus mDownloadStatus;
    OnDownloadComplete mCallback;

    /*
    OnDownloadComplete interface is used for callback method to calling object.
    It helps to perform task after the download has completed.
     */
    public interface OnDownloadComplete {
        void onDownloadComplete(String data, DownloadStatus mDownloadStatus);
    }

    public GetRawData(OnDownloadComplete mCallback) {
        this.mCallback = mCallback;
        mDownloadStatus = DownloadStatus.IDLE;
    }
    void runInSameThread(String s){
        Log.d(TAG, "runInSameThread: starts with value "+s);

        String result = doInBackground(s);
        Log.d(TAG, "runInSameThread: data after downloading: "+result);

        /*
        send the downloaded data to callback object.
        if null return null
         */
        if(mCallback!=null){
            mCallback.onDownloadComplete(doInBackground(s),mDownloadStatus);
        }else{
            Log.e(TAG, "runInSameThread: call back method not found");
        }
        Log.d(TAG, "runInSameThread: ends");
    }




    private String doInBackground(String s){
        Log.d(TAG, "doInBackground: starts");
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        /*
        check if the string passed is null.
        if null, return null and set Download status - FAILED_OR_EMPTY
         */
        if(s==null){
            mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
            return null;
        }
        /*
        if string passed is not null proceed with download
         */
        try{
            mDownloadStatus = DownloadStatus.PROCESSING;
            //make URL of string passed
            URL url = new URL(s);

            //open http connection to URL
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            //check for response code
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: response code was: "+response);
            if(response==200){
                 /*
            result variable of type StringBuilder will store data retrieve from bufferedReader
            bufferedReader will be used to read inputStream from connection
             */
                StringBuilder result = new StringBuilder();
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                //loop to read till EOF
                for(String line = bufferedReader.readLine();line!=null;line=bufferedReader.readLine()){
                    result.append(line).append("\n");
                }

                mDownloadStatus = DownloadStatus.OK;
                return result.toString();
            }
            mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
            return null;


        }catch (MalformedURLException e){
            Log.e(TAG, "doInBackground: MalformedUrlException caught "+e.getMessage());
        }catch (IOException e){
            Log.e(TAG, "doInBackground: IOException caught "+e.getMessage());
        }catch (SecurityException e){
            Log.e(TAG, "doInBackground: Security Exception | Need permission "+e.getMessage());
        }finally {
           if(connection!=null){
               connection.disconnect();
           }
           if(bufferedReader!=null) {
               try{
                   bufferedReader.close();
               }catch (IOException e){
                   Log.e(TAG, "doInBackground: IOException caught "+e.getMessage());
               }
           }
        }
        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        Log.d(TAG, "doInBackground: ends");
        return null;

    }
}
