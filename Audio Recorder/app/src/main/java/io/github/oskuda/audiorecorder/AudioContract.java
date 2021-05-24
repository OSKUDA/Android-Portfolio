package io.github.oskuda.audiorecorder;

import android.provider.BaseColumns;

public class AudioContract {

    static final String TABLE_NAME = "AudioDetails";

    //columns
    public static class Columns{
        public static final String _ID = BaseColumns._ID;
        public static final String AUDIO_TITLE = "AudioTitle";
        public static final String AUDIO_DATE = "AudioDate";
        public static final String AUDIO_DESCRIPTION = "AudioDescription";
        public static final String AUDIO_FILE_NAME = "AudioFileName";
        public static final String AUDIO_FILE_DURATION = "AudioFileDuration";

        private Columns(){
            // private constructor to prevent initialization
        }
    }

}
