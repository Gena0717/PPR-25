package parliament;

import java.util.Set;

/**
 * represents a party that is represented in the Bundestag
 * @author Sophie Kaiser
 */
public interface Party {

    /**
     * get the name of a party
     * @return the name of the party
     * @author Sophie Kaiser
     */
    String getName();

    /**
     * set the name of a party
     * @param name set the name of the party
     * @author Sophie Kaiser
     */
    void setName(String name);

    /**
     * get all members of a party
     * @return all members of the party
     * @author Sophie Kaiser
     */
    Set<Speaker> getMembers();

    /**
     * add a speaker to a party as a member
     * @param member speaker to be added to the party as a member;
     *               this speaker should be a member of the Bundestag
     * @author Sophie Kaiser
     */
    void addMember(Speaker member);

    /**
     * remove a speaker from a party as a member
     * @param member speaker to be removed from the party as a member
     * @author Sophie Kaiser
     */
    void removeMember(Speaker member);
}
