package io.github.oskuda.audiorecorder;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.IOError;


public class SavedRecordingsFragment extends Fragment implements CursorRecyclerViewAdapter.OnDeleteFileClicked{
    private static final String TAG = "SavedRecordingsFragment";

    public static CursorRecyclerViewAdapter mAdapter;
    private Context mContext;
    public SavedRecordingsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // when orientation changes, the parent activity recreates.
        // update the context every time the orientation changes
        mContext = getActivity().getApplicationContext();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_recordings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if(mAdapter==null){
            mAdapter = new CursorRecyclerViewAdapter(getAllItem(), getActivity(), this);
        }

        recyclerView.setAdapter(mAdapter);
    }

    private Cursor getAllItem(){
        SQLiteDatabase db = AppDatabase.getInstance(getActivity()).getWritableDatabase();
        return db.query(AudioContract.TABLE_NAME, null,null,null,null,null,null);
    }

    @Override
    public void onDeleteFile(AudioFile audioFile) {
        // remove the file from storage
        boolean deleteStatus = false;
        try {
            String filePath = mContext.getExternalCacheDir().getAbsolutePath() + "/" + audioFile.getFile_id() + ".3gp";
            File file = new File(filePath);
            deleteStatus = file.delete();
        } catch (IOError e) {
            Log.d(TAG, "onClick: IOError " + e.getMessage());
        }
        if(deleteStatus){
            Log.d(TAG, "onDeleteFile: successful deletion in storage");
            // remove from database
            SQLiteDatabase sqLiteDatabase = AppDatabase.getInstance(getActivity()).getWritableDatabase();
            int temp = sqLiteDatabase.delete(AudioContract.TABLE_NAME,AudioContract.Columns.AUDIO_FILE_NAME+" = "+audioFile.getFile_id(),null);
            // update cursor`
            // update the recycler view by swapping the cursor
            if(SavedRecordingsFragment.mAdapter!=null){
                SavedRecordingsFragment.mAdapter.swapCursor(sqLiteDatabase.query(AudioContract.TABLE_NAME, null,null,null,null,null,null));
            }
            Log.d(TAG, "onDeleteFile: lines removed "+temp);
        }else{
            Log.d(TAG, "onDeleteFile: FAILED");
        }
    }

    @Override
    public void onPlayFile(AudioFile audioFile) {
        Log.d(TAG, "onPlayFile: called");
        Bundle arguments = new Bundle();
        arguments.putString(FilePlayerDialog.AUDIO_FILE_TITLE_KEY,audioFile.getFileName());
        arguments.putString(FilePlayerDialog.AUDIO_FILE_ID_KEY,audioFile.getFile_id());
        arguments.putString(FilePlayerDialog.AUDIO_FILE_DURATION_KEY,audioFile.getFileDuration());
        FilePlayerDialog dialog = new FilePlayerDialog();
        dialog.setArguments(arguments);
        dialog.show(getFragmentManager(),null);
    }
}