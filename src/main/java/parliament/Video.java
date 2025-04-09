package parliament;

/**
 * represents a video recording of a speech given during a session
 * of the Bundestag
 * @author Sophie Kaiser
 */
public interface Video {

    /**
     * get video ID
     * @return the ID of the video
     * @author Sophie Kaiser
     */
    String getID();

    /**
     * get the session a video is a recording of
     * @return the session the video is a recording of
     * @author Sophie Kaiser
     */
    Session getSession();

    /**
     * get the URL of a video
     * @return URL of the video
     * @author Sophie Kaiser
     */
    String getURL();

    /**
     * set the URL of a video
     * @param url URL of the video
     * @author Sophie Kaiser
     */
    void setURL(String url);
}
