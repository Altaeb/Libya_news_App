package abdelfattah.libyanews.object;

public class UserPreference {

    private final boolean mAuthorPreference;
    private final boolean mThumbnailPreference;
    private final String mArticleNumberPreference;
    private final String mOrderByPreference;

    /**
     * Custom object to store user preferences from Settings
     *
     * @param authorPreference        author name to display in list item : true or false
     * @param thumbnailPreference     news article thumbnail to include in list item : true or false
     * @param articleNumberPreference number of news article per page
     * @param orderByPreference       sort news article in form of newest or relevance
     */
    public UserPreference(boolean authorPreference
            , boolean thumbnailPreference
            , String articleNumberPreference
            , String orderByPreference) {
        mAuthorPreference = authorPreference;
        mThumbnailPreference = thumbnailPreference;
        mArticleNumberPreference = articleNumberPreference;
        mOrderByPreference = orderByPreference;
    }

    /**
     * @return news article author preference
     */
    public boolean getAuthorPreference() {
        return mAuthorPreference;
    }

    /**
     * @return news article thumbnail preference
     */
    public boolean getThumbnailPreference() {
        return mThumbnailPreference;
    }

    /**
     * @return news article per page
     */
    public String getArticleNumberPreference() {
        return mArticleNumberPreference;
    }

    /**
     * @return sort news by newest or relevance
     */
    public String getOrderByPreference() {
        return mOrderByPreference;
    }
}