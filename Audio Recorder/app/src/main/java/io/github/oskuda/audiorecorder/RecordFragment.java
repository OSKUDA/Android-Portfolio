package io.github.oskuda.audiorecorder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.Calendar;

public class RecordFragment extends Fragment {
    private static final String TAG = "RecordFragment";

    private static boolean RECORD_STATE = false;
    private static int PROGRESS_COUNT;
    private static String CHRONOMETER_VALUE;
    private static long TIME_WHEN_STOPPED;
    public static final String RECORD_STATE_KEY = "recordKey";
    public static final String PROGRESS_COUNT_KEY = "progressKey";
    public static final String CHRONOMETER_VALUE_KEY = "chronometerKey";
    public static final String TIME_WHEN_STOPPED_KEY = "timeStopKey";

    public static final String FILE_NAME_KEY = "fileName";

    ProgressBar mProgressBar;
    FloatingActionButton fabRecord;
    Chronometer chronometer;
    TextView txtRecordStatus;

    private MediaRecorder mMediaRecorder;
    private static long fileName;

    public RecordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.d(TAG, "onCreate: ends");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: called");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: stars");
        super.onViewCreated(view, savedInstanceState);

        mProgressBar = view.findViewById(R.id.progressBar);
        fabRecord = view.findViewById(R.id.fab_record);
        chronometer = view.findViewById(R.id.chronometer);
        txtRecordStatus = view.findViewById(R.id.txt_record_status);

        setListeners();

        // restore the state of fragment
        if (savedInstanceState != null) {
            RECORD_STATE = savedInstanceState.getBoolean(RECORD_STATE_KEY, false);
            PROGRESS_COUNT = savedInstanceState.getInt(PROGRESS_COUNT_KEY, -1);
            CHRONOMETER_VALUE = savedInstanceState.getString(CHRONOMETER_VALUE_KEY, null);
            TIME_WHEN_STOPPED = savedInstanceState.getLong(TIME_WHEN_STOPPED_KEY, -1);

            // apply appropriate icons
            // also update record status message
            if (RECORD_STATE) {
                fabRecord.setImageResource(R.drawable.ic_stop);
                txtRecordStatus.setText(R.string.fab_text_record_stop);
            } else {
                fabRecord.setImageResource(R.drawable.ic_mic);
                txtRecordStatus.setText(R.string.fab_text_record_start);
            }


            // restore the progress bar state
            if (PROGRESS_COUNT != -1) {
                mProgressBar.setProgress(PROGRESS_COUNT);
            }

            // restore the chronometer state
            if (CHRONOMETER_VALUE != null) {
                chronometer.setText(CHRONOMETER_VALUE);
            }

            if (TIME_WHEN_STOPPED != -1) {
                // restart the chronometer
                chronometer.setBase(SystemClock.elapsedRealtime() + TIME_WHEN_STOPPED);
                chronometer.start();
            }

        }


        Log.d(TAG, "onViewCreated: ends");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: starts");
        super.onSaveInstanceState(outState);
        // save data on orientation change or any other interrupt on our fragment lifecycle

        PROGRESS_COUNT = mProgressBar.getProgress();
        outState.putInt(PROGRESS_COUNT_KEY, PROGRESS_COUNT);

        CHRONOMETER_VALUE = chronometer.getText().toString();
        outState.putString(CHRONOMETER_VALUE_KEY, CHRONOMETER_VALUE);

        outState.putBoolean(RECORD_STATE_KEY, RECORD_STATE);

        if (RECORD_STATE) {
            TIME_WHEN_STOPPED = chronometer.getBase() - SystemClock.elapsedRealtime();
            chronometer.stop();
        } else {
            TIME_WHEN_STOPPED = -1;
        }

        outState.putLong(TIME_WHEN_STOPPED_KEY, TIME_WHEN_STOPPED);

        Log.d(TAG, "onSaveInstanceState: ends");
    }

    private void setListeners() {
        // implement onChronometerTickListener for progress bar
        chronometer.setOnChronometerTickListener(chronometer -> {
            // this method is called after every tick in chronometer
            // it will update our progress bar accordingly
            int second = Integer.parseInt(chronometer.getText().toString()
                    .replaceAll("[^0-9]", "")
                    .substring(2));
            mProgressBar.setProgress(second);
        });

        // onClick event handle for floating action button
        fabRecord.setOnClickListener(v -> {
            Log.d(TAG, "onClick: called");
            if (RECORD_STATE) {
                // stop the recording
                // change the icon of fab from 'stop' to 'mic'
                fabRecord.setImageResource(R.drawable.ic_mic);

                // change the guide text
                txtRecordStatus.setText(R.string.fab_text_record_start);

                // stop the chronometer
                chronometer.stop();
                TIME_WHEN_STOPPED = 0;

                // reset the chronometer text
                chronometer.setText(R.string.txt_audio_current_time_default);

                // reset the progress bar
                mProgressBar.setProgress(0);

                // stop recording
                if (mMediaRecorder != null) {
                    stopRecording();
                }

                // flip RECORD_STATE
                RECORD_STATE = !RECORD_STATE;


                // create appDialog
                FileDetailDialog dialog = new FileDetailDialog();
                // pass FileId
                Bundle arguments = new Bundle();
                arguments.putLong(FILE_NAME_KEY, fileName);
                dialog.setArguments(arguments);
                dialog.show(getActivity().getSupportFragmentManager(), null);
            } else {
                // check if the app has record permission
                int hasRecordAudioPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO);
                // if permission is not granted, request the user
                if (hasRecordAudioPermission != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "The app requires record permission to function", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onCreate: Requesting permission");
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                } else {
                    // change the icon of fab from 'mic' to 'stop'
                    fabRecord.setImageResource(R.drawable.ic_stop);
                    // change the guide text
                    txtRecordStatus.setText(R.string.fab_text_record_stop);

                    // start the chronometer
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();

                    // start recording
                    if (prepareRecorder() && mMediaRecorder != null) {
                        startRecording();
                    }

                    // flip RECORD_STATE
                    RECORD_STATE = !RECORD_STATE;
                }
            }
        });
    }

    private boolean prepareRecorder() {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        // configure file path
        fileName = Calendar.getInstance().getTimeInMillis();
        String filePath = getActivity().getExternalCacheDir().getAbsolutePath() + "/" + fileName + ".3gp";

        mMediaRecorder.setOutputFile(filePath);
        try {
            mMediaRecorder.prepare();
            return true;
        } catch (IOException e) {
            Log.d(TAG, "prepareRecorder: prepare() exception " + e.getMessage());
            return false;
        }
    }

    private void startRecording() {
        Log.d(TAG, "startRecording: called");
        mMediaRecorder.start();
    }

    private void stopRecording() {
        Log.d(TAG, "stopRecording: called");
        mMediaRecorder.stop();          // stop recording
        mMediaRecorder.reset();         // set state to idle
        mMediaRecorder.release();       // release resource back to the system
        mMediaRecorder = null;
    }

}