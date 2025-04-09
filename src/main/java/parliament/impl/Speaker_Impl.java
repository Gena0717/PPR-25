package parliament.impl;

import parliament.*;

import java.time.LocalDate;

/**
 * implementation of the Speaker interface,
 * represents a person that spoke during a session of the Bundestag,
 * can be a member of the Bundestag or a guest speaker
 *
 * @author Oliwia Daszczynska
 */
public class Speaker_Impl implements Speaker {

    /**
     * the ID of the speaker
     */
    private final String id;

    /**
     * the last name of the speaker
     */
    private String lastName;

    /**
     * the first name of the speaker
     */
    private String firstName;

    /**
     * the status of a speaker as a member of the Bundestag or
     * as guest speaker,
     * true if the speaker is a member of the (current) Bundestag
     * false if not
     */
    private boolean member;

    /**
     * the location of the speaker (as an addition to their name)
     */
    private String location;

    /**
     * the title of nobility of the speaker
     */
    private String nobility;

    /**
     * prefix to the last name of the speaker
     */
    private String prefix;

    /**
     * the title the speaker is addressed with
     */
    private String addressTitle;

    /**
     * the academic title of the speaker
     */
    private String academicTitle;

    /**
     * the date of birth of the speaker
     */
    private LocalDate dateOfBirth;

    /**
     * the place of birth of the speaker
     */
    private String placeOfBirth;

    /**
     * the country of birth of the speaker
     */
    private String countryOfBirth;

    /**
     * the date of death of the speaker
     */
    private LocalDate dateOfDeath;

    /**
     * the gender of the speaker
     */
    private String gender;

    /**
     * the marital status of the speaker
     */
    private String maritalStatus;

    /**
     * the religion of the speaker
     */
    private String religion;

    /**
     * the profession of the speaker
     */
    private String profession;

    /**
     * the curriculum vitae of the speaker
     */
    private String curriculumVitae;

    /**
     * the party of the speaker
     */
    private Party party;

    /**
     * the grouping of the speaker
     */
    private Grouping grouping;

    /**
     * the portrait picture of the speaker
     */
    private Picture picture;

    /**
     * package private constructor to ensure Speaker_Impl can only be
     * instantiated via ParliamentFactory_Impl
     *
     * @param id        the ID of the speaker
     * @param lastName  the last name of the speaker
     * @param firstName the first name of the speaker
     * @author Sophie Kaiser
     */
    Speaker_Impl(
            String id,
            String lastName,
            String firstName
    ) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    /**
     * get the ID of a speaker
     *
     * @return the ID of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public String getID() {
        return this.id;
    }

    /**
     * get the first name of a speaker
     *
     * @return the first name of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * set the first name of a speaker
     *
     * @param firstName the first name of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * get the last name of a speaker
     *
     * @return the last name of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public String getLastName() {
        return this.lastName;
    }

    /**
     * set the last name of a speaker
     *
     * @param lastName the last name of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * get the status of a speaker as a member of the Bundestag or
     * as guest speaker
     *
     * @return true if the speaker is a member of the (current) Bundestag
     * false if not
     * @author Sophie Kaiser
     */
    @Override
    public boolean isMember() {
        return this.member;
    }

    /**
     * set the status of a speaker as a member of the Bundestag or
     * as a guest speaker
     *
     * @param membership true if the speaker is a member of the (current)
     *                   Bundestag,
     *                   false if not
     * @author Sophie Kaiser
     */
    @Override
    public void setMembership(boolean membership) {
        this.member = membership;
    }

    /**
     * get the location of a speaker (as an addition to their name)
     *
     * @return the location of the speaker (as an addition to their name)
     * @author Sophie Kaiser
     */
    @Override
    public String getLocation() {
        return this.location;
    }

    /**
     * set the location of a speaker (as an addition to their name)
     *
     * @param location the location of the speaker (as and addition to
     *                 their name)
     * @author Sophie Kaiser
     */
    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * get the title of nobility of a speaker
     *
     * @return the title of nobility of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public String getNobility() {
        return this.nobility;
    }

    /**
     * set the title of nobility of a speaker
     *
     * @param nobility the title of nobility of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public void setNobility(String nobility) {
        this.nobility = nobility;
    }

    /**
     * get the prefix to the last name of a speaker
     *
     * @return prefix to the last name of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * set the prefix to the last name of a speaker
     *
     * @param prefix prefix to the last name of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * get the title a speaker is addressed with
     *
     * @return the title the speaker is addressed with
     * @author Sophie Kaiser
     */
    @Override
    public String getAddressTitle() {
        return this.addressTitle;
    }

