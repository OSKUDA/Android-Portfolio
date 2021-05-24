package io.github.oskuda.audiorecorder;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.FileNotFoundException;

public class PlaybackManager {
    private static final String TAG = "PlaybackAudio";
    Context mContext;

    long fileId;
    MediaPlayer mMediaPlayer;
    ProgressBar mProgressBar;
    TextView mTextViewCurrentTime;
    Handler seekHandler;

    public PlaybackManager(Context context, Uri filePath, long fileId, ImageView imageViewButtonPlayToggle, ProgressBar progressBar, TextView textViewCurrentTime) throws FileNotFoundException {
        Log.d(TAG, "PlaybackAudio: constructor called");
        mContext = context;
        this.fileId = fileId;
        Log.d(TAG, "PlaybackManager: context "+mContext.toString());

        mMediaPlayer = MediaPlayer.create(mContext, filePath);
        if(mMediaPlayer == null){
            throw new FileNotFoundException("File "+fileId+" was not found. Error!");
        }

        mProgressBar = progressBar;
        mTextViewCurrentTime = textViewCurrentTime;
        //noinspection deprecation
        seekHandler = new Handler();
        mMediaPlayer.setOnCompletionListener(mp -> {
            imageViewButtonPlayToggle.setImageResource(R.drawable.ic_play);
            // change the state of isPlaying to false
            FilePlayerDialog.isPlaying = false;
            mTextViewCurrentTime.setText(R.string.txt_audio_current_time_default);
        });
    }




    // to play the audio
    public void play(){
        Log.d(TAG, "startAudio: called");
        if(mMediaPlayer!=null){

            mMediaPlayer.start();
            mProgressBar.setProgress(mMediaPlayer.getCurrentPosition());
            mProgressBar.setMax(mMediaPlayer.getDuration());

            // updating progress bar
            seekHandler.postDelayed(updateSeekBar, 5);
        }
    }

    // to pause the audio
    public void pause(){
        Log.d(TAG, "pause: called");
        if(mMediaPlayer!=null){
            mMediaPlayer.pause();
            seekHandler.removeCallbacksAndMessages(null);
        }
    }

    // to stop the audio
    public void stop(){
        Log.d(TAG, "stop: called");
        if(mMediaPlayer!=null){
            mMediaPlayer.stop();
            FilePlayerDialog.isPlaying = false;
            seekHandler.removeCallbacksAndMessages(null);
        }
    }

    // to seek the audio forward or backward
    public void seekForward(int duration){
        if(mMediaPlayer!=null){
            if((mMediaPlayer.getCurrentPosition()+(duration*1000))>=mMediaPlayer.getDuration()){
                mMediaPlayer.seekTo(mMediaPlayer.getDuration()-1);
            }else{
                mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() + (duration*1000));
            }
        }
    }
    public void seekBackward(int duration){
        if(mMediaPlayer!=null){
            mMediaPlayer.seekTo(Math.max((mMediaPlayer.getCurrentPosition() - (duration * 1000)), 0));
        }
    }

    // release resources
    public void releaseResource(){
        Log.d(TAG, "releaseResource: called");
        if(mMediaPlayer!=null){
            mMediaPlayer.release();
        }
    }

    // Background Runnable thread
    private final Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            long currentDuration = mMediaPlayer.getCurrentPosition();

            // display time completed playing
            // elapsed.setText(""+ milliSecondsToTimer(currentDuration));
            // convert millisecond data to minutes and seconds
            long minutes = (currentDuration / 1000) / 60;
            long seconds = (currentDuration / 1000) % 60;
            mTextViewCurrentTime.setText(String.format("%02d:%02d", minutes, seconds));

            // update progress bar
            mProgressBar.setProgress((int)currentDuration);

            // call this thread again after 15 milliseconds => ~1000/60fps
            seekHandler.postDelayed(this,5);
        }
    };





}
