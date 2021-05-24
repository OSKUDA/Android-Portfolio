package io.github.oskuda.audiorecorder;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity implements FileDetailDialog.FileDetailDialogEvents, 
        CursorRecyclerViewAdapter.OnDeleteFileClicked, FilePlayerDialog.OnFilePlayerDialogEvents{
    private static final String TAG = "MainActivity";

    public static final int SEEK_FORWARD_OR_BACKWARD_DURATION = 5;
    // Requesting permission to RECORD_AUDIO
    public static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;
    private final String[] permissions = {Manifest.permission.RECORD_AUDIO};

    private TabLayout mTabLayout;
    private ViewPager2 mViewPager2;

    private PlaybackManager mPlaybackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize app database
        AppDatabase.getInstance(this);

        // check if the app has record permission
        int hasRecordAudioPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        Log.d(TAG, "onCreate: checkSelfPermission "+ hasRecordAudioPermission);

        // if permission is not granted, request the user
        if(hasRecordAudioPermission !=PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "onCreate: Requesting permission");
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        }

        // tab layout configurations
        // tabLayout is used with viewPager2
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager2 = findViewById(R.id.view_pager_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentAdapter fragmentAdapter = new FragmentAdapter(fragmentManager, getLifecycle());
        mViewPager2.setAdapter(fragmentAdapter);


        // respond to taps on tabs
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        // respond to swipes in pagerView and change selected tab on tabLayout
        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                mTabLayout.selectTab(mTabLayout.getTabAt(position));
            }
        });


        Log.d(TAG, "onCreate: ends");
    }

    @Override
    public void onPositiveFileDetailSaveDialogResult(Bundle args) {
        Log.d(TAG, "onPositiveDialogResult: called");
        // retrieve the data passed form FileDetailDialog
        if(args!=null){
            long fileId = args.getLong(FileDetailDialog.FILE_ID_KEY,-1);
            String fileName = args.getString(FileDetailDialog.FILE_NAME_KEY, null);
            String fileDescription = args.getString(FileDetailDialog.FILE_DESCRIPTION_KEY, null);

            // get file duration
//            MediaPlayer mediaPlayer = MediaPlayer.create(this,Uri.parse(getExternalCacheDir().getAbsolutePath()+"/"+fileId+".3gp"));
//            long duration = 0;
//            if(mediaPlayer==null){
//                Log.d(TAG, "onPositiveFileDetailSaveDialogResult: media player creation failed");
//            }else{
//                duration = mediaPlayer.getDuration();
//            }
            Uri uri = Uri.parse(getExternalCacheDir().getAbsolutePath()+"/"+fileId+".3gp");
            Log.d(TAG, "onPositiveFileDetailSaveDialogResult: uri "+uri.toString());
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(uri.toString());
            String fileDuration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            mediaMetadataRetriever.release();


            if(fileId!=-1 && fileName!=null && fileDescription!=null){
                // insert to our database
                Log.d(TAG, "onPositiveSaveDialogResult: inserting to database" +
                        " fileName "+ fileName +"\n" +
                        " fileDescription "+ fileDescription + "\n" +
                        " fileId " + fileId +"\n" +
                        " fileDate "+ fileId+"\n"+
                        " fileDuration "+ fileDuration);
                SQLiteDatabase sqLiteDatabase = AppDatabase.getInstance(this).getWritableDatabase();


                ContentValues contentValues = new ContentValues();
                contentValues.put(AudioContract.Columns.AUDIO_TITLE, fileName);
                contentValues.put(AudioContract.Columns.AUDIO_DESCRIPTION, fileDescription);
                contentValues.put(AudioContract.Columns.AUDIO_DATE, fileId);
                contentValues.put(AudioContract.Columns.AUDIO_FILE_NAME, fileId);
                contentValues.put(AudioContract.Columns.AUDIO_FILE_DURATION,fileDuration);
                long result = sqLiteDatabase.insert(AudioContract.TABLE_NAME, null, contentValues);

                // update the recycler view by swapping the cursor
                if(SavedRecordingsFragment.mAdapter!=null){
                    SavedRecordingsFragment.mAdapter.swapCursor(sqLiteDatabase.query(AudioContract.TABLE_NAME, null,null,null,null,null,null));
                }
                Log.d(TAG, "onPositiveSaveDialogResult: result "+result);
            }
        }

    }


    @Override
    public void onFileDetailDialogCancelled() {
        Log.d(TAG, "onDialogCancelled: called");
        // do nothing
    }

    @Override
    public void onDeleteFile(AudioFile audioFile) {
        // never called. here to satisfy interface
    }

    @Override
    public void onPlayFile(AudioFile audioFile) {
        // never called. here to satisfy interface
    }

    @Override
    public void onPrepareMediaPlayer(String fileId, ImageView imageViewButtonPlayToggle, ProgressBar progressBar, TextView textViewCurrentTime) {
        Log.d(TAG, "onPrepareMediaPlayer: called");
        try{
            mPlaybackManager = new PlaybackManager(this,
                    Uri.parse(getExternalCacheDir().getAbsolutePath()+"/"+fileId+".3gp"),
                    Long.parseLong(fileId),
                    imageViewButtonPlayToggle,
                    progressBar,
                    textViewCurrentTime);
        }catch (FileNotFoundException e){
            Log.e(TAG, "onPrepareMediaPlayer: File not found! "+e.getMessage());
        }
    }

    @Override
    public void onPlayClicked() {
        Log.d(TAG, "onPlayClicked: called");
        mPlaybackManager.play();
    }

    @Override
    public void onPauseClicked() {
        mPlaybackManager.pause();
    }

    @Override
    public void onForwardClicked() {
        Log.d(TAG, "onForwardClicked: called");
        if(mPlaybackManager!=null){
            mPlaybackManager.seekForward(SEEK_FORWARD_OR_BACKWARD_DURATION);
        }
    }

    @Override
    public void onRewindClicked() {
        Log.d(TAG, "onRewindClicked: called");
        mPlaybackManager.seekBackward(SEEK_FORWARD_OR_BACKWARD_DURATION);
    }

    @Override
    public void onDialogCancelled() {
        mPlaybackManager.stop();
        mPlaybackManager.releaseResource();
    }
}