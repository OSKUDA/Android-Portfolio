package io.github.oskuda.audiorecorder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;

public class CursorRecyclerViewAdapter extends RecyclerView.Adapter<CursorRecyclerViewAdapter.ViewHolder> {

    private Cursor mCursor;
    private final Context mContext;
    private final OnDeleteFileClicked mCallback;

    interface OnDeleteFileClicked{
        void onDeleteFile(AudioFile audioFile);
        void onPlayFile(AudioFile audioFile);
    }
    public CursorRecyclerViewAdapter(Cursor cursor, Activity context, OnDeleteFileClicked callback) {
        mCursor = cursor;
        mContext = context;
        mCallback = callback;
    }

    @NonNull
    @Override
    public CursorRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_list_item,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CursorRecyclerViewAdapter.ViewHolder holder, int position) {

        if(mCursor == null || mCursor.getCount()==0){
            holder.fileName.setText(R.string.txt_instruction_header);
            holder.fileDescription.setText(R.string.txt_instruction_description);
            holder.fileRecordDate.setVisibility(View.GONE);
            holder.playButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }else{
            if(!mCursor.moveToPosition(position)){
                throw new IllegalStateException("Couldn't move cursor to position "+ position);
            }

            final AudioFile audioFile = new AudioFile(mCursor.getLong(mCursor.getColumnIndex(AudioContract.Columns._ID)),
                    mCursor.getString(mCursor.getColumnIndex(AudioContract.Columns.AUDIO_TITLE)),
                    mCursor.getString(mCursor.getColumnIndex(AudioContract.Columns.AUDIO_DESCRIPTION)),
                    mCursor.getString(mCursor.getColumnIndex(AudioContract.Columns.AUDIO_DATE)),
                    mCursor.getString(mCursor.getColumnIndex(AudioContract.Columns.AUDIO_FILE_NAME)),
                    mCursor.getString(mCursor.getColumnIndex(AudioContract.Columns.AUDIO_FILE_DURATION)));

            holder.fileName.setText(audioFile.getFileName());
            holder.fileDescription.setText(audioFile.getFileDescription());

            // make buttons visible again
            holder.playButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.fileRecordDate.setVisibility(View.VISIBLE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(audioFile.getRecordDate()));
            holder.fileRecordDate.setText(calendar.get(Calendar.YEAR)+"/"+calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.DAY_OF_MONTH));

            // set onClick listeners for buttons
            holder.playButton.setOnClickListener(v -> mCallback.onPlayFile(audioFile));
            holder.deleteButton.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Are you sure you want to delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, which) -> mCallback.onDeleteFile(audioFile))
                        .setNegativeButton("No", (dialog, which) -> {
                        });
                AlertDialog alert = builder.create();
                alert.show();
            });
        }

    }

    @Override
    public int getItemCount() {
        if(mCursor==null || mCursor.getCount()==0){
            return 1;
        }else{
            return mCursor.getCount();
        }
    }

    void swapCursor(Cursor newCursor){
        if(newCursor == mCursor){
            return;
        }
        mCursor = newCursor;
        if(newCursor != null){
            // notify the observers about the new cursor.
            notifyDataSetChanged();
        }else{
            // notify the observers about the lack of a data set
            notifyItemRangeRemoved(0, getItemCount());
        }
    }



    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView fileName;
        TextView fileDescription;
        TextView fileRecordDate;
        ImageView playButton;
        ImageView deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.fileName = itemView.findViewById(R.id.txt_view_file_name);
            this.fileDescription = itemView.findViewById(R.id.txt_view_record_description);
            this.fileRecordDate = itemView.findViewById(R.id.txt_view_record_date);
            this.playButton = itemView.findViewById(R.id.image_btn_record_play);
            this.deleteButton = itemView.findViewById(R.id.image_btn_record_delete);
        }
    }
}
