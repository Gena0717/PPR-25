package parliament.impl;

import parliament.*;

import java.util.LinkedList;

/**
 * implementation of the Speech interface,
 * represents a speech given during a session of the Bundestag,
 * including any comments made during it
 * @author Oliwia Daszczynska
 */
public class Speech_Impl implements Speech {

    /**
     * the ID of the speech
     */
    private final String id;

    /**
     * the speaker giving the speech
     */
    private Speaker speaker;

    /**
     * the agenda item the speech belongs to
     */
    private final AgendaItem agendaItem;

    /**
     * the text contents of the speech, saved as sections of the
     * speech in a linked list
     */
    private LinkedList<Content> contents = new LinkedList<>();

    /**
     * the video recording of the speech, optional
     */
    private Video video;

    /**
     * package private constructor to ensure Speech_Impl can only be
     * instantiated via ParliamentFactory_Impl,
     * speech is added to the agenda item it belongs to
     * @param id the ID of the speech
     * @param speaker the speaker giving the speech
     * @param agendaItem the agenda item the speech belongs to
     * @author Sophie Kaiser
     */
    Speech_Impl(
            String id,
            Speaker speaker,
            AgendaItem agendaItem
    ){
        this.id = id;
        this.speaker = speaker;
        this.agendaItem = agendaItem;
        // add new speech to its agenda item
        this.agendaItem.addSpeech(this);
    }

    /**
     * get the ID of a speech
     *
     * @return the ID of the speech
     * @author Sophie Kaiser
     */
    @Override
    public String getID() {
        return this.id;
    }

    /**
     * get the speaker that gave a speech
     *
     * @return the speaker that gave the speech
     * @author Sophie Kaiser
     */
    @Override
    public Speaker getSpeaker() {
        return this.speaker;
    }

    /**
     * set the speaker that gave a speech
     *
     * @param speaker the speaker that gave the speech
     * @author Sophie Kaiser
     */
    @Override
    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }

    /**
     * get the agenda item a speech belongs to
     *
     * @return the agenda item the speech belongs to
     * @author Sophie Kaiser
     */
    @Override
    public AgendaItem getAgendaItem() {
        return this.agendaItem;
    }

    /**
     * get the session during which a speech was given
     *
     * @return the session during which the speech was given
     * @author Sophie Kaiser
     */
    @Override
    public Session getSession() {
        return this.agendaItem.getSession();
    }

    /**
     * get the text contents of a speech
     *
     * @return the text contents of the speech
     * @author Sophie Kaiser
     */
    @Override
    public LinkedList<Content> getContents() {
        return this.contents;
    }

    /**
     * add a piece of text content to a speech
     *
     * @param content piece of text content to be added to the speech
     * @author Sophie Kaiser
     */
    @Override
    public void addContent(Content content) {
        this.contents.add(content);
    }

    /**
     * get the video recording of a speech
     *
     * @return the video recording of the speech
     * @author Sophie Kaiser
     */
    @Override
    public Video getVideo() {
        return this.video;
    }

    /**
     * set the video recording of a speech
     *
     * @param video the video recording of the speech
     * @author Sophie Kaiser
     */
    @Override
    public void setVideo(Video video) {
        this.video = video;
    }
}
