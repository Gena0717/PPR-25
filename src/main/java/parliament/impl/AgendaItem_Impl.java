package parliament.impl;

import parliament.AgendaItem;
import parliament.Session;
import parliament.Speech;

import java.util.LinkedList;

/**
 * implementation of the AgendaItem interface,
 * represents an item on the agenda of a session of the Bundestag
 * @author Sophie Kaiser
 */
public class AgendaItem_Impl implements AgendaItem {

    /**
     * title of the agenda item
     */
    private String title;

    /**
     * the session the agenda item belongs to
     */
    private final Session session;

    /**
     * all speeches belonging to the agenda item
     */
    private LinkedList<Speech> speeches = new LinkedList<>();

    /**
     * package private constructor to ensure AgendaItem_Impl can only be
     * instantiated via ParliamentFactory_Impl;
     * agenda item is added to the session it belongs to
     * @param title title of the agenda item
     * @param session the session the agenda item belongs to
     * @author Sophie Kaiser
     */
    AgendaItem_Impl(
            String title,
            Session session
    ){
        this.title = title;
        this.session = session;
        // add new agenda item to its session
        this.session.addAgendaItem(this);
    }

    /**
     * get the title of an agenda item
     *
     * @return the title of the agenda item
     * @author Sophie Kaiser
     */
    @Override
    public String getTitle() {
        return this.title;
    }

    /**
     * set the title of an agenda item
     *
     * @param title title of the agenda item
     * @author Sophie Kaiser
     */
    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * get the session an agenda item belongs to
     *
     * @return the session the agenda item belongs to
     * @author Sophie Kaiser
     */
    @Override
    public Session getSession() {
        return this.session;
    }

    /**
     * get all speeches belonging to an agenda item
     *
     * @return all speeches belonging to the agenda item
     * @author Sophie Kaiser
     */
    @Override
    public LinkedList<Speech> getSpeeches() {
        return this.speeches;
    }

    /**
     * add a speech to an agenda item
     *
     * @param speech speech to be added to the agenda item
     * @author Sophie Kaiser
     */
    @Override
    public void addSpeech(Speech speech) {
        this.speeches.add(speech);
    }
}
