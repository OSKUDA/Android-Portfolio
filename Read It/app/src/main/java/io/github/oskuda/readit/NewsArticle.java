package io.github.oskuda.readit;

import java.io.Serializable;

public class NewsArticle implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String mId;
    private final String mPublicationDate;
    private final String mHeadline;
    private final String mImageUri;

    public NewsArticle(String id, String publicationDate, String headline, String imageUri) {
        mId = id;
        mPublicationDate = publicationDate;
        mHeadline = headline;
        mImageUri = imageUri;
    }

    public String getId() {
        return mId;
    }

    public String getPublicationDate() {
        return mPublicationDate;
    }

    public String getHeadline() {
        return mHeadline;
    }

    public String getImageUri() {
        return mImageUri;
    }

    public String dateFormatter() {
        return this.getPublicationDate().replaceAll("[TZ]", " ");
    }

    @Override
    public String toString() {
        return "NewsArticle{" +
                "mId='" + mId + '\'' +
                ", mPublicationDate='" + mPublicationDate + '\'' +
                ", mHeadline='" + mHeadline + '\'' +
                ", mImageUri='" + mImageUri + '\'' +
                '}';
    }
}
