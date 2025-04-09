package parliament;

/**
 * tags for content that designate whether a certain piece of text
 * is the introduction of a speaker, part of the speech proper, a quote
 * or a comment
 * @author Sophie Kaiser
 */
public enum ContentTag {

    /**
     * introduction of a speaker at the beginning of the speech
     */
    SPEAKER_INTRODUCTION,

    /**
     * text belonging to the speech proper
     */
    SPEECH_TEXT,

    /**
     * quotation used in the speech
     */
    QUOTE,

    /**
     * comment made during the speech
     */
    COMMENT
}
