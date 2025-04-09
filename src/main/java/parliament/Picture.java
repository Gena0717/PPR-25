package parliament;

import java.time.LocalDateTime;

/**
 * represents a portrait picture of a member of the Bundestag
 * @author Sophie Kaiser
 */
public interface Picture {

    /**
     * get the number of a picture in the Bundestag picture database
     * @return the number of the picture in the Bundestag picture database
     * @author Sophie Kaiser
     */
    int getPictureNumber();

    /**
     * get the description of a picture
     * @return the description of the picture
     * @author Sophie Kaiser
     */
    String getDescription();

    /**
     * set the description of a picture
     * @param description the description of the picture
     * @author Sophie Kaiser
     */
    void setDescription(String description);

    /**
     * get the speaker depicted in a picture
     * @return the speaker depicted in the picture
     * @author Sophie Kaiser
     */
    Speaker getSpeaker();

    /**
     * set the speaker depicted in a picture
     * @param speaker the speaker depicted in the picture
     * @author Sophie Kaiser
     */
    void setSpeaker(Speaker speaker);

    /**
     * get the location where a picture was taken
     * @return the location where the picture was taken
     * @author Sophie Kaiser
     */
    String getLocation();

    /**
     * set the location where a picture was taken
     * @param location the location where the picture was taken
     * @author Sophie Kaiser
     */
    void setLocation(String location);

    /**
     * get the date and time a picture was taken
     * @return the date and time the picture was taken
     * @author Sophie Kaiser
     */
    LocalDateTime getDateTime();

    /**
     * set the date and time a picture was taken
     * @param dateTime the date and time the picture was taken
     * @author Sophie Kaiser
     */
    void setDateTime(LocalDateTime dateTime);

    /**
     * get the name of the photographer that took a picture
     * @return the name of the photographer that took the picture
     * @author Sophie Kaiser
     */
    String getPhotographer();

    /**
     * set the name of the photographer that took a picture
     * @param photographer the name og the photographer that took
     *                     the picture
     * @author Sophie Kaiser
     */
    void setPhotographer(String photographer);

    /**
     * get the URL of a picture
     * @return the URL of the picture
     * @author Sophie Kaiser
     */
    String getURL();

    /**
     * set the URL of a picture
     * @param url the URL of the picture
     * @author Sophie Kaiser
     */
    void setURL(String url);
}
