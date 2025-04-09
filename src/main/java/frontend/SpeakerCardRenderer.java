package frontend;

import org.bson.Document;

/**
 * Class to render a speaker card based on a given document.
 * @author Ishak Bouaziz
 * */
public class SpeakerCardRenderer implements IRenderable {

    /**
     * Outputs a String in HTML format to render a speaker card.
     * @author Ishak Bouaziz
     * */
    @Override
    public String toHtml(Document document) {
        String id = document.getString("_id");
        String lastName = document.getString("lastname") == null ? "" : document.getString("lastname");
        String firstName = document.getString("firstname") == null ? "" : document.getString("firstname");
        String partyName = document.getString("party_name").isEmpty() ? "Parteilos" : document.getString("party_name");
        return String.format("<a class='card' href='/speakers/%s'><p>%s</p><p>%s</p><p>%s</p></a>", id, lastName, firstName, partyName);
    }
}
