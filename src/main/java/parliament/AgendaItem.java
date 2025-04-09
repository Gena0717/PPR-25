package parliament;

import java.util.LinkedList;

/**
 * represents an item on the agenda of a session of the Bundestag
 * @author Sophie Kaiser
 */
public interface AgendaItem {

    /**
     * get the title of an agenda item
     * @return the title of the agenda item
     * @author Sophie Kaiser
     */
    String getTitle();

    /**
     * set the title of an agenda item
     * @param title title of the agenda item
     * @author Sophie Kaiser
     */
    void setTitle(String title);

    /**
     * get the session an agenda item belongs to
     * @return the session the agenda item belongs to
     * @author Sophie Kaiser
     */
    Session getSession();

    /**
     * get all speeches belonging to an agenda item
     * @return all speeches belonging to the agenda item
     * @author Sophie Kaiser
     */
    LinkedList<Speech> getSpeeches();

    /**
     * add a speech to an agenda item
     * @param speech speech to be added to the agenda item
     * @author Sophie Kaiser
     */
    void addSpeech(Speech speech);
}
