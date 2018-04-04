package tech.techbug.booksearch;

/**
 * Created by noblegas on 1/4/18.
 */

public class BookDetails {
    private String mTitle, mAuthor;
    private String mDescription;
    private String mPageCount = "Not Available";
    private String mThumbnailUrl;
    private String mInfoUrl;

    public BookDetails(String mTitle, String mAuthor, String mDescription,
                       String mPageCount, String mThumbnailUrl, String mInfoUrl) {
        this.mTitle = mTitle;
        this.mAuthor = mAuthor;
        this.mDescription = mDescription;
        this.mPageCount = mPageCount;
        this.mThumbnailUrl = mThumbnailUrl;
        this.mInfoUrl = mInfoUrl;
    }

    public BookDetails(String mTitle, String mAuthor, String mPageCount,
                       String mThumbnailUrl, String mInfoUrl) {
        this.mTitle = mTitle;
        this.mAuthor = mAuthor;
        this.mPageCount = mPageCount;
        this.mThumbnailUrl = mThumbnailUrl;
        this.mInfoUrl = mInfoUrl;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmPageCount() {
        return mPageCount;
    }

    public void setmPageCount(String mPageCount) {
        this.mPageCount = mPageCount;
    }

    public String getmThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setmThumbnailUrl(String mThumbnailUrl) {
        this.mThumbnailUrl = mThumbnailUrl;
    }

    public String getmInfoUrl() {
        return mInfoUrl;
    }

    public void setmInfoUrl(String mInfoUrl) {
        this.mInfoUrl = mInfoUrl;
    }
}
