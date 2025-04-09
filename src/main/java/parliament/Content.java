package parliament;

/**
 * represents a piece of text from the protocol of a speech given in a
 * session of the Bundestag;
 * this unit is intended to potentially have NLP analysis performed on it
 * @author Sophie Kaiser
 */
public interface Content {

    /**
     * get the ID of a piece of content
     * @return ID of the piece of content
     * @author Sophie Kaiser
     */
    String getID();

    /**
     * tag designating the content as the introduction of the speaker
     * at the beginning of the speech, a part of the speech itself,
     * a comment or a quote;
     * @return tag describing the type of content the piece of content is
     * @author Sophie Kaiser
     */
    ContentTag getContentTag();

    /**
     * get the text content of a piece of content
     * @return the text content of the piece of content
     * @author Sophie Kaiser
     */
    String getText();

    /**
     * get the speech a piece of content belongs to
     * @return the speech the piece of content belongs to
     * @author Sophie Kaiser
     */
    Speech getSpeech();
}
