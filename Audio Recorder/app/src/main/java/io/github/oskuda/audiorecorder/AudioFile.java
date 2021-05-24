package io.github.oskuda.audiorecorder;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class AudioFile implements Serializable {
    public static final long serialVersionUID = 20161110L;

    private final long m_id;
    private final String mFileName;
    private final String mFileDescription;
    private final String mRecordDate;
    private final String mFile_id;
    private final String mFileDuration;

    public AudioFile(long id, String fileName, String fileDescription, String recordDate, String file_id, String fileDuration) {
        m_id = id;
        mFileName = fileName;
        mFileDescription = fileDescription;
        mRecordDate = recordDate;
        mFile_id = file_id;
        mFileDuration = fileDuration;
    }

    public String getFileName() {
        return mFileName;
    }

    public String getFileDescription() {
        return mFileDescription;
    }

    public String getRecordDate() {
        return mRecordDate;
    }

    public String getFile_id() {
        return mFile_id;
    }

    public String getFileDuration() {
        return mFileDuration;
    }

    @NonNull
    @Override
    public String toString() {
        return "AudioFile{" +
                "m_id=" + m_id +
                ", mFileName='" + mFileName + '\'' +
                ", mFileDescription='" + mFileDescription + '\'' +
                ", mRecordDate='" + mRecordDate + '\'' +
                ", mFile_id=" + mFile_id +
                '}';
    }
}
