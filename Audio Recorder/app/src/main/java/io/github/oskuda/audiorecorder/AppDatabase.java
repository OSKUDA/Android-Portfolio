package io.github.oskuda.audiorecorder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AppDatabase extends SQLiteOpenHelper {

    private static final String TAG = "AppDatabase";
    public static final String DATABASE_NAME = "AudioDetails.db";
    public static final int DATABASE_VERSION = 1;

    // implement AppDatabase as a singleton
    private static AppDatabase instance = null;


    private AppDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "AppDatabase: constructor");
    }

    static AppDatabase getInstance(Context context){
        if(instance==null){
            Log.d(TAG, "getInstance: creating new instance");
            instance = new AppDatabase(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate: starts");
        addAudioDetailsTable(sqLiteDatabase);
        Log.d(TAG, "onCreate: ends");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: starts");
        switch (oldVersion){
            case 1:
                break;
            default:
                throw new IllegalStateException("onUpgrade() with unknown newVersion: "+newVersion);
        }
        Log.d(TAG, "onUpgrade: ends");
    }

    private void addAudioDetailsTable(SQLiteDatabase sqLiteDatabase){

        /*
        CREATE TABLE AudioDetails (_id INTEGER PRIMARY KEY NOT NULL,
                                    AudioTitle TEXT NOT NULL, AudioDate INTEGER,
                                    AudioDescription TEXT,
                                    AudioFileDuration TEXT,
                                    AudioFileId INTEGER NOT NULL);
         */
        String sSQL;    // use a string variable to facilitate logging
        sSQL = "CREATE TABLE "+ AudioContract.TABLE_NAME + " ("
                + AudioContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
                + AudioContract.Columns.AUDIO_TITLE + " TEXT NOT NULL, "
                + AudioContract.Columns.AUDIO_DATE + " INTEGER, "
                + AudioContract.Columns.AUDIO_DESCRIPTION + " TEXT, "
                + AudioContract.Columns.AUDIO_FILE_DURATION + " TEXT,"
                + AudioContract.Columns.AUDIO_FILE_NAME + " INTEGER NOT NULL);";
        Log.d(TAG, "addAudioDetailsTable: sSQL is "+sSQL);
        sqLiteDatabase.execSQL(sSQL);
    }

}
