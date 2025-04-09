package parliament.impl;

import parliament.Picture;
import parliament.Speaker;

import java.time.LocalDateTime;

/**
 * implementation of the Picture interface
 * represents a portrait picture of a member of the Bundestag
 *
 * @author Sophie Kaiser
 */
public class Picture_Impl implements Picture {

    /**
     * the number of the picture in the Bundestag picture database
     */
    private final int pictureNumber;

    /**
     * description of the picture
     */
    private String description;

    /**
     * the speaker depicted in the picture
     */
    private Speaker speaker;

    /**
     * the location where the picture was taken
     */
    private String location;

    /**
     * the date and time the picture was taken
     */
    private LocalDateTime dateTime;

    /**
     * the name of the photographer that took the picture
     */
    private String photographer;

    /**
     * the URL of the picture
     */
    private String url;

    /**
     * package private constructor to ensure Picture_Impl can only be
     * instantiated via ParliamentFactory_Impl
     *
     * @param pictureNumber the number of the picture in the Bundestag
     *                      picture database
     * @author Sophie Kaiser
     */
    Picture_Impl(int pictureNumber) {
        this.pictureNumber = pictureNumber;
    }

    /**
     * get the number of a picture in the Bundestag picture database
     *
     * @return the number of the picture in the Bundestag picture database
     * @author Sophie Kaiser
     */
    @Override
    public int getPictureNumber() {
        return this.pictureNumber;
    }

    /**
     * get the description of a picture
     *
     * @return the description of the picture
     * @author Sophie Kaiser
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * set the description of a picture
     *
     * @param description the description of the picture
     * @author Sophie Kaiser
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get the speaker depicted in a picture
     *
     * @return the speaker depicted in the picture
     * @author Sophie Kaiser
     */
    @Override
    public Speaker getSpeaker() {
        return this.speaker;
    }

    /**
     * set the speaker depicted in a picture,
     * set the speaker's picture to this picture
     *
     * @param speaker the speaker depicted in the picture
     * @author Sophie Kaiser
     * @author Oliwia Daszczynska
     */
    @Override
    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
        if (this.speaker.getPicture() == null) {  //if the speaker doesn't have a picture yet
            this.speaker.setPicture(this);        // set this as their picture
        }
    }

    /**
     * get the location where a picture was taken
     *
     * @return the location where the picture was taken
     * @author Sophie Kaiser
     */
    @Override
    public String getLocation() {
        return this.location;
    }

    /**
     * set the location where a picture was taken
     *
     * @param location the location where the picture was taken
     * @author Sophie Kaiser
     */
    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * get the date and time a picture was taken
     *
     * @return the date and time the picture was taken
     * @author Sophie Kaiser
     */
    @Override
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    /**
     * set the date and time a picture was taken
     *
     * @param dateTime the date and time the picture was taken
     * @author Sophie Kaiser
     */
    @Override
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * get the name of the photographer that took a picture
     *
     * @return the name of the photographer that took the picture
     * @author Sophie Kaiser
     */
    @Override
    public String getPhotographer() {
        return this.photographer;
    }

    /**
     * set the name of the photographer that took a picture
     *
     * @param photographer the name og the photographer that took
     *                     the picture
     * @author Sophie Kaiser
     */
    @Override
    public void setPhotographer(String photographer) {
        this.photographer = photographer;
    }

    /**
     * get the URL of a picture
     *
     * @return the URL of the picture
     * @author Sophie Kaiser
     */
    @Override
    public String getURL() {
        return this.url;
    }

    /**
     * set the URL of a picture
     *
     * @param url the URL of the picture
     * @author Sophie Kaiser
     */
    @Override
    public void setURL(String url) {
        this.url = url;
    }
}
