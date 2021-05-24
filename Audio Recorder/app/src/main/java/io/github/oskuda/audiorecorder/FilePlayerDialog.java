package io.github.oskuda.audiorecorder;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.Objects;

public class FilePlayerDialog extends DialogFragment {
    private static final String TAG = "FilePlayerDialog";

    public static final String AUDIO_FILE_ID_KEY = "audioFileIdKey";
    public static final String AUDIO_FILE_TITLE_KEY = "audioFileTitleKey";
    public static final String AUDIO_FILE_DURATION_KEY = "audioFileDuration";

    private OnFilePlayerDialogEvents mOnFilePlayerDialogEventsCallback;

    /**
     * The dialogue's callback interface to notify of user selected results (play, pause, stop, etc.)
     */
    interface OnFilePlayerDialogEvents {
        void onPrepareMediaPlayer(String fileId, ImageView imageViewButtonPlayToggle, ProgressBar progressBar, TextView textViewCurrentTime);

        void onPlayClicked();

        void onPauseClicked();

        void onForwardClicked();

        void onRewindClicked();

        void onDialogCancelled();
    }

    // view variables
    TextView mTextViewFileTitle;
    TextView mTextViewCurrentTime;
    TextView mTextViewTotalDuration;
    ProgressBar mProgressBar;
    ImageView mImageViewButtonPlayToggle;
    ImageView mImageViewButtonForward;
    ImageView mImageViewButtonRewind;

    private String fileName;
    private String fileId;
    private String fileDuration;

    public static boolean isPlaying = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // check if calling fragment or activity has implemented DialogEvents callback methods
        if (!(context instanceof OnFilePlayerDialogEvents)) {
            throw new ClassCastException(context.toString() + " must implement FilePlayerDialog.FilePlayerDialogEvents interface");
        }
        mOnFilePlayerDialogEventsCallback = (OnFilePlayerDialogEvents) context;
    }


    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: called");
        super.onDetach();
        // stop the player if playing and release the resources
        mOnFilePlayerDialogEventsCallback.onDialogCancelled();
        mOnFilePlayerDialogEventsCallback = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog: starts");

        View view = Objects.requireNonNull(getActivity().getLayoutInflater().inflate(R.layout.dialog_audio_player, new LinearLayout(getActivity()), false));

        // hook the views
        mTextViewFileTitle = view.findViewById(R.id.txt_view_file_title);
        mTextViewCurrentTime = view.findViewById(R.id.txt_view_current_time);
        mTextViewTotalDuration = view.findViewById(R.id.txt_view_duration);
        mImageViewButtonPlayToggle = view.findViewById(R.id.image_button_player_play);
        mImageViewButtonForward = view.findViewById(R.id.image_button_player_forward);
        mImageViewButtonRewind = view.findViewById(R.id.image_button_player_rewind);
        mProgressBar = view.findViewById(R.id.progress_bar_audio_player);

        // get passed arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            fileId = arguments.getString(AUDIO_FILE_ID_KEY, "null");
            fileName = arguments.getString(AUDIO_FILE_TITLE_KEY, "null");
            fileDuration = arguments.getString(AUDIO_FILE_DURATION_KEY, "null");
        }

        mTextViewFileTitle.setText(fileName);
        mTextViewCurrentTime.setText(R.string.txt_audio_current_time_default);
        // convert millisecond data to minutes and seconds
        long milliseconds = Long.parseLong(fileDuration);
        long minutes = (milliseconds / 1000) / 60;
        long seconds = (milliseconds / 1000) % 60;
        mTextViewTotalDuration.setText(String.format("%02d:%02d", minutes, seconds));

        setListener();
        // initialize our media player from 0 sec
        mOnFilePlayerDialogEventsCallback.onPrepareMediaPlayer(fileId, mImageViewButtonPlayToggle, mProgressBar, mTextViewCurrentTime);


        // build dialog
        Dialog builder = new Dialog(getActivity());
        builder.setContentView(view);
        Log.d(TAG, "onCreateDialog: ends");
        return builder;
    }


    private void setListener() {
        mImageViewButtonPlayToggle.setOnClickListener(v -> {
            // change the play button according to the state
            isPlaying = !isPlaying;
            if (isPlaying) {
                mImageViewButtonPlayToggle.setImageResource(R.drawable.ic_pause);
                mOnFilePlayerDialogEventsCallback.onPlayClicked();
            } else {
                mImageViewButtonPlayToggle.setImageResource(R.drawable.ic_play);
                mOnFilePlayerDialogEventsCallback.onPauseClicked();
            }


        });
        mImageViewButtonForward.setOnClickListener(v -> mOnFilePlayerDialogEventsCallback.onForwardClicked());
        mImageViewButtonRewind.setOnClickListener(v -> mOnFilePlayerDialogEventsCallback.onRewindClicked());
    }
}
