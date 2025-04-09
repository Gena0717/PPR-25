package parliament;

import java.time.LocalDate;

/**
 * represents a person that spoke during a session of the Bundestag,
 * can be a member of the Bundestag or a guest speaker
 * @author Oliwia Daszczynska
 */
public interface Speaker {

    /**
     * get the ID of a speaker
     * @return the ID of the speaker
     * @author Sophie Kaiser
     */
    String getID();

    /**
     * get the first name of a speaker
     * @return the first name of the speaker
     * @author Sophie Kaiser
     */
    String getFirstName();

    /**
     * set the first name of a speaker
     * @param firstName the first name of the speaker
     * @author Sophie Kaiser
     */
    void setFirstName(String firstName);

    /**
     * get the last name of a speaker
     * @return the last name of the speaker
     * @author Sophie Kaiser
     */
    String getLastName();

    /**
     * set the last name of a speaker
     * @param lastName the last name of the speaker
     * @author Sophie Kaiser
     */
    void setLastName(String lastName);

    /**
     * get the status of a speaker as a member of the Bundestag or
     * as guest speaker
     * @return true if the speaker is a member of the (current) Bundestag
     * false if not
     * @author Sophie Kaiser
     */
    boolean isMember();

    /**
     * set the status of a speaker as a member of the Bundestag or
     * as a guest speaker
     * @param membership true if the speaker is a member of the (current)
     *                   Bundestag,
     *                   false if not
     * @author Sophie Kaiser
     */
    void setMembership(boolean membership);

    /**
     * get the location of a speaker (as an addition to their name)
     * @return the location of the speaker (as an addition to their name)
     * @author Sophie Kaiser
     */
    String getLocation();

    /**
     * set the location of a speaker (as an addition to their name)
     * @param location the location of the speaker (as and addition to
     *                 their name)
     * @author Sophie Kaiser
     */
    void setLocation(String location);

    /**
     * get the title of nobility of a speaker
     * @return the title of nobility of the speaker
     * @author Sophie Kaiser
     */
    String getNobility();

    /**
     * set the title of nobility of a speaker
     * @param nobility the title of nobility of the speaker
     * @author Sophie Kaiser
     */
    void setNobility(String nobility);

    /**
     * get the prefix to the last name of a speaker
     * @return prefix to the last name of the speaker
     * @author Sophie Kaiser
     */
    String getPrefix();

    /**
     * set the prefix to the last name of a speaker
     * @param prefix prefix to the last name of the speaker
     * @author Sophie Kaiser
     */
    void setPrefix(String prefix);

    /**
     * get the title a speaker is addressed with
     * @return the title the speaker is addressed with
     * @author Sophie Kaiser
     */
    String getAddressTitle();

    /**
     * set the title a speaker is addressed with
     * @param addressTitle the title the speaker is addressed with
     * @author Sophie Kaiser
     */
    void setAddressTitle(String addressTitle);

    /**
     * get the academic title of a speaker
     * @return the academic title of the speaker
     * @author Sophie Kaiser
     */
    String getAcademicTitle();

    /**
     * set the academic title of a speaker
     * @param academicTitle the academic title of the speaker
     * @author Sophie Kaiser
     */
    void setAcademicTitle(String academicTitle);

    /**
     * get the date of birth of a speaker
     * @return the date of birth of the speaker
     * @author Sophie Kaiser
     */
    LocalDate getDateOfBirth();

    /**
     * set the date of birth of a speaker
     * @param dateOfBirth the date of birth of the speaker
     * @author Sophie Kaiser
     */
    void setDateOfBirth(LocalDate dateOfBirth);

    /**
     * get the place of birth of a speaker
     * @return the place of birth of the speaker
     * @author Sophie Kaiser
     */
    String getPlaceOfBirth();

    /**
     * set the place of birth of a speaker
     * @param placeOfBirth the place of birth of the speaker
     * @author Sophie Kaiser
     */
    void setPlaceOfBirth(String placeOfBirth);

    /**
     * get the country of birth of a speaker
     * @return the country of birth of the speaker
     * @author Sophie Kaiser
     */
    String getCountryOfBirth();

    /**
     * set the country of birth of a speaker
     * @param countryOfBirth the country of birth of the speaker
     * @author Sophie Kaiser
     */
    void setCountryOfBirth(String countryOfBirth);

    /**
     * get the date of death of a speaker
     * @return the date of death of the speaker
     * @author Sophie Kaiser
     */
    LocalDate getDateOfDeath();

    /**
     * set the date of death of a speaker
     * @param dateOfDeath the date of death of the speaker
     * @author Sophie Kaiser
     */
    void setDateOfDeath(LocalDate dateOfDeath);

    /**
     * get the gender of a speaker
     * @return the gender of the speaker
     * @author Sophie Kaiser
     */
    String getGender();

    /**
     * set the gender of a speaker
     * @param gender the gender of the speaker
     * @author Sophie Kaiser
     */
    void setGender(String gender);

    /**
     * get the marital status of a speaker
     * @return the marital status of the speaker
     * @author Sophie Kaiser
     */
    String getMaritalStatus();

    /**
     * set the marital status of a speaker
     * @param maritalStatus the marital status of the speaker
     * @author Sophie Kaiser
     */
    void setMaritalStatus(String maritalStatus);

    /**
     * get the religion of a speaker
     * @return the religion of the speaker
     * @author Sophie Kaiser
     */
    String getReligion();

    /**
     * set the religion of a speaker
     * @param religion the religion of the speaker
     * @author Sophie Kaiser
     */
    void setReligion(String religion);

    /**
     * get the profession of a speaker
     * @return the profession of the speaker
     * @author Sophie Kaiser
     */
    String getProfession();

    /**
     * set the profession of a speaker
     * @param profession the profession of the speaker
     * @author Sophie Kaiser
     */
    void setProfession(String profession);

    /**
     * get the curriculum vitae of a speaker
     * @return the curriculum vitae of the speaker
     * @author Sophie Kaiser
     */
    String getCurriculumVitae();

    /**
     * set the curriculum vitae of a speaker
     * @param curriculumVitae the curriculum vitae of the speaker
     * @author Sophie Kaiser
     */
    void setCurriculumVitae(String curriculumVitae);

    /**
     * get the party of a speaker
     * @return the party of the speaker
     * @author Sophie Kaiser
     */
    Party getParty();

    /**
     * set the party of a speaker
     * @param party the party of the speaker
     * @author Sophie Kaiser
     */
    void setParty(Party party);

    /**
     * get the grouping of a speaker
     * @return the grouping of the speaker
     * @author Sophie Kaiser
     */
    Grouping getGrouping();

    /**
     * set the grouping of a speaker
     * @param grouping the grouping of the speaker
     * @author Sophie Kaiser
     */
    void setGrouping(Grouping grouping);

    /**
     * get the portrait picture of a speaker
     * @return the portrait picture of the speaker
     * @author Sophie Kaiser
     */
    Picture getPicture();

    /**
     * set the portrait picture of a speaker
     * @param picture the portrait picture of the speaker
     * @author Sophie Kaiser
     */
    void setPicture(Picture picture);
}
