package parliament;

import java.util.LinkedList;

/**
 * represents a speech given during a session of the Bundestag,
 * including any comments made during it
 * @author Oliwia Daszczynska
 */
public interface Speech {

    /**
     * get the ID of a speech
     * @return the ID of the speech
     * @author Sophie Kaiser
     */
    String getID();

    /**
     * get the speaker that gave a speech
     * @return the speaker that gave the speech
     * @author Sophie Kaiser
     */
    Speaker getSpeaker();

    /**
     * set the speaker that gave a speech
     * @param speaker the speaker that gave the speech
     * @author Sophie Kaiser
     */
    void setSpeaker(Speaker speaker);

    /**
     * get the agenda item a speech belongs to
     * @return the agenda item the speech belongs to
     * @author Sophie Kaiser
     */
    AgendaItem getAgendaItem();

    /**
     * get the session during which a speech was given
     * @return the session during which the speech was given
     * @author Sophie Kaiser
     */
    Session getSession();

    /**
     * get the text contents of a speech
     * @return the text contents of the speech
     * @author Sophie Kaiser
     */
    LinkedList<Content> getContents();

    /**
     * add a piece of text content to a speech
     * @param content piece of text content to be added to the speech
     * @author Sophie Kaiser
     */
    void addContent(Content content);

    /**
     * get the video recording of a speech
     * @return the video recording of the speech
     * @author Sophie Kaiser
     */
    Video getVideo();

    /**
     * set the video recording of a speech
     * @param video the video recording of the speech
     * @author Sophie Kaiser
     */
    void setVideo(Video video);
}
