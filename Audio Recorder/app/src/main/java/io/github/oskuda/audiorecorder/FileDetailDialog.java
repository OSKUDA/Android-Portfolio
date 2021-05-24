package io.github.oskuda.audiorecorder;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.io.IOError;
import java.util.Objects;

public class FileDetailDialog extends DialogFragment {
    private static final String TAG = "FileDetailDialog";

    public static final String FILE_ID_KEY = "fileId";
    public static final String FILE_NAME_KEY = "fileName";
    public static final String FILE_DESCRIPTION_KEY = "fileDescription";

    FileDetailDialogEvents mFileDetailDialogEventsCallback;

    /**
     * The dialogue's callback interface to notify of user selected results (deletion confirmed, etc.)
     */
    interface FileDetailDialogEvents {
        void onPositiveFileDetailSaveDialogResult(Bundle args);
        void onFileDetailDialogCancelled();
    }

    // view variables
    TextView mTextViewFileId;
    EditText mEditTextFileName;
    EditText mEditTextFileDescription;
    Button mButtonSave;
    Button mButtonDiscard;

    private long fileId;


    @Override
    public void onAttach(@NonNull Context context) {
        Log.d(TAG, "onAttach: called by " + context.toString());
        super.onAttach(context);

        // check if calling fragment or activity has implemented DialogEvents callback methods
        if (!(context instanceof FileDetailDialogEvents)) {
            throw new ClassCastException(context.toString() + " must implement AppDialog.DialogEvents interface");
        }
        mFileDetailDialogEventsCallback = (FileDetailDialogEvents) context;
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: called");
        super.onDetach();
        // reset the active callback interface, because we don't have an activity or fragment any longer
        mFileDetailDialogEventsCallback = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.dialog_file_details, new LinearLayout(getActivity()), false);

        // hook the views
        mTextViewFileId = view.findViewById(R.id.txt_file_id_show);
        mEditTextFileName = view.findViewById(R.id.txt_edit_file_name);
        mEditTextFileDescription = view.findViewById(R.id.txt_edt_file_description);
        mButtonSave = view.findViewById(R.id.btn_save_audio);
        mButtonDiscard = view.findViewById(R.id.btn_discard_audio);

        // get passed arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            fileId = arguments.getLong(RecordFragment.FILE_NAME_KEY, -1);
        }

        // display fileId
        mTextViewFileId.setText(String.valueOf(fileId));

        setButtonListener();


        // build dialog
        Dialog builder = new Dialog(getActivity());
        builder.setContentView(view);
        setCancelable(false);
        return builder;

    }

    private void setButtonListener() {
        mButtonSave.setOnClickListener(v -> {
            Log.d(TAG, "onClick: save called");
            // send user data to mainActivity to insert in the database
            if (validateInput()) {
                Bundle arguments = new Bundle();
                arguments.putLong(FILE_ID_KEY, fileId);
                arguments.putString(FILE_NAME_KEY, mEditTextFileName.getText().toString());
                arguments.putString(FILE_DESCRIPTION_KEY, mEditTextFileDescription.getText().toString());
                mFileDetailDialogEventsCallback.onPositiveFileDetailSaveDialogResult(arguments);
                // dismiss the dialog
                if (getDialog() != null) {
                    getDialog().dismiss();
                }
            } else {
                Toast.makeText(getActivity(), "Please provide all fields", Toast.LENGTH_SHORT).show();
            }

        });
        mButtonDiscard.setOnClickListener(v -> {
            Log.d(TAG, "onClick: discard called");
            boolean deleteStatus = false;
            // remove the file from storage
            try {
                String filePath = getActivity().getExternalCacheDir().getAbsolutePath() + "/" + fileId + ".3gp";
                File file = new File(filePath);
                deleteStatus = file.delete();
            } catch (IOError e) {
                Log.d(TAG, "onClick: IOError " + e.getMessage());
            }
            if (deleteStatus) {
                Toast.makeText(getActivity(), fileId + ": file was not saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), fileId + ": failed to remove the file. It is saved", Toast.LENGTH_SHORT).show();
            }

            // callback
            mFileDetailDialogEventsCallback.onFileDetailDialogCancelled();

            // dismiss the dialog
            if (getDialog() != null) {
                getDialog().dismiss();
            }
        });
    }

    public boolean validateInput() {
        return !mEditTextFileName.getText().toString().equals("") && !mEditTextFileDescription.getText().toString().equals("");
    }
}
