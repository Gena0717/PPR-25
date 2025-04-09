package parliament.impl;

import parliament.AgendaItem;
import parliament.Session;
import parliament.Video;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;

/**
 * implementation of the Session interface,
 * represents a session of the Bundestag
 * @author Oliwia Daszczynska
 */
public class Session_Impl implements Session {

    /**
     * the number of the session within the legislative period
     */
    private final int sessionNr;

    /**
     * the date the session took place on
     */
    private LocalDate date;

    /**
     * the time of day the session began
     */
    private LocalTime startTime;

    /**
     * the time of day the session ended
     */
    private LocalTime endTime;

    /**
     * the agenda of the session as a list of all items
     */
    private LinkedList<AgendaItem> agendaItems = new LinkedList<>();

    /**
     * the video of the session
     */
    private Video video;

    /**
     * package private constructor to ensure Session_Impl can only be
     * instantiated via ParliamentFactory_Impl
     * @param sessionNr the number of the session within the legislative
     *                  period
     * @author Sophie Kaiser
     */
    Session_Impl(
            int sessionNr
    ){
        this.sessionNr = sessionNr;
    }

    /**
     * get the number of a session within the legislative period
     *
     * @return the number of the session within the legislative period
     * @author Sophie Kaiser
     */
    @Override
    public int getSessionNr() {
        return this.sessionNr;
    }

    /**
     * get the date a session took place on
     *
     * @return the date the session took place on
     * @author Sophie Kaiser
     */
    @Override
    public LocalDate getDate() {
        return this.date;
    }

    /**
     * set the date a session took place on
     *
     * @param date the date the session took place on
     * @author Sophie Kaiser
     */
    @Override
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * get the time of day a session began
     *
     * @return the time of day the session began
     * @author Sophie Kaiser
     */
    @Override
    public LocalTime getStartTime() {
        return this.startTime;
    }

    /**
     * set the time of day a session began
     *
     * @param time the time of day the session began
     * @author Sophie Kaiser
     */
    @Override
    public void setStartTime(LocalTime time) {
        this.startTime = time;
    }

    /**
     * get the time of day a session ended
     *
     * @return the time of day the session ended
     * @author Sophie Kaiser
     */
    @Override
    public LocalTime getEndTime() {
        return this.endTime;
    }

    /**
     * set the time of day a session ended
     *
     * @param time the time of day the session ended
     * @author Sophie Kaiser
     */
    @Override
    public void setEndTime(LocalTime time) {
        this.endTime = time;
    }

    /**
     * get the agenda of a session as a list of all items
     *
     * @return the agenda of the session as a list of all items
     * @author Sophie Kaiser
     */
    @Override
    public LinkedList<AgendaItem> getAgendaItems() {
        return this.agendaItems;
    }

    /**
     * add an item to the agenda of a session
     *
     * @param agendaItem item to be added to the agenda of the session
     * @author Sophie Kaiser
     */
    @Override
    public void addAgendaItem(AgendaItem agendaItem) {
        this.agendaItems.add(agendaItem);
    }

    /**
     * get video of this session
     * @return video of this session
     * @author Oliwia Daszczynska
     */
    @Override
    public Video getVideo(){
        return this.video;
    }

    /**
     * set video of this session
     * @param video of the session
     * @author Oliwia Daszczynska
     */
    @Override
    public void setVideo(Video video){
        this.video = video;
    }
}
