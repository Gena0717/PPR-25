package parliament;

import org.bson.Document;
import parliament.impl.Speaker_Impl;

import java.util.Set;

/**
 * factory for the creation of objects that are associated
 * with the Bundestag;
 * created objects are kept track of within the factory
 * @author Sophie Kaiser
 */
public interface ParliamentFactory {

    /**
     * create an agenda item object
     * @param title title of the agenda item
     * @param session session the agenda item is part of
     * @return new agenda item object
     * @author Sophie Kaiser
     */
    AgendaItem createAgendaItem(
            String title,
            Session session
    );

    /**
     * get all agenda item objects created by a factory
     * @return all agenda item objects created by the factory
     * @author Sophie Kaiser
     */
    Set<AgendaItem> getAgendaItems();

    /**
     * create a content object
     * @param contentTag tag describing the type of content
     * @param text text content of the content object
     * @param speech speech the content object is part of
     * @return new content object
     * @author Sophie Kaiser
     */
    Content createContent(
            ContentTag contentTag,
            String text,
            Speech speech
    );

    /**
     * get all content objects created by a factory
     * @return all content objects created by the factory
     * @author Sophie Kaiser
     */
    Set<Content> getContents();

    /**
     * create a group object
     * @param name name of the grouping or group
     * @param group status of the grouping as a group in the Bundestag
     * @return new grouping object
     * @author Sophie Kaiser
     */
    Grouping createGrouping(
            String name,
            boolean group
    );

    /**
     * get all grouping objects created by a factory
     * @return all grouping objects created by the factory
     * @author Sophie Kaiser
     */
    Set<Grouping> getGroupings();

    /**
     * create a party object
     * @param name name of the party
     * @return new party object
     * @author Sophie Kaiser
     */
    Party createParty(
            String name
    );

    /**
     * get all party objects created by a factory
     * @return all party objects created by the factory
     * @author Sophie Kaiser
     */
    Set<Party> getParties();

    /**
     * create a picture object
     * @param pictureNumber number of the picture in the Bundestag
     *                      picture database
     * @return new picture object
     * @author Sophie Kaiser
     */
    Picture createPicture(
            int pictureNumber
    );

    /**
     * get all picture objects created by a factory
     * @return all picture objects created by the factory
     * @author Sophie Kaiser
     */
    Set<Picture> getPictures();

    /**
     * create a session object
     * @param SessionNr number of the session within the
     *                  legislative period
     * @return new session object
     * @author Sophie Kaiser
     */
    Session createSession(
            int SessionNr
    );

    /**
     * get all session objects created by a factory
     * @return all session objects created by the factory
     * @author Sophie Kaiser
     */
    Set<Session> getSessions();

    /**
     * create a speaker object
     * @param id the ID of the speaker
     * @param lastName the last name of the speaker
     * @param firstName the first name of the speaker
     * @return new speaker object
     * @author Sophie Kaiser
     */
    Speaker createSpeaker(
            String id,
            String lastName,
            String firstName
    );

    /**
     *create a speaker object based on a given document
     * @param document the associated database document
     * @return new speaker object
     * @author Ishak Bouaziz
     **/
    Speaker_Impl createSpeaker(Document document);

    /**
     * get all speaker objects created by a factory
     * @return all speaker objects created by the factory
     * @author Sophie Kaiser
     */
    Set<Speaker> getSpeakers();

    /**
     * clears the collection of speakers held by the factory
     * @author Ishak Bouaziz
     */
    void clearSpeakers();

    /**
     * create a speech object
     * @param id the ID of the speech
     * @param speaker the speaker giving the speech
     * @param agendaItem the agenda item the speech belongs to
     * @return new speech object
     * @author Sophie Kaiser
     */
    Speech createSpeech(
            String id,
            Speaker speaker,
            AgendaItem agendaItem
    );

    /**
     * get all speech objects created by a factory
     * @return all speech objects created by the factory
     * @author Sophie Kaiser
     */
    Set<Speech> getSpeeches();

    /**
     * create a video object
     * @param id the video ID
     * @param session the session the video is a recording of
     * @param url the video URL
     * @return new video object
     * @author Sophie Kaiser
     */
    Video createVideo(
            String id,
            Session session,
            String url
    );

    /**
     * get all video objects created by a factory
     * @return all video objects created by the factory
     * @author Sophie Kaiser
     */
    Set<Video> getVideos();
}