    /**
     * set the title a speaker is addressed with
     *
     * @param addressTitle the title the speaker is addressed with
     * @author Sophie Kaiser
     */
    @Override
    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    /**
     * get the academic title of a speaker
     *
     * @return the academic title of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public String getAcademicTitle() {
        return this.academicTitle;
    }

    /**
     * set the academic title of a speaker
     *
     * @param academicTitle the academic title of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public void setAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
    }

    /**
     * get the date of birth of a speaker
     *
     * @return the date of birth of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    /**
     * set the date of birth of a speaker
     *
     * @param dateOfBirth the date of birth of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * get the place of birth of a speaker
     *
     * @return the place of birth of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public String getPlaceOfBirth() {
        return this.placeOfBirth;
    }

    /**
     * set the place of birth of a speaker
     *
     * @param placeOfBirth the place of birth of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    /**
     * get the country of birth of a speaker
     *
     * @return the country of birth of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public String getCountryOfBirth() {
        return this.countryOfBirth;
    }

    /**
     * set the country of birth of a speaker
     *
     * @param countryOfBirth the country of birth of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public void setCountryOfBirth(String countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    /**
     * get the date of death of a speaker
     *
     * @return the date of death of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public LocalDate getDateOfDeath() {
        return this.dateOfDeath;
    }

    /**
     * set the date of death of a speaker
     *
     * @param dateOfDeath the date of death of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public void setDateOfDeath(LocalDate dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    /**
     * get the gender of a speaker
     *
     * @return the gender of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public String getGender() {
        return this.gender;
    }

    /**
     * set the gender of a speaker
     *
     * @param gender the gender of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * get the marital status of a speaker
     *
     * @return the marital status of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public String getMaritalStatus() {
        return this.maritalStatus;
    }

    /**
     * set the marital status of a speaker
     *
     * @param maritalStatus the marital status of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    /**
     * get the religion of a speaker
     *
     * @return the religion of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public String getReligion() {
        return this.religion;
    }

    /**
     * set the religion of a speaker
     *
     * @param religion the religion of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public void setReligion(String religion) {
        this.religion = religion;
    }

    /**
     * get the profession of a speaker
     *
     * @return the profession of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public String getProfession() {
        return this.profession;
    }

    /**
     * set the profession of a speaker
     *
     * @param profession the profession of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public void setProfession(String profession) {
        this.profession = profession;
    }

    /**
     * get the curriculum vitae of a speaker
     *
     * @return the curriculum vitae of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public String getCurriculumVitae() {
        return this.curriculumVitae;
    }

    /**
     * set the curriculum vitae of a speaker
     *
     * @param curriculumVitae the curriculum vitae of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public void setCurriculumVitae(String curriculumVitae) {
        this.curriculumVitae = curriculumVitae;
    }

    /**
     * get the party of a speaker
     *
     * @return the party of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public Party getParty() {
        return this.party;
    }

    /**
     * set the party of a speaker,
     * add the speaker to the party,
     * if applicable, remove the speaker from old party
     *
     * @param party the party of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public void setParty(Party party) {
        // if a new party is set, remove speaker from the old party
        if (this.party != null && this.party != party) {
            party.removeMember(this);
        }
        this.party = party;
        // add speaker to party
        this.party.addMember(this);
    }

    /**
     * get the grouping of a speaker
     *
     * @return the grouping of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public Grouping getGrouping() {
        return this.grouping;
    }

    /**
     * set the grouping of a speaker,
     * add the speaker to the grouping,
     * if applicable, remove the speaker from old grouping
     *
     * @param grouping the grouping of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public void setGrouping(Grouping grouping) {
        // if a new grouping is set, remove speaker from the old grouping
        if (this.grouping != null && this.grouping != grouping) {
            grouping.removeMember(this);
        }
        this.grouping = grouping;
        // add speaker to grouping
        this.grouping.addMember(this);
    }

    /**
     * get the portrait picture of a speaker
     *
     * @return the portrait picture of the speaker
     * @author Sophie Kaiser
     */
    @Override
    public Picture getPicture() {
        return this.picture;
    }

    /**
     * set the portrait picture of a speaker,
     * set the speaker depicted in the picture to this speaker
     *
     * @param picture the portrait picture of the speaker
     * @author Sophie Kaiser
     * @author Oliwia Daszczynska
     */
    @Override
    public void setPicture(Picture picture) {
        this.picture = picture;
        if (this.picture.getSpeaker()==null){   // if this picture doesn't have a speaker yet
            this.picture.setSpeaker(this);      // set this speaker as being depicted in this picture
        }
    }
}
