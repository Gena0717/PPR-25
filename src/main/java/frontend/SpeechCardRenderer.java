package frontend;

import database.MongoDatabaseHandler_Impl;
import org.bson.Document;

/**
 * render speech card
 */
public class SpeechCardRenderer implements IRenderable {
    /**
     * speech document to html
     *
     * @param document speech document
     * @return String in HTML format.
     * @author Ishak Bouaziz
     * @author Oliwia Daszczynska
     */
    @Override
    public String toHtml(Document document) {
        String id = document.getString("_id");
        Integer nr = document.getInteger("session_nr");
        String agenda = document.getString("agenda_item") == null ? "No agenda title" : document.getString("agenda_item");
        String speaker_id = document.getString("speaker_id");
        Document speaker = MongoDatabaseHandler_Impl.getCollectionSpeaker().find(new Document("_id", speaker_id)).first();
        assert speaker != null;
        String speakerName = speaker.getString("lastname") + " " + speaker.getString("firstname");
        String party = speaker.getString("party_name");
        return String.format(
                "<a class='card' href='/speeches/%s'><p>%d. Sitzung</p><p>%s</p><p>%s</p><p>%s</p></a>",
                id, nr, agenda, speakerName, party
        );
    }
}
