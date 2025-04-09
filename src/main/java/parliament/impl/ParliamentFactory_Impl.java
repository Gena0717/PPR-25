package parliament.impl;

import org.bson.Document;
import parliament.*;

import java.util.HashSet;
import java.util.Set;

/**
 * implementation of the ParliamentFactory interface,
 * factory for the creation of objects that are associated
 * with the Bundestag;
 * created objects are kept track of within the factory
 * @author Sophie Kaiser
 */
public class ParliamentFactory_Impl implements ParliamentFactory {

    /**
     * all agenda item objects created by the factory
     */
    private Set<AgendaItem> agendaItems = new HashSet<>();

    /**
     * all content objects created by the factory
     */
    private Set<Content> contents = new HashSet<>();

    /**
     * all grouping objects created by the factory
     */
    private Set<Grouping> groupings = new HashSet<>();

    /**
     * all party objects created by the factory
     */
    private Set<Party> parties = new HashSet<>();

    /**
     * all picture objects created by the factory
     */
    private Set<Picture> pictures = new HashSet<>();

    /**
     * all session objects created by the factory
     */
    private Set<Session> sessions = new HashSet<>();

    /**
     * all speaker objects created by the factory
     */
    private Set<Speaker> speakers = new HashSet<>();

    /**
     * all speech objects created by the factory
     */
    private Set<Speech> speeches = new HashSet<>();

    /**
     * all video objects created by the factory
     */
    private Set<Video> videos = new HashSet<>();

    /**
     * empty constructor, no parameters required
     * @author Sophie Kaiser
     */
    public ParliamentFactory_Impl() {}

    /**
     * create an agenda item object
     *
     * @param title   title of the agenda item
     * @param session session the agenda item belongs to
     * @return new agenda item object
     * @author Sophie Kaiser
     */
    @Override
    public AgendaItem_Impl createAgendaItem(String title, Session session) {
        AgendaItem_Impl agendaItem = new AgendaItem_Impl(title,session);
        this.agendaItems.add(agendaItem);
        return agendaItem;
    }

    /**
     * get all agenda item objects created by a factory
     *
     * @return all agenda item objects created by the factory
     * @author Sophie Kaiser
     */
    @Override
    public Set<AgendaItem> getAgendaItems() {
        return this.agendaItems;
    }

    /**
     * create a content object
     *
     * @param contentTag tag describing the type of content
     * @param text       text content of the content object
     * @param speech     speech the content object is part of
     * @return new content object
     * @author Sophie Kaiser
     */
    @Override
    public Content_Impl createContent(ContentTag contentTag, String text, Speech speech) {
        Content_Impl content = new Content_Impl(contentTag,text,speech);
        this.contents.add(content);
        return content;
    }

    /**
     * get all content objects created by a factory
     *
     * @return all content objects created by the factory
     * @author Sophie Kaiser
     */
    @Override
    public Set<Content> getContents() {
        return this.contents;
    }

    /**
     * create a group object
     *
     * @param name  name of the grouping or group
     * @param group true if the grouping has group status in the Bundestag,
     *              false if not
     * @return new grouping object
     * @author Sophie Kaiser
     */
    @Override
    public Grouping_Impl createGrouping(String name, boolean group) {
        Grouping_Impl grouping = new Grouping_Impl(name, group);
        this.groupings.add(grouping);
        return grouping;
    }

    /**
     * get all grouping objects created by a factory
     *
     * @return all grouping objects created by the factory
     * @author Sophie Kaiser
     */
    @Override
    public Set<Grouping> getGroupings() {
        return this.groupings;
    }

    /**
     * create a party object
     *
     * @param name name of the party
     * @return new party object
     * @author Sophie Kaiser
     */
    @Override
    public Party_Impl createParty(String name) {
        Party_Impl party = new Party_Impl(name);
        this.parties.add(party);
        return party;
    }

    /**
     * get all party objects created by a factory
     *
     * @return all party objects created by the factory
     * @author Sophie Kaiser
     */
    @Override
    public Set<Party> getParties() {
        return this.parties;
    }

    /**
     * create a picture object
     *
     * @param pictureNumber number of the picture in the Bundestag
     *                      picture database
     * @return new picture object
     * @author Sophie Kaiser
     */
    @Override
    public Picture_Impl createPicture(int pictureNumber) {
        Picture_Impl picture = new Picture_Impl(pictureNumber);
        this.pictures.add(picture);
        return picture;
    }

    /**
     * get all picture objects created by a factory
     *
     * @return all picture objects created by the factory
     * @author Sophie Kaiser
     */
    @Override
    public Set<Picture> getPictures() {
        return this.pictures;
    }

    /**
     * create a session object
     *
     * @param sessionNr number of the session within the
     *                  legislative period
     * @return new session object
     * @author Sophie Kaiser
     */
    @Override
    public Session_Impl createSession(int sessionNr) {
        Session_Impl session = new Session_Impl(sessionNr);
        this.sessions.add(session);
        return session;
    }

    /**
     * get all session objects created by a factory
     *
     * @return all session objects created by the factory
     * @author Sophie Kaiser
     */
    @Override
    public Set<Session> getSessions() {
        return this.sessions;
    }

    /**
     * create a speaker object
     *
     * @param id        the ID of the speaker
     * @param lastName  the last name of the speaker
     * @param firstName the first name of the speaker
     * @return new speaker object
     * @author Sophie Kaiser
     */
    @Override
    public Speaker_Impl createSpeaker(
            String id, String lastName, String firstName
    ) {
        Speaker_Impl speaker = new Speaker_Impl(id, lastName, firstName);
        this.speakers.add(speaker);
        return speaker;
    }

    /**
     *create a speaker object based on a given document
     * @param document the associated database document
     * @return new speaker object
     **/
    @Override
    public Speaker_Impl createSpeaker(Document document){
        return createSpeaker(document.getString("lastname"), document.getString("firstname"), document.getString("_id"));
    }

    /**
     * get all speaker objects created by a factory
     *
     * @return all speaker objects created by the factory
     * @author Sophie Kaiser
     */
    @Override
    public Set<Speaker> getSpeakers() {
        return this.speakers;
    }

    /**
     * clears the collection of speakers held by the factory
     * @author Ishak Bouaziz
     */
    @Override
    public void clearSpeakers(){
        this.speakers.clear();
    }

    /**
     * create a speech object
     *
     * @param id         the ID of the speech
     * @param speaker    the speaker giving the speech
     * @param agendaItem the agenda item the speech belongs to
     * @return new speech object
     * @author Sophie Kaiser
     */
    @Override
    public Speech_Impl createSpeech(
            String id, Speaker speaker, AgendaItem agendaItem
    ) {
        Speech_Impl speech = new Speech_Impl(id, speaker, agendaItem);
        this.speeches.add(speech);
        return speech;
    }

    /**
     * get all speech objects created by a factory
     *
     * @return all speech objects created by the factory
     * @author Sophie Kaiser
     */
    @Override
    public Set<Speech> getSpeeches() {
        return this.speeches;
    }

    /**
     * create a video object
     *
     * @param id      the video ID
     * @param session the session the video is a recording of
     * @param url     the video URL
     * @return new video object
     * @author Sophie Kaiser
     */
    @Override
    public Video_Impl createVideo(String id, Session session, String url) {
        Video_Impl video = new Video_Impl(id, session, url);
        this.videos.add(video);
        return video;
    }

    /**
     * get all video objects created by a factory
     *
     * @return all video objects created by the factory
     * @author Sophie Kaiser
     */
    @Override
    public Set<Video> getVideos() {
        return this.videos;
    }
}
