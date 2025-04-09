package parliament;

import parliament.impl.Video_Impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;

/**
 * represents a session of the Bundestag
 * @author Oliwia Daszczynska
 */
public interface Session {

    /**
     * get the number of a session within the legislative period
     * @return the number of the session within the legislative period
     * @author Sophie Kaiser
     */
    int getSessionNr();

    /**
     * get the date a session took place on
     * @return the date the session took place on
     * @author Sophie Kaiser
     */
    LocalDate getDate();

    /**
     * set the date a session took place on
     * @param date the date the session took place on
     * @author Sophie Kaiser
     */
    void setDate(LocalDate date);

    /**
     * get the time of day a session began
     * @return the time of day the session began
     * @author Sophie Kaiser
     */
    LocalTime getStartTime();

    /**
     * set the time of day a session began
     * @param time the time of day the session began
     * @author Sophie Kaiser
     */
    void setStartTime(LocalTime time);

    /**
     * get the time of day a session ended
     * @return the time of day the session ended
     * @author Sophie Kaiser
     */
    LocalTime getEndTime();

    /**
     * set the time of day a session ended
     * @param time the time of day the session ended
     * @author Sophie Kaiser
     */
    void setEndTime(LocalTime time);

    /**
     * get the agenda of a session as a list of all items
     * @return the agenda of the session as a list of all items
     * @author Sophie Kaiser
     */
    LinkedList<AgendaItem> getAgendaItems();

    /**
     * add an item to the agenda of a session
     * @param agendaItem item to be added to the agenda of the session
     * @author Sophie Kaiser
     */
    void addAgendaItem(AgendaItem agendaItem);

    /**
     * get video of a session
     * @author Oliwia Daszczynska
     */
    Video getVideo();

    void setVideo(Video video);
}
