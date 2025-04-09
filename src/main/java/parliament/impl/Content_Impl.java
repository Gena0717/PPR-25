package parliament.impl;

import parliament.Content;
import parliament.ContentTag;
import parliament.Speech;


/**
 * implementation of the Content interface,
 * represents a piece of text from the protocol of a speech given in a
 * session of the Bundestag;
 * this unit is intended to potentially have NLP analysis performed on it
 * @author Sophie Kaiser
 */
public class Content_Impl implements Content {

    /**
     * ID of the piece of content,
     * constructed from the ID of the speech it is part of and the index
     * of the content in the list of the speech's contents
     */
    private final String id;

    /**
     * tag designating the content as the introduction of the speaker
     * at the beginning of the speech, a part of the speech itself,
     * a comment or a quote
     */
    private final ContentTag contentTag;

    /**
     * the text content of the piece of content
     */
    private final String text;

    /**
     * the speech the piece of content is part of
     */
    private final Speech speech;

    /**
     * package private constructor to ensure Content_Impl can only be
     * instantiated via ParliamentFactory_Impl;
     * content is added to the speech it is part of and assigned
     * an ID
     * @param contentTag tag describing the type of content
     * @param text the text content of the piece of content
     * @param speech the speech the piece of content is part of
     * @author Sophie Kaiser
     */
    Content_Impl(
            ContentTag contentTag,
            String text,
            Speech speech
    ){
        this.contentTag = contentTag;
        this.text = text;
        this.speech = speech;
        /* add content to its speech so its index in the list of contents
        can be accessed
         */
        this.speech.addContent(this);
        /* construct the ID of the content from the ID of the speech it
        belongs to and the index of the content in the list of the speech's
        contents
         */
        this.id = this.speech.getID() + "-"
                + this.speech.getContents().indexOf(this);
    }

    /**
     * get the ID of a piece of content
     *
     * @return ID of the piece of content,
     * constructed from the ID of the speech it is part of and the index
     * of the content in the list of the speech's contents
     * @author Sophie Kaiser
     */
    @Override
    public String getID() {
        return this.id;
    }

    /**
     * tag designating the content as the introduction of the speaker
     * at the beginning of the speech, a part of the speech itself,
     * a comment or a quote;
     *
     * @return tag describing the type of content the piece of content is
     * @author Sophie Kaiser
     */
    @Override
    public ContentTag getContentTag() {
        return this.contentTag;
    }

    /**
     * get the text content of a piece of content
     *
     * @return the text content of the piece of content
     * @author Sophie Kaiser
     */
    @Override
    public String getText() {
        return this.text;
    }

    /**
     * get the speech a piece of content belongs to
     *
     * @return the speech the piece of content belongs to
     * @author Sophie Kaiser
     */
    @Override
    public Speech getSpeech() {
        return this.speech;
    }
}
