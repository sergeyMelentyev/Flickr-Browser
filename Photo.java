package com.example.atmen.flickrbrowser;

class Photo {
    private String mTitle;
    private String mAuthor;
    private String mAuthorId;
    private String mLink;
    private String mTags;
    private String mImage;

    public Photo(String mTitle, String mAuthor, String mAuthorId, String mLink, String mTags, String mImage) {
        this.mTitle = mTitle;
        this.mAuthor = mAuthor;
        this.mAuthorId = mAuthorId;
        this.mLink = mLink;
        this.mTags = mTags;
        this.mImage = mImage;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmAuthorId() {
        return mAuthorId;
    }

    public String getmImage() {
        return mImage;
    }

    public String getmLink() {
        return mLink;
    }

    public String getmTags() {
        return mTags;
    }

    public String getmTitle() {
        return mTitle;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "mAuthor='" + mAuthor + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mAuthorId='" + mAuthorId + '\'' +
                ", mLink='" + mLink + '\'' +
                ", mTags='" + mTags + '\'' +
                ", mImage='" + mImage + '\'' +
                '}';
    }
}
