package parliament.impl;

import parliament.Session;
import parliament.Video;

/**
 * implementation of the Video interface,
 * represents a video recording of a speech given during a session
 * of the Bundestag
 * @author Sophie Kaiser
 */
public class Video_Impl implements Video {

    /**
     * ID of the video
     */
    private final String id;

    /**
     * the session the video is a recording of
     */
    private final Session session;

    /**
     * URL of the video
     */
    private String url;

    /**
     * package private constructor to ensure Video_Impl can only be
     * instantiated via ParliamentFactory_Impl,
     * video is added to its session
     * @param id the ID of the video
     * @param session the session the video is a recording of
     * @param url the URL of the video
     * @author Sophie Kaiser
     * @author Oliwia Daszczynska
     */
    Video_Impl(String id, Session session, String url){
        this.id = id;
        this.session = session;
        this.url = url;

        // add video to its session
        this.session.setVideo(this);
    }

    /**
     * get video ID
     *
     * @return the ID of the video
     * @author Sophie Kaiser
     */
    @Override
    public String getID() {
        return this.id;
    }

    /**
     * get the session a video is a recording of
     *
     * @return the session the video is a recording of
     * @author Sophie Kaiser
     */
    @Override
    public Session getSession() {
        return this.session;
    }

    /**
     * get the URL of a video
     *
     * @return URL of the video
     * @author Sophie Kaiser
     */
    @Override
    public String getURL() {
        return this.url;
    }

    /**
     * set the URL of a video
     *
     * @param url URL of the video
     * @author Sophie Kaiser
     */
    @Override
    public void setURL(String url) {
        this.url = url;
    }
}
